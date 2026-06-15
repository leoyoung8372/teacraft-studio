<template>
  <view class="address-page">
    <!-- 地址列表 -->
    <view v-if="!editing" class="content">
      <view v-if="addresses.length === 0" class="empty"><text>暂无地址</text></view>
      <view v-for="addr in addresses" :key="addr.id" class="addr-card">
        <view class="addr-info">
          <text class="addr-name">{{ addr.receiverName }}  {{ addr.receiverPhone }}</text>
          <text class="addr-detail">{{ addr.detailAddress }}</text>
          <text class="addr-tag" v-if="addr.isDefault === 1">默认</text>
        </view>
        <view class="addr-actions">
          <text class="act-btn" @tap="startEdit(addr)">编辑</text>
          <text class="act-btn del" @tap="handleDelete(addr.id)">删除</text>
        </view>
      </view>
      <view class="add-btn" @tap="startAdd"><text>+ 新增地址</text></view>
    </view>

    <!-- 新增/编辑表单 -->
    <view v-else class="form">
      <view class="form-header">
        <text @tap="editing = false">取消</text>
        <text>{{ editId ? '编辑地址' : '新增地址' }}</text>
        <text></text>
      </view>
      <input v-model="form.receiverName" placeholder="收货人姓名" class="input" />
      <input v-model="form.receiverPhone" type="number" placeholder="手机号" maxlength="11" class="input" />
      <textarea v-model="form.detailAddress" placeholder="详细地址" class="input textarea" :rows="3" />
      <view class="default-row">
        <text>设为默认地址</text>
        <switch :checked="form.isDefault === 1" @change="form.isDefault = $event.detail.value ? 1 : 0" color="#07c160" />
      </view>
      <view class="save-btn" @tap="handleSave">
        <text>{{ editId ? '保存修改' : '添加地址' }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { list, save, remove } from '@/api/address'

const addresses = ref([])
const editing = ref(false)     // 是否在编辑状态
const editId = ref(null)       // null=新增，有值=编辑
const form = ref({ receiverName: '', receiverPhone: '', detailAddress: '', isDefault: 0 })

onMounted(() => { fetchAddresses() })

const fetchAddresses = async () => {
  const res = await list()
  addresses.value = res.data
}

const startAdd = () => {
  editId.value = null
  form.value = { receiverName: '', receiverPhone: '', detailAddress: '', isDefault: 0 }
  editing.value = true
}

const startEdit = (addr) => {
  editId.value = addr.id
  form.value = {
    receiverName: addr.receiverName,
    receiverPhone: addr.receiverPhone,
    detailAddress: addr.detailAddress,
    isDefault: addr.isDefault
  }
  editing.value = true
}

const handleSave = async () => {
  if (!form.value.receiverName || !form.value.receiverPhone || !form.value.detailAddress) {
    uni.showToast({ title: '请填写完整信息', icon: 'none' })
    return
  }
  try {
    await save({ id: editId.value, ...form.value })
    editing.value = false
    fetchAddresses()
  } catch (e) {}
}

const handleDelete = (id) => {
  uni.showModal({
    title: '提示',
    content: '确定删除该地址？',
    success: async (res) => {
      if (res.confirm) {
        await remove(id)
        fetchAddresses()
      }
    }
  })
}
</script>

<style scoped>
.address-page { min-height: 100vh; background: #f5f5f5; }
.content { padding: 20rpx 24rpx; }
.empty { text-align: center; padding: 80rpx 0; color: #999; font-size: 26rpx; }
.addr-card {
  background: #fff; border-radius: 12rpx; padding: 24rpx; margin-bottom: 16rpx;
}
.addr-name { font-size: 28rpx; color: #333; font-weight: bold; }
.addr-detail { font-size: 24rpx; color: #666; margin-top: 8rpx; display: block; }
.addr-tag {
  display: inline-block; background: rgba(7,193,96,0.1); color: #07c160;
  font-size: 20rpx; padding: 2rpx 12rpx; border-radius: 4rpx; margin-top: 6rpx;
}
.addr-actions { margin-top: 16rpx; border-top: 1rpx solid #f0f0f0; padding-top: 14rpx; }
.act-btn { font-size: 24rpx; color: #07c160; margin-right: 32rpx; }
.act-btn.del { color: #e74c3c; }
.add-btn {
  background: #fff; border-radius: 12rpx; text-align: center;
  padding: 28rpx; font-size: 28rpx; color: #07c160; margin-top: 16rpx;
}

/* 表单 */
.form { padding: 24rpx; }
.form-header {
  display: flex; justify-content: space-between;
  padding: 16rpx 0 32rpx; font-size: 30rpx; color: #333;
}
.input {
  background: #fff; border-radius: 12rpx; padding: 24rpx;
  margin-bottom: 20rpx; font-size: 28rpx;
}
.textarea { height: 120rpx; }
.default-row {
  display: flex; justify-content: space-between; align-items: center;
  background: #fff; border-radius: 12rpx; padding: 20rpx 24rpx; font-size: 28rpx;
}
.save-btn {
  background: #07c160; border-radius: 40rpx; text-align: center;
  padding: 24rpx; margin-top: 40rpx; color: #fff; font-size: 30rpx; font-weight: bold;
}
</style>
