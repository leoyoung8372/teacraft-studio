<template>
  <div class="login-container">
    <div class="login-card">
      <!-- 头部 Logo 区 -->
      <div class="login-header">
        <div class="logo">🍵</div>
        <h2>TeaCraft Studio</h2>
        <p>商家管理后台</p>
      </div>

      <!-- 表单 -->
      <el-form :model="form" size="large">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="用户名"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="handleLogin"
            class="login-btn"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const form = ref({ username: '', password: '' })

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await request.post('/api/admin/login', {
      username: form.value.username,
      password: form.value.password
    })
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('adminId', res.data.adminId)
    localStorage.setItem('username', res.data.username)
    router.push('/')
  } catch (e) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #e8f4f0 0%, #d4e8e2 50%, #c5ddd6 100%);
}

.login-card {
  width: 420px;
  padding: 48px 40px 32px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.08);
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.logo {
  font-size: 48px;
  margin-bottom: 12px;
}

.login-header h2 {
  margin: 0 0 8px 0;
  font-size: 22px;
  color: #2c3e50;
  font-weight: 600;
}

.login-header p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  border-radius: 8px;
}
</style>
