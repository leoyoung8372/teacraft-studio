<template>
  <view class="container">
    <view class="header">
      <text class="title">TeaCraft Studio</text>
      <text class="subtitle">欢迎回来</text>
    </view>

    <view class="form">
      <input v-model="phone" type="number" placeholder="请输入手机号" maxlength="11" class="input" />
      <input v-model="password" type="password" placeholder="请输入密码" class="input" />

      <button :loading="loading" @tap="handleLogin" class="btn">登 录</button>

      <view class="link" @tap="goRegister">还没有账号？立即注册</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '@/api/user'

const phone = ref('')
const password = ref('')
const loading = ref(false)

const handleLogin = async () => {
  if (!phone.value || !password.value) {
    uni.showToast({ title: '请输入手机号和密码', icon: 'none' })
    return
  }
  loading.value = true
  try {
    const res = await login({ phone: phone.value, password: password.value })
    // 保存登录信息到本地缓存
    uni.setStorageSync('token', res.data.token)
    uni.setStorageSync('userInfo', {
      userId: res.data.userId,
      nickname: res.data.nickname,
      avatarUrl: res.data.avatarUrl
    })
    uni.showToast({ title: '登录成功', icon: 'success' })
    // 跳转到主页
    setTimeout(() => {
      uni.reLaunch({ url: '/pages/index/index' })
    }, 500)
  } catch (e) {
    // 错误已在 request.js 中处理
  } finally {
    loading.value = false
  }
}

const goRegister = () => {
  uni.navigateTo({ url: '/pages/register/register' })
}
</script>

<style scoped>
.container {
  padding: 80rpx 48rpx;
}
.header {
  margin-bottom: 64rpx;
}
.title {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 12rpx;
}
.subtitle {
  font-size: 28rpx;
  color: #909399;
}
.input {
  border: 1px solid #e0e0e0;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
  font-size: 28rpx;
}
.btn {
  width: 100%;
  background-color: #07c160;
  color: #fff;
  border: none;
  border-radius: 12rpx;
  padding: 24rpx;
  font-size: 32rpx;
  margin-top: 24rpx;
}
.link {
  text-align: center;
  margin-top: 36rpx;
  font-size: 26rpx;
  color: #07c160;
}
</style>
