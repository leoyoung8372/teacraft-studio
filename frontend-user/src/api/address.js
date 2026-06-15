import request from '@/utils/request'

/**
 * 地址 API
 */

/** 获取用户所有地址 */
export const list = () => {
  return request({ url: '/api/address/list', method: 'GET' })
}

/** 新增/编辑地址 */
export const save = (data) => {
  return request({ url: '/api/address/save', method: 'POST', data })
}

/** 删除地址 */
export const remove = (id) => {
  return request({ url: `/api/address/${id}`, method: 'DELETE' })
}
