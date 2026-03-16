import request from "../utils/request";

export function getActiveBanners() {
  return request({
    url: "/banners/active",
    method: "get"
  });
}

export function saveBanner(data) {
  return request({
    url: "/banners",
    method: "post",
    data
  });
}
