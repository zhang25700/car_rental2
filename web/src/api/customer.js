import request from "../utils/request";

export function getCustomers() {
  return request({
    url: "/customers",
    method: "get"
  });
}

export function saveCustomer(data) {
  return request({
    url: "/customers",
    method: "post",
    data
  });
}
