import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import HomeView from '@/views/HomeView.vue'
import ProductManage from '@/views/ProductManage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: LoginView
    },
    {
      path: '/',
      component: HomeView,
      // 子路由：后续在 router-view 中展示
      children: [
        { path: 'products', component: ProductManage },
        { path: '', redirect: '/products' }  // 默认跳转库存管理
      ]
    }
  ]
})

// 路由守卫：未登录时强制跳转登录页
router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    return '/login' // 未登录 → 跳登录
  }
  if (to.path === '/login' && token) {
    return '/' // 已登录 → 跳主页
  }
})

export default router
