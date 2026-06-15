<template>
  <view class="container">
    <view class="header">
      <text class="title">创建账号</text>
      <text class="subtitle">加入 TeaCraft Studio</text>
    </view>

    <view class="form">
      <input v-model="nickname" placeholder="请输入昵称" class="input" />
      <input v-model="phone" type="number" placeholder="请输入手机号" maxlength="11" class="input" />
      <input v-model="password" type="password" placeholder="请输入密码" class="input" />

      <button :loading="loading" @tap="handleRegister" class="btn">注 册</button>

      <view class="link" @tap="goLogin">已有账号？去登录</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { register } from '@/api/user'

const nickname = ref('')
const phone = ref('')
const password = ref('')
const loading = ref(false)

const handleRegister = async () => {
  if (!nickname.value || !phone.value || !password.value) {
    uni.showToast({ title: '请填写完整信息', icon: 'none' })
    return
  }
  loading.value = true
  try {
    await register({
      nickname: nickname.value,
      phone: phone.value,
      password: password.value
    })
    uni.showToast({ title: '注册成功，请登录', icon: 'success' })
    setTimeout(() => {
      uni.navigateBack() // 返回登录页
    }, 500)
  } catch (e) {
    // 错误已在 request.js 中处理
  } finally {
    loading.value = false
  }
}

const goLogin = () => {
  uni.navigateBack()
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
