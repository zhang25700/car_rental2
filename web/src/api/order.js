import request from "../utils/request";

export function getOrders(params) {
  return request({
    url: "/orders",
    method: "get",
    params
  });
}

export function createOrder(data) {
  return request({
    url: "/orders",
    method: "post",
    data
  });
}

export function updateOrderStatus(id, status) {
  return request({
    url: `/orders/${id}/status`,
    method: "put",
    params: { status }
  });
}
