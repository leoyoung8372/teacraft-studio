<template>
  <div>
    <h3>历史订单</h3>
    <el-table :data="orders" border stripe v-loading="loading" empty-text="暂无历史订单" @expand-change="handleExpand">
      <el-table-column type="expand">
        <template #default="{ row }">
          <div style="padding: 12px 20px">
            <el-table :data="row.items" border size="small" empty-text="暂无商品明细">
              <el-table-column label="商品图片" width="80">
                <template #default="{ row: item }">
                  <img
                    v-if="item.productImageUrl"
                    :src="item.productImageUrl"
                    style="width: 50px; height: 50px; object-fit: cover; border-radius: 6px"
                  />
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="productName" label="商品名称" />
              <el-table-column label="规格" min-width="180">
                <template #default="{ row: item }">
                  <span style="font-size: 12px; color: #666">
                    {{ [item.sweetness, item.temperature, item.cupSize, item.addOns].filter(Boolean).join(' / ') || '-' }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="price" label="单价" width="90">
                <template #default="{ row: item }">￥{{ item.price.toFixed(2) }}</template>
              </el-table-column>
              <el-table-column prop="quantity" label="数量" width="60" />
              <el-table-column label="小计" width="90">
                <template #default="{ row: item }">￥{{ item.subtotal.toFixed(2) }}</template>
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
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
import { ElMessage } from 'element-plus'
import { listHistory, detail } from '@/api/order'

const orders = ref([])
const loading = ref(false)

// 展开行时加载订单商品明细
const handleExpand = async (row, expanded) => {
  if (expanded && !row.items) {
    try {
      const res = await detail(row.id)
      row.items = res.data.items
    } catch {
      ElMessage.error('加载订单明细失败')
    }
  }
}

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
