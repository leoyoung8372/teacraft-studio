<template>
  <view class="home">
    <!-- ① 顶部：Logo + 品牌名 -->
    <view class="header">
      <image src="http://localhost:8080/images/logo.png" class="logo" mode="aspectFit" />
      <text class="brand">TeaCraft Studio</text>
      <text class="slogan">每一杯，都是手作之美</text>
    </view>

    <!-- ② 轮播图 -->
    <view class="banner-wrapper">
      <swiper class="banner-swiper" indicator-dots autoplay circular interval="3000">
        <swiper-item v-for="(img, i) in banners" :key="i">
          <image :src="img" class="banner-img" mode="aspectFill" />
        </swiper-item>
      </swiper>
    </view>

    <!-- ③ 自提 / 外卖入口 -->
    <view class="service-row">
      <view class="service-card">
        <text class="service-icon">🏪</text>
        <view class="service-text">
          <text class="service-title">门店自提</text>
          <text class="service-desc">到店即取，无需等待</text>
        </view>
      </view>
      <view class="service-card">
        <text class="service-icon">🛵</text>
        <view class="service-text">
          <text class="service-title">外卖配送</text>
          <text class="service-desc">新鲜直达，即刻享受</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
const IMG_BASE = 'http://localhost:8080/images'
const banners = [
  `${IMG_BASE}/banner/b1.png`,
  `${IMG_BASE}/banner/b2.png`,
  `${IMG_BASE}/banner/b3.png`,
  `${IMG_BASE}/banner/b4.png`
]
</script>

<style scoped>
.home {
  padding-bottom: 20rpx;
}

/* ===== 顶部品牌区 ===== */
.header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 0 24rpx;
}
.logo {
  width: 120rpx;
  height: 120rpx;
  border-radius: 24rpx;
}
.brand {
  margin-top: 16rpx;
  font-size: 36rpx;
  font-weight: bold;
  color: #2c3e50;
}
.slogan {
  margin-top: 6rpx;
  font-size: 24rpx;
  color: #999;
}

/* ===== 轮播图 ===== */
.banner-wrapper {
  padding: 0 24rpx;
}
.banner-swiper {
  width: 100%;
  height: 340rpx;
  border-radius: 16rpx;
  overflow: hidden;
}
.banner-img {
  width: 100%;
  height: 100%;
}

/* ===== 自提/外卖 ===== */
.service-row {
  display: flex;
  gap: 20rpx;
  padding: 32rpx 24rpx;
}
.service-card {
  flex: 1;
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.04);
}
.service-icon {
  font-size: 52rpx;
  margin-right: 16rpx;
}
.service-text {
  display: flex;
  flex-direction: column;
}
.service-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}
.service-desc {
  font-size: 22rpx;
  color: #999;
  margin-top: 4rpx;
}
</style>
