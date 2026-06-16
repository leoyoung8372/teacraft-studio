<template>
  <view class="order-page">
    <!-- 左侧分类列表 -->
    <scroll-view scroll-y class="category-sidebar">
      <view
        v-for="cat in categories"
        :key="cat.id"
        class="category-item"
        :class="{ active: activeId === cat.id }"
        @tap="switchCategory(cat.id)"
      >
        <text>{{ cat.name }}</text>
      </view>
    </scroll-view>

    <!-- 右侧商品列表 -->
    <scroll-view scroll-y class="product-list">
      <view v-for="prod in products" :key="prod.id" class="product-card" @tap="goDetail(prod)">
        <image
          :src="prod.imageUrl || defaultImg"
          class="product-img"
          mode="aspectFill"
        />
        <view class="product-info">
          <text class="product-name">{{ prod.name }}</text>
          <text class="product-desc">{{ truncate(prod.description) }}</text>
          <text class="product-price">￥{{ prod.price }}</text>
        </view>
      </view>
      <!-- 无商品提示 -->
      <view v-if="!loading && products.length === 0" class="empty-tip">
        <text>该分类暂无商品</text>
      </view>
    </scroll-view>

    <!-- 浮动购物车栏（非空时显示） -->
    <view v-if="cartCount > 0" class="cart-bar" @tap="goCheckout">
      <view class="cart-info">
        <view class="cart-count">{{ cartCount }}</view>
        <text class="cart-label">已选商品</text>
      </view>
      <text class="cart-total">￥{{ cartTotal.toFixed(2) }}</text>
      <view class="checkout-btn"><text>结算</text></view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { list } from '@/api/category'
import { listByCategory } from '@/api/product'
import { list as listCart } from '@/api/cart'

const IMG_BASE = 'http://localhost:8080/images'
const defaultImg = `${IMG_BASE}/product/iceChocolate.jpeg`

const categories = ref([])
const products = ref([])
const activeId = ref(null)
const loading = ref(false)
const cartItems = ref([])      // 购物车列表
const cartTotal = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.subtotal, 0)
})
const cartCount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
})

// 加载分类列表，默认选中第一个
onMounted(async () => {
  const res = await list()
  categories.value = res.data
  if (categories.value.length > 0) {
    activeId.value = categories.value[0].id
    fetchProducts()
  }
  fetchCart()
})

// 从详情页返回时刷新购物车
onShow(() => {
  fetchCart()
})

// 截断文字：超过10字显示省略号
const truncate = (text) => {
  if (!text) return '暂无简介'
  return text.length > 10 ? text.substring(0, 10) + '...' : text
}

// 跳转结算页
const goCheckout = () => {
  uni.navigateTo({ url: '/pages/checkout/checkout' })
}

// 跳转商品详情
const goDetail = (prod) => {
  getApp().globalData.product = prod
  uni.navigateTo({ url: '/pages/detail/detail' })
}

// 切换分类
const switchCategory = (id) => {
  if (activeId.value === id) return
  activeId.value = id
  fetchProducts()
}

// 加载购物车数据
const fetchCart = async () => {
  try {
    const res = await listCart()
    cartItems.value = res.data
  } catch (e) { /* 忽略 */ }
}

// 按分类加载商品
const fetchProducts = async () => {
  loading.value = true
  try {
    const res = await listByCategory(activeId.value)
    products.value = res.data
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.order-page {
  display: flex;
  height: 100vh;
}

/* ===== 左侧分类 ===== */
.category-sidebar {
  width: 180rpx;
  background-color: #f5f5f5;
  flex-shrink: 0;
}
.category-item {
  padding: 28rpx 16rpx;
  text-align: center;
  font-size: 26rpx;
  color: #666;
  border-left: 6rpx solid transparent;
}
.category-item.active {
  background-color: #fff;
  color: #07c160;
  font-weight: bold;
  border-left-color: #07c160;
}

/* ===== 右侧商品 ===== */
.product-list {
  flex: 1;
  padding: 16rpx 16rpx 120rpx; /* 底部留空给浮动购物车 */
}
.product-card {
  display: flex;
  background: #fff;
  border-radius: 12rpx;
  padding: 16rpx;
  margin-bottom: 16rpx;
}
.product-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 8rpx;
  flex-shrink: 0;
}
.product-info {
  flex: 1;
  margin-left: 20rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.product-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}
.product-desc {
  font-size: 24rpx;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-price {
  font-size: 28rpx;
  font-weight: bold;
  color: #e74c3c;
}
.empty-tip {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 26rpx;
}

/* ===== 浮动购物车栏 ===== */
.cart-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  background: #fff;
  padding: 16rpx 24rpx;
  box-shadow: 0 -2rpx 16rpx rgba(0,0,0,0.08);
}
.cart-info {
  display: flex;
  align-items: center;
}
.cart-count {
  width: 44rpx;
  height: 44rpx;
  background: #07c160;
  color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 44rpx;
  font-size: 22rpx;
  font-weight: bold;
}
.cart-label {
  margin-left: 10rpx;
  font-size: 26rpx;
  color: #666;
}
.cart-total {
  flex: 1;
  text-align: right;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-right: 24rpx;
}
.checkout-btn {
  background: #07c160;
  border-radius: 40rpx;
  padding: 14rpx 36rpx;
  color: #fff;
  font-size: 26rpx;
  font-weight: bold;
}
</style>
