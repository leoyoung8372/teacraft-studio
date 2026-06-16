import request from '@/utils/request'

/**
 * 商家端订单 API
 */

/** 待处理订单 */
export const listPending = () => request({ url: '/api/admin/orders/pending', method: 'GET' })

/** 完成订单 */
export const complete = (id) => request({ url: `/api/admin/orders/${id}/complete`, method: 'PUT' })

/** 取消订单 */
export const cancel = (id) => request({ url: `/api/admin/orders/${id}/cancel`, method: 'PUT' })

/** 历史订单 */
export const listHistory = () => request({ url: '/api/admin/orders/history', method: 'GET' })
