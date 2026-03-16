import request from "../utils/request";

export function getVehicles(params) {
  return request({
    url: "/vehicles",
    method: "get",
    params
  });
}

export function getHotVehicles() {
  return request({
    url: "/vehicles/hot",
    method: "get"
  });
}

export function saveVehicle(data) {
  return request({
    url: "/vehicles",
    method: "post",
    data
  });
}

export function updateVehicleStatus(id, status) {
  return request({
    url: `/vehicles/${id}/status`,
    method: "put",
    params: { status }
  });
}
