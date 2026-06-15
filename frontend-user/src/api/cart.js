import request from '@/utils/request'

/**
 * 购物车 API
 */

/** 加入购物车 */
export const add = (data) => {
  return request({
    url: '/api/cart/add',
    method: 'POST',
    data
  })
}

/** 查看购物车 */
export const list = () => {
  return request({
    url: '/api/cart/list',
    method: 'GET'
  })
}

/** 修改数量 */
export const updateQuantity = (id, quantity) => {
  return request({
    url: `/api/cart/${id}`,
    method: 'PUT',
    data: { quantity }
  })
}

/** 删除条目 */
export const remove = (id) => {
  return request({
    url: `/api/cart/${id}`,
    method: 'DELETE'
  })
}
