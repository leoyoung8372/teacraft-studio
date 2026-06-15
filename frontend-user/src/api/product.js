import request from '@/utils/request'

/**
 * 商品 API —— 获取商品列表
 */

export const listByCategory = (categoryId) => {
  return request({
    url: `/api/products?categoryId=${categoryId}`,
    method: 'GET'
  })
}
