import request from '@/utils/request'

/**
 * 订单 API
 */

/** 创建订单 */
export const create = (data) => {
  return request({ url: '/api/order/create', method: 'POST', data })
}

/** 订单列表 */
export const list = () => {
  return request({ url: '/api/order/list', method: 'GET' })
}

/** 订单详情 */
export const detail = (id) => {
  return request({ url: `/api/order/${id}`, method: 'GET' })
}
