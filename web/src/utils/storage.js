const ACCESS_TOKEN_KEY = "car_rental_access_token";
const REFRESH_TOKEN_KEY = "car_rental_refresh_token";
const USER_ROLE_KEY = "car_rental_user_role";

export function getAccessToken() {
  return localStorage.getItem(ACCESS_TOKEN_KEY) || "";
}

export function getRefreshToken() {
  return localStorage.getItem(REFRESH_TOKEN_KEY) || "";
}

export function getUserRole() {
  return localStorage.getItem(USER_ROLE_KEY) || "";
}

export function setTokens(accessToken, refreshToken, role) {
  localStorage.setItem(ACCESS_TOKEN_KEY, accessToken || "");
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken || "");
  localStorage.setItem(USER_ROLE_KEY, role || "");
}

export function clearTokens() {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
  localStorage.removeItem(USER_ROLE_KEY);
}
