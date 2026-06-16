<template>
  <div>
    <h3>历史订单</h3>
    <el-table :data="orders" border stripe v-loading="loading" empty-text="暂无历史订单">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column label="总价" width="100">
        <template #default="{ row }">￥{{ row.totalAmount.toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="方式" width="80">
        <template #default="{ row }">
          {{ row.mealType === 'self_pickup' ? '自提' : '外卖' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'finished' ? 'success' : 'info'" size="small">
            {{ row.status === 'finished' ? '已完成' : '已取消' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" width="170">
        <template #default="{ row }">{{ row.createdAt?.substring(0, 16) }}</template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listHistory } from '@/api/order'

const orders = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await listHistory()
    orders.value = res.data
  } finally {
    loading.value = false
  }
})
</script>
