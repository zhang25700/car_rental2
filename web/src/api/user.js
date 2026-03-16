import request from "../utils/request";

export function getUsers() {
  return request({
    url: "/users",
    method: "get"
  });
}

export function saveUser(data) {
  return request({
    url: "/users",
    method: "post",
    data
  });
}
