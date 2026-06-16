<template>
  <div>
    <h3>订单管理</h3>
    <el-table :data="orders" border stripe v-loading="loading" empty-text="暂无待处理订单">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column label="总价" width="100">
        <template #default="{ row }">￥{{ row.totalAmount.toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="方式" width="80">
        <template #default="{ row }">
          {{ row.mealType === 'self_pickup' ? '自提' : '外卖' }}
        </template>
      </el-table-column>
      <el-table-column label="下单时间" width="170">
        <template #default="{ row }">{{ row.createdAt?.substring(0, 16) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button type="success" size="small" @click="handleComplete(row.id)">完成</el-button>
          <el-button type="danger" size="small" @click="handleCancel(row.id)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listPending, complete, cancel } from '@/api/order'

const orders = ref([])
const loading = ref(false)

const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await listPending()
    orders.value = res.data
  } finally {
    loading.value = false
  }
}

const handleComplete = async (id) => {
  await complete(id)
  ElMessage.success('订单已完成')
  fetchOrders()
}

const handleCancel = (id) => {
  ElMessageBox.confirm('确定取消该订单？取消后库存将自动恢复。', '取消确认', {
    confirmButtonText: '确定取消',
    cancelButtonText: '再想想',
    type: 'warning'
  }).then(async () => {
    await cancel(id)
    ElMessage.success('订单已取消，库存已恢复')
    fetchOrders()
  }).catch(() => {})
}

onMounted(() => fetchOrders())
</script>
