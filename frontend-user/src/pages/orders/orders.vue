<template>
  <view class="orders-page">
    <view v-if="orders.length === 0" class="empty">
      <text class="empty-icon">📋</text>
      <text>暂无订单</text>
      <text class="empty-hint">去点餐页下单吧</text>
    </view>
    <view v-for="o in orders" :key="o.id" class="order-card" @tap="goDetail(o.id)">
      <view class="card-header">
        <text class="order-no">订单号：{{ o.orderNo }}</text>
        <text class="order-status" :class="'status-' + o.status">
          {{ STATUS_MAP[o.status] || o.status }}
        </text>
      </view>
      <view class="card-body">
        <text class="meal-type">{{ o.mealType === 'self_pickup' ? '🏪 自提' : '🛵 外卖' }}</text>
        <text class="order-time">{{ o.createdAt?.substring(0, 16) }}</text>
      </view>
      <view class="card-footer">
        <text class="order-amount">￥{{ o.totalAmount.toFixed(2) }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { list } from '@/api/order'

const STATUS_MAP = { making: '制作中', finished: '已完成', cancelled: '已取消' }
const orders = ref([])

const fetchOrders = async () => {
  try {
    const res = await list()
    orders.value = res.data
  } catch (e) {}
}

onMounted(() => fetchOrders())
onShow(() => fetchOrders()) // 切换 Tab 或返回时刷新

const goDetail = (id) => {
  uni.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` })
}
</script>

<style scoped>
.orders-page { padding: 20rpx 24rpx; min-height: 100vh; background: #f5f5f5; }
.empty { text-align: center; padding: 120rpx 0; }
.empty-icon { font-size: 80rpx; display: block; margin-bottom: 20rpx; }
.empty-hint { font-size: 24rpx; color: #999; display: block; margin-top: 8rpx; }
.order-card {
  background: #fff; border-radius: 12rpx; padding: 24rpx; margin-bottom: 16rpx;
}
.card-header { display: flex; justify-content: space-between; margin-bottom: 12rpx; }
.order-no { font-size: 24rpx; color: #999; }
.order-status { font-size: 24rpx; font-weight: bold; }
.status-making { color: #e6a23c; }
.status-finished { color: #07c160; }
.status-cancelled { color: #999; }
.card-body { font-size: 24rpx; color: #666; margin-bottom: 12rpx; }
.meal-type { margin-right: 24rpx; }
.order-amount { font-size: 32rpx; font-weight: bold; color: #e74c3c; }
</style>
