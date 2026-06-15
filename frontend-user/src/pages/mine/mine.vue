<template>
  <view class="mine-page">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <image :src="user.avatarUrl || defaultAvatar" class="avatar" mode="aspectFill" />
      <text class="nickname">{{ user.nickname || '未设置' }}</text>
      <text class="phone">{{ user.phone || '' }}</text>
    </view>

    <!-- 功能入口 -->
    <view class="menu-list">
      <view class="menu-item" @tap="goAddress">
        <text class="menu-icon">📍</text>
        <text class="menu-text">地址管理</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-btn" @tap="handleLogout">
      <text>退出登录</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const IMG_BASE = 'http://localhost:8080/images'
const defaultAvatar = `${IMG_BASE}/logo.png`

const user = ref({ nickname: '', phone: '', avatarUrl: '' })

onMounted(() => {
  const info = uni.getStorageSync('userInfo')
  if (info) {
    user.value = info
  }
  // 补充手机号（登录时未保存，从 token 解析不到的话就用缓存）
  const phone = uni.getStorageSync('phone')
  if (phone) user.value.phone = phone
})

const goAddress = () => {
  uni.navigateTo({ url: '/pages/address/address' })
}

const handleLogout = () => {
  uni.clearStorageSync()
  uni.reLaunch({ url: '/pages/login/login' })
}
</script>

<style scoped>
.mine-page {
  min-height: 100vh;
  background: #f5f5f5;
}
.user-card {
  background: #07c160;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 0 40rpx;
}
.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: #fff;
}
.nickname {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-top: 16rpx;
}
.phone {
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 6rpx;
}
.menu-list {
  margin: 20rpx 0;
  background: #fff;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.menu-icon {
  font-size: 36rpx;
  margin-right: 16rpx;
}
.menu-text {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}
.menu-arrow {
  font-size: 36rpx;
  color: #ccc;
}
.logout-btn {
  margin: 40rpx 24rpx;
  background: #fff;
  border-radius: 12rpx;
  text-align: center;
  padding: 24rpx;
  font-size: 28rpx;
  color: #e74c3c;
}
</style>
