<template>
  <view class="home">
    <!-- ① 品牌名 -->
    <view class="header">
      <text class="brand">TeaCraft Studio</text>
    </view>

    <!-- ② 轮播 Banner -->
    <view class="banner-wrapper">
      <swiper class="banner-swiper" indicator-dots autoplay circular interval="3000">
        <swiper-item v-for="(img, i) in banners" :key="i">
          <image :src="img" class="banner-img" mode="aspectFill" />
        </swiper-item>
      </swiper>
    </view>

    <!-- ③ 促销广告横幅（暂时注释）
    <view class="ad-wrapper">
      <image :src="IMG_BASE + '/banner/ad.png'" class="ad-img" mode="widthFix" />
    </view>
    -->

    <!-- ④ 服务承诺 -->
    <view class="promise-row">
      <view class="promise-card">
        <text class="promise-icon">🍃</text>
        <text class="promise-title">新鲜现做</text>
        <text class="promise-desc">每日鲜果直达，现点现做</text>
      </view>
      <view class="promise-card">
        <text class="promise-icon">🏅</text>
        <text class="promise-title">严选原料</text>
        <text class="promise-desc">全球产地直采，品质保障</text>
      </view>
      <view class="promise-card">
        <text class="promise-icon">⚡</text>
        <text class="promise-title">快速出杯</text>
        <text class="promise-desc">高效制作，无需久等</text>
      </view>
    </view>

    <!-- ⑤ 自提 / 外卖入口 -->
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

    <!-- ⑥ 品牌故事 -->
    <view class="story">
      <text class="story-title">品牌故事</text>
      <text class="story-text">TeaCraft Studio 创立于 2024 年，我们相信每一杯茶饮都值得被认真对待。从茶叶的甄选到配方的研发，从水果的采买到制作的工艺，我们坚持手作精神，拒绝工业化的千篇一律。每一杯递到你手中的，不止是饮品，更是一份用心与温度。</text>
    </view>

    <!-- ⑦ 口号 -->
    <view class="brand-footer">
      <text class="slogan-bottom">每一杯，都是手作之美</text>
    </view>

    <!-- ⑧ 底部 -->
    <view class="footer">
      <text>TeaCraft Studio © 2026</text>
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
.home { background: #f8f8f8; padding-bottom: 40rpx; }

/* ===== ① 顶部品牌 ===== */
.header {
  display: flex; justify-content: center;
  padding: 36rpx 0 20rpx; background: #fff;
}
.brand { font-size: 38rpx; font-weight: bold; color: #2c3e50; }

/* ===== ② 轮播 ===== */
.banner-wrapper { padding: 24rpx 24rpx 0; }
.banner-swiper { width: 100%; height: 340rpx; border-radius: 16rpx; overflow: hidden; }
.banner-img { width: 100%; height: 100%; }

/* ===== ③ 广告横幅 ===== */
.ad-wrapper { padding: 16rpx 24rpx; }
.ad-img { width: 100%; border-radius: 16rpx; display: block; }

/* ===== ④ 服务承诺三卡片 ===== */
.promise-row { display: flex; gap: 16rpx; padding: 8rpx 24rpx 20rpx; }
.promise-card {
  flex: 1; background: #fff; border-radius: 16rpx;
  padding: 24rpx 12rpx; text-align: center;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.03);
}
.promise-icon { font-size: 48rpx; display: block; margin-bottom: 10rpx; }
.promise-title { font-size: 24rpx; font-weight: 600; color: #333; }
.promise-desc { font-size: 20rpx; color: #999; margin-top: 4rpx; display: block; }

/* ===== ⑤ 自提/外卖 ===== */
.service-row { display: flex; gap: 20rpx; padding: 0 24rpx 20rpx; }
.service-card {
  flex: 1; display: flex; align-items: center; background: #fff;
  border-radius: 16rpx; padding: 28rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.04);
}
.service-icon { font-size: 52rpx; margin-right: 16rpx; }
.service-text { display: flex; flex-direction: column; }
.service-title { font-size: 28rpx; font-weight: 600; color: #333; }
.service-desc { font-size: 22rpx; color: #999; margin-top: 4rpx; }

/* ===== ⑥ 品牌故事 ===== */
.story {
  margin: 0 24rpx 20rpx; background: #fff;
  border-radius: 16rpx; padding: 32rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.03);
}
.story-title { font-size: 30rpx; font-weight: bold; color: #333; display: block; margin-bottom: 16rpx; }
.story-text { font-size: 24rpx; color: #666; line-height: 1.8; }

/* ===== ⑦ Logo + 口号 ===== */
.brand-footer {
  display: flex; flex-direction: column; align-items: center;
  padding: 36rpx 0 20rpx;
}
.slogan-bottom { font-size: 22rpx; color: #bbb; }

/* ===== ⑧ 底部 ===== */
.footer { text-align: center; padding: 32rpx 0 16rpx; }
.footer text { font-size: 22rpx; color: #ccc; }
</style>
