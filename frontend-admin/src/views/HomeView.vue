<template>
  <div class="home-container">
    <el-container>
      <!-- 侧栏 -->
      <el-aside width="200px" class="sidebar">
        <el-menu :default-active="route.path" router class="side-menu">
          <el-menu-item index="/products">
            <span>库存管理</span>
          </el-menu-item>
          <el-menu-item index="/orders">
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="/order-history">
            <span>历史订单</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <!-- 内容区 -->
      <el-container class="main-area">
        <el-header>
          <span>欢迎，{{ username }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const username = localStorage.getItem('username') || '管理员'

const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.home-container {
  height: 100vh;
}
/* 侧栏 */
.sidebar {
  background-color: #FFFFFF !important;
}
.side-menu {
  background-color: #FFFFFF;
  border-right: none;
}
/* 菜单项选中态：浅灰背景 + 圆角 */
:deep(.el-menu-item.is-active) {
  background-color: #F2F0F0 !important;
  border-radius: 12px;
  margin: 4px 12px;
  width: auto;
}
/* 内容区 */
.main-area {
  background-color: #FDFCFC;
}
:deep(.el-main) {
  padding: 0;
}
.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
}
</style>
