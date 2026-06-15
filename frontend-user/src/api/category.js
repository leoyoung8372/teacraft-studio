import request from '@/utils/request'

/**
 * 分类 API —— 获取商品分类列表
 */

export const list = () => {
  return request({
    url: '/api/categories',
    method: 'GET'
  })
}
