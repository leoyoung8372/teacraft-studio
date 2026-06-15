/**
 * UniApp 网络请求封装
 * 统一 baseURL、自动携带 JWT、401 拦截、错误提示
 * 后续所有接口调用都通过这个函数，不在页面中直接写 uni.request
 */

const BASE_URL = 'http://localhost:8080'

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token') // 从本地缓存取 Token

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        // 有 Token 则自动携带
        ...(token ? { Authorization: `Bearer ${token}` } : {})
      },
      success: (res) => {
        const data = res.data
        // 后端统一返回 { code, message, data }
        if (data.code === 200) {
          resolve(data) // 成功，返回整个 Result 对象
        } else {
          uni.showToast({ title: data.message || '请求失败', icon: 'none' })
          reject(new Error(data.message))
        }
      },
      fail: (err) => {
        // 401 未登录 → 清缓存并跳转登录页
        if (err.statusCode === 401) {
          uni.removeStorageSync('token')
          uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' })
          uni.reLaunch({ url: '/pages/login/login' })
        } else {
          uni.showToast({ title: '网络错误', icon: 'none' })
        }
        reject(err)
      }
    })
  })
}

export default request
