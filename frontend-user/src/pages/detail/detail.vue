<template>
  <view class="detail-page" v-if="product">
    <!-- 商品大图 -->
    <image :src="product.imageUrl || defaultImg" class="product-img" mode="aspectFill" />

    <!-- 基本信息 -->
    <view class="info-section">
      <text class="name">{{ product.name }}</text>
      <text class="desc">{{ product.description || '暂无简介' }}</text>
      <text class="price">￥{{ product.price }}</text>
    </view>

    <!-- 规格选择 -->
    <view class="spec-section">
      <!-- 甜度 -->
      <view class="spec-group">
        <text class="spec-label">甜度</text>
        <view class="spec-options">
          <text v-for="s in SWEETNESS" :key="s" class="spec-tag"
            :class="{ active: form.sweetness === s }" @tap="form.sweetness = s">{{ s }}</text>
        </view>
      </view>
      <!-- 温度 -->
      <view class="spec-group">
        <text class="spec-label">温度</text>
        <view class="spec-options">
          <text v-for="t in TEMPERATURE" :key="t" class="spec-tag"
            :class="{ active: form.temperature === t }" @tap="form.temperature = t">{{ t }}</text>
        </view>
      </view>
      <!-- 杯型 -->
      <view class="spec-group">
        <text class="spec-label">杯型</text>
        <view class="spec-options">
          <text v-for="c in CUP_SIZES" :key="c" class="spec-tag"
            :class="{ active: form.cupSize === c }" @tap="form.cupSize = c">{{ c }}</text>
        </view>
      </view>
      <!-- 加料 -->
      <view class="spec-group">
        <text class="spec-label">加料（多选）</text>
        <view class="spec-options">
          <text v-for="a in ADD_ONS" :key="a" class="spec-tag"
            :class="{ active: selectedAddOns.includes(a) }" @tap="toggleAddOn(a)">{{ a }}</text>
        </view>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <!-- 上排：金额 + 数量 -->
      <view class="top-row">
        <text class="total-price">￥{{ (product.price * qty).toFixed(2) }}</text>
        <view class="quantity-stepper">
          <text class="qty-btn" @tap="qty > 1 && qty--">−</text>
          <text class="qty-val">{{ qty }}</text>
          <text class="qty-btn" @tap="qty++">+</text>
        </view>
      </view>
      <!-- 下排：两个按钮 -->
      <view class="btn-row">
        <view class="cart-btn" @tap="handleAdd">
          <text>加入购物车</text>
        </view>
        <view class="buy-btn" @tap="handleBuyNow">
          <text>立即购买</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { add } from '@/api/cart'

const IMG_BASE = 'http://localhost:8080/images'
const defaultImg = `${IMG_BASE}/product/iceChocolate.jpeg`

// 固定规格选项（文档要求：固定选项，不动态扩展）
const SWEETNESS = ['全糖', '七分糖', '半糖', '三分糖', '无糖']
const TEMPERATURE = ['热', '温', '常温', '去冰', '少冰']
const CUP_SIZES = ['中杯', '大杯']
const ADD_ONS = ['珍珠', '椰果', '奶盖', '红豆']

const product = ref(null)
const qty = ref(1)
const selectedAddOns = ref([]) // 加料多选
const form = ref({ sweetness: '', temperature: '', cupSize: '' })

onMounted(() => {
  // 从页面参数获取商品数据
  const data = getApp().globalData?.product
  if (data) {
    product.value = data
    getApp().globalData.product = null // 用完清掉
  }
})

// 加料多选切换
const toggleAddOn = (item) => {
  const idx = selectedAddOns.value.indexOf(item)
  if (idx >= 0) {
    selectedAddOns.value.splice(idx, 1)
  } else {
    selectedAddOns.value.push(item)
  }
}

// 加入购物车
const handleAdd = async () => {
  try {
    await add({
      productId: product.value.id,
      quantity: qty.value,
      sweetness: form.value.sweetness || null,
      temperature: form.value.temperature || null,
      cupSize: form.value.cupSize || null,
      addOns: selectedAddOns.value.length > 0 ? selectedAddOns.value.join(',') : null
    })
    uni.showToast({ title: '已加入购物车', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 500)
  } catch (e) {}
}

// 立即购买：加入购物车后直接跳结算页
const handleBuyNow = async () => {
  try {
    await add({
      productId: product.value.id,
      quantity: qty.value,
      sweetness: form.value.sweetness || null,
      temperature: form.value.temperature || null,
      cupSize: form.value.cupSize || null,
      addOns: selectedAddOns.value.length > 0 ? selectedAddOns.value.join(',') : null
    })
    uni.navigateTo({ url: '/pages/checkout/checkout' })
  } catch (e) {}
}
</script>

<style scoped>
.detail-page {
  padding-bottom: 120rpx;
}
.product-img {
  width: 100%;
  height: 480rpx;
}
.info-section {
  padding: 24rpx;
  background: #fff;
}
.name {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  display: block;
}
.desc {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}
.price {
  font-size: 36rpx;
  font-weight: bold;
  color: #e74c3c;
  margin-top: 12rpx;
  display: block;
}

/* 规格 */
.spec-section {
  margin-top: 16rpx;
  background: #fff;
  padding: 24rpx;
}
.spec-group {
  margin-bottom: 20rpx;
}
.spec-label {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
  display: block;
}
.spec-options {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}
.spec-tag {
  padding: 10rpx 24rpx;
  font-size: 24rpx;
  border: 1px solid #ddd;
  border-radius: 8rpx;
  color: #666;
}
.spec-tag.active {
  border-color: #07c160;
  color: #07c160;
  background: rgba(7,193,96,0.06);
}

/* 底部栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 16rpx 24rpx 20rpx;
  box-shadow: 0 -2rpx 12rpx rgba(0,0,0,0.06);
}
.top-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}
.total-price {
  font-size: 34rpx;
  font-weight: bold;
  color: #e74c3c;
}
.quantity-stepper {
  display: flex;
  align-items: center;
}
.qty-btn {
  width: 56rpx;
  height: 56rpx;
  border: 1px solid #ddd;
  border-radius: 8rpx;
  text-align: center;
  line-height: 56rpx;
  font-size: 32rpx;
}
.qty-val {
  margin: 0 20rpx;
  font-size: 30rpx;
  font-weight: bold;
}
.btn-row {
  display: flex;
  gap: 20rpx;
}
.cart-btn {
  flex: 1;
  background: #fff;
  border: 2rpx solid #333;
  border-radius: 44rpx;
  padding: 20rpx;
  text-align: center;
  color: #333;
  font-size: 28rpx;
  font-weight: bold;
}
.buy-btn {
  flex: 1;
  background: #e74c3c;
  border-radius: 44rpx;
  padding: 20rpx;
  text-align: center;
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
}
</style>
