<template>
  <view class="detail-page" v-if="order">
    <!-- 状态 -->
    <view class="status-bar" :class="'status-' + order.status">
      <text class="status-text">{{ STATUS_MAP[order.status] }}</text>
    </view>

    <!-- 订单信息 -->
    <view class="card">
      <text class="label">订单号：{{ order.orderNo }}</text>
      <text class="label">下单时间：{{ order.createdAt?.substring(0, 16) }}</text>
      <text class="label">用餐方式：{{ order.mealType === 'self_pickup' ? '到店自取' : '外卖配送' }}</text>
    </view>

    <!-- 商品清单 -->
    <view class="card">
      <text class="section-title">商品清单</text>
      <view v-for="item in items" :key="item.id" class="item-row">
        <image :src="item.productImageUrl || defaultImg" class="item-img" mode="aspectFill" />
        <view class="item-info">
          <text class="item-name">{{ item.productName }}</text>
          <text class="item-specs" v-if="item.sweetness || item.temperature || item.cupSize || item.addOns">
            {{ [item.sweetness, item.temperature, item.cupSize, item.addOns].filter(Boolean).join(' / ') }}
          </text>
        </view>
        <text class="item-qty">x{{ item.quantity }}</text>
        <text class="item-price">￥{{ item.subtotal.toFixed(2) }}</text>
      </view>
    </view>

    <!-- 合计 -->
    <view class="total-card">
      <text class="total-label">合计</text>
      <text class="total-price">￥{{ order.totalAmount.toFixed(2) }}</text>
    </view>

    <!-- 再来一单 -->
    <view class="reorder-btn" @tap="handleReorder">
      <text>{{ reordering ? '加入中...' : '再来一单' }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { detail } from '@/api/order'
import { add } from '@/api/cart'

const IMG_BASE = 'http://localhost:8080/images'
const defaultImg = `${IMG_BASE}/product/iceChocolate.jpeg`
const STATUS_MAP = { making: '制作中', finished: '已完成', cancelled: '已取消' }

const order = ref(null)
const items = ref([])
const reordering = ref(false)

// onLoad 接收页面路由参数，避免 getCurrentPages 兼容问题
onLoad(async (options) => {
  if (options.id) {
    const res = await detail(Number(options.id))
    order.value = res.data.order
    items.value = res.data.items
  }
})

// 再来一单：将订单商品逐个加入购物车
const handleReorder = async () => {
  reordering.value = true
  try {
    for (const item of items.value) {
      await add({
        productId: item.productId,
        quantity: item.quantity,
        sweetness: item.sweetness,
        temperature: item.temperature,
        cupSize: item.cupSize,
        addOns: item.addOns
      })
    }
    uni.showToast({ title: '已加入购物车', icon: 'success' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/order/order' }) // 跳点餐页
    }, 500)
  } catch (e) {
  } finally {
    reordering.value = false
  }
}
</script>

<style scoped>
.detail-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 40rpx; }
.status-bar { padding: 40rpx 24rpx; text-align: center; }
.status-making { background: rgba(230,162,60,0.1); }
.status-finished { background: rgba(7,193,96,0.1); }
.status-cancelled { background: rgba(153,153,153,0.1); }
.status-text { font-size: 36rpx; font-weight: bold; }
.status-making .status-text { color: #e6a23c; }
.status-finished .status-text { color: #07c160; }
.status-cancelled .status-text { color: #999; }

.card { background: #fff; border-radius: 12rpx; padding: 24rpx; margin: 20rpx 24rpx; }
.label { font-size: 26rpx; color: #666; display: block; margin-bottom: 8rpx; }
.section-title { font-size: 28rpx; font-weight: bold; color: #333; margin-bottom: 16rpx; display: block; }

.item-row { display: flex; align-items: center; margin-bottom: 16rpx; }
.item-img { width: 80rpx; height: 80rpx; border-radius: 8rpx; flex-shrink: 0; }
.item-info { flex: 1; margin-left: 16rpx; }
.item-name { font-size: 26rpx; color: #333; }
.item-specs { font-size: 22rpx; color: #999; display: block; }
.item-qty { font-size: 24rpx; color: #999; margin-right: 16rpx; }
.item-price { font-size: 28rpx; font-weight: bold; color: #333; }

.total-card {
  display: flex; justify-content: space-between;
  background: #fff; margin: 20rpx 24rpx; border-radius: 12rpx; padding: 24rpx;
}
.total-label { font-size: 28rpx; color: #333; }
.total-price { font-size: 36rpx; font-weight: bold; color: #e74c3c; }

.reorder-btn {
  background: #07c160; border-radius: 40rpx; text-align: center;
  padding: 24rpx; margin: 40rpx 48rpx; color: #fff;
  font-size: 30rpx; font-weight: bold;
}
</style>
