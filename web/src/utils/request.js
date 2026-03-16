import axios from "axios";
import { Message } from "element-ui";
import { clearTokens, getAccessToken, getRefreshToken, getUserRole, setTokens } from "./storage";
import router from "../router";

const service = axios.create({
  baseURL: "/api",
  timeout: 8000
});

service.interceptors.request.use(config => {
  const accessToken = getAccessToken();
  const refreshToken = getRefreshToken();
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  if (refreshToken) {
    config.headers["X-Refresh-Token"] = refreshToken;
  }
  return config;
});

service.interceptors.response.use(
  response => {
    const newAccessToken = response.headers["x-new-access-token"];
    const newRefreshToken = response.headers["x-new-refresh-token"];
    if (newAccessToken && newRefreshToken) {
      setTokens(newAccessToken, newRefreshToken, getUserRole());
    }

    const body = response.data;
    if (body && body.code !== 200) {
      Message.error(body.message || "请求失败");
      return Promise.reject(new Error(body.message || "request error"));
    }
    return body;
  },
  async error => {
    const response = error.response;
    if (response && response.status === 401 && getRefreshToken()) {
      try {
        const refreshResult = await axios.post("/api/auth/refresh", {
          refreshToken: getRefreshToken()
        });
        if (refreshResult.data.code === 200) {
          setTokens(
            refreshResult.data.data.accessToken,
            refreshResult.data.data.refreshToken,
            refreshResult.data.data.role || getUserRole()
          );
          return service(error.config);
        }
      } catch (e) {
        clearTokens();
        router.replace("/login");
      }
    }

    const message = response?.data?.message || error.message || "网络异常";
    Message.error(message);
    return Promise.reject(error);
  }
);

export default service;
