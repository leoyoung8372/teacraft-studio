<template>
  <view class="checkout-page">
    <!-- 购物车商品 -->
    <view class="card">
      <text class="section-title">商品清单</text>
      <view v-for="item in cartItems" :key="item.cartItemId" class="item-row">
        <image :src="item.productImageUrl || defaultImg" class="item-img" mode="aspectFill" />
        <view class="item-info">
          <text class="item-name">{{ item.productName }}</text>
          <text class="item-specs" v-if="item.sweetness || item.temperature || item.cupSize">
            {{ [item.sweetness, item.temperature, item.cupSize].filter(Boolean).join(' / ') }}
          </text>
        </view>
        <text class="item-qty">x{{ item.quantity }}</text>
        <text class="item-price">￥{{ item.subtotal.toFixed(2) }}</text>
      </view>
    </view>

    <!-- 用餐方式 -->
    <view class="card">
      <text class="section-title">用餐方式</text>
      <view class="meal-switch">
        <view class="meal-option" :class="{ active: mealType === 'self_pickup' }"
          @tap="mealType = 'self_pickup'">
          <text>🏪 到店自取</text>
        </view>
        <view class="meal-option" :class="{ active: mealType === 'delivery' }"
          @tap="mealType = 'delivery'">
          <text>🛵 外卖配送</text>
        </view>
      </view>
    </view>

    <!-- 收货地址（外卖时显示） -->
    <view class="card" v-if="mealType === 'delivery'">
      <text class="section-title">收货地址</text>
      <view v-if="addresses.length === 0" class="empty-hint">
        <text>暂无地址，请先去「我的 → 地址管理」添加</text>
      </view>
      <view v-for="addr in addresses" :key="addr.id" class="addr-item"
        :class="{ selected: selectedAddrId === addr.id }" @tap="selectedAddrId = addr.id">
        <view class="addr-info">
          <text class="addr-name">{{ addr.receiverName }}  {{ addr.receiverPhone }}</text>
          <text class="addr-detail">{{ addr.detailAddress }}</text>
        </view>
        <text class="addr-check">{{ selectedAddrId === addr.id ? '✅' : '○' }}</text>
      </view>
    </view>

    <!-- 合计 + 下单 -->
    <view class="bottom-bar">
      <text class="total-label">合计：</text>
      <text class="total-price">￥{{ total.toFixed(2) }}</text>
      <view class="submit-btn" @tap="handleSubmit">
        <text>{{ submitting ? '提交中...' : '确认下单' }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { list as listCart } from '@/api/cart'
import { create as createOrder } from '@/api/order'
import { list as listAddress } from '@/api/address'

const IMG_BASE = 'http://localhost:8080/images'
const defaultImg = `${IMG_BASE}/product/iceChocolate.jpeg`

const cartItems = ref([])
const addresses = ref([])
const mealType = ref('self_pickup')     // self_pickup / delivery
const selectedAddrId = ref(null)
const submitting = ref(false)

const total = computed(() => cartItems.value.reduce((s, i) => s + i.subtotal, 0))

onMounted(async () => {
  const cartRes = await listCart()
  cartItems.value = cartRes.data
  const addrRes = await listAddress()
  addresses.value = addrRes.data
  // 默认选中第一个地址
  if (addresses.value.length > 0) {
    selectedAddrId.value = addresses.value[0].id
  }
})

const handleSubmit = async () => {
  if (cartItems.value.length === 0) {
    uni.showToast({ title: '购物车为空', icon: 'none' })
    return
  }
  if (mealType.value === 'delivery' && !selectedAddrId.value) {
    uni.showToast({ title: '请选择收货地址', icon: 'none' })
    return
  }
  submitting.value = true
  try {
    await createOrder({
      mealType: mealType.value,
      addressId: mealType.value === 'delivery' ? selectedAddrId.value : null
    })
    uni.showToast({ title: '下单成功', icon: 'success' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/orders/orders' }) // 跳转订单 Tab
    }, 500)
  } catch (e) {
    // 错误在 request 拦截器中处理
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.checkout-page {
  padding: 20rpx 24rpx 140rpx;
  background: #f5f5f5;
  min-height: 100vh;
}
.card {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}
.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 16rpx;
}

/* 商品 */
.item-row {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}
.item-img {
  width: 80rpx;
  height: 80rpx;
  border-radius: 8rpx;
  flex-shrink: 0;
}
.item-info {
  flex: 1;
  margin-left: 16rpx;
}
.item-name {
  font-size: 26rpx;
  color: #333;
}
.item-specs {
  font-size: 22rpx;
  color: #999;
  display: block;
}
.item-qty {
  font-size: 24rpx;
  color: #999;
  margin-right: 16rpx;
}
.item-price {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

/* 用餐方式 */
.meal-switch {
  display: flex;
  gap: 16rpx;
}
.meal-option {
  flex: 1;
  text-align: center;
  padding: 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 12rpx;
  font-size: 26rpx;
}
.meal-option.active {
  border-color: #07c160;
  background: rgba(7,193,96,0.06);
  color: #07c160;
}

/* 地址 */
.empty-hint {
  color: #999;
  font-size: 24rpx;
  text-align: center;
  padding: 24rpx;
}
.addr-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.addr-info {
  flex: 1;
}
.addr-name {
  font-size: 26rpx;
  color: #333;
}
.addr-detail {
  font-size: 22rpx;
  color: #999;
  display: block;
  margin-top: 4rpx;
}
.addr-check {
  font-size: 32rpx;
}

/* 底部 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  background: #fff;
  padding: 20rpx 24rpx;
  box-shadow: 0 -2rpx 12rpx rgba(0,0,0,0.06);
}
.total-label {
  font-size: 28rpx;
  color: #333;
}
.total-price {
  font-size: 36rpx;
  font-weight: bold;
  color: #e74c3c;
  flex: 1;
}
.submit-btn {
  background: #07c160;
  border-radius: 40rpx;
  padding: 18rpx 40rpx;
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
}
</style>
