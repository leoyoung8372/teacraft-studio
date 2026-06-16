import request from '@/utils/request'

/** 商品列表 */
export const list = (params) => request({ url: '/api/admin/products', method: 'GET', params })

/** 新增/编辑商品 */
export const save = (data) => request({ url: '/api/admin/products', method: 'POST', data })

/** 删除商品 */
export const remove = (id) => request({ url: `/api/admin/products/${id}`, method: 'DELETE' })
