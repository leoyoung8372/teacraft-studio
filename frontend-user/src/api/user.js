import request from '@/utils/request'

/**
 * 用户 API —— 封装登录和注册接口
 */

/** 用户注册 */
export const register = (data) => {
  return request({
    url: '/api/user/register',
    method: 'POST',
    data
  })
}

/** 用户登录，返回 Result<LoginResponse> */
export const login = (data) => {
  return request({
    url: '/api/user/login',
    method: 'POST',
    data
  })
}
