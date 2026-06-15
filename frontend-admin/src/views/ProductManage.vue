<template>
  <div class="product-manage">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索商品名称" clearable
        style="width:240px" @keyup.enter="fetchProducts" />
      <el-button type="primary" @click="openDialog(null)">新增商品</el-button>
    </div>

    <!-- 商品表格 -->
    <el-table :data="products" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="名称" />
      <el-table-column label="分类" width="120">
        <template #default="{ row }">
          {{ getCategoryName(row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="100" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="上架" width="70">
        <template #default="{ row }">
          <el-tag :type="row.isOnSale === 1 ? 'success' : 'info'" size="small">
            {{ row.isOnSale === 1 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      v-model:page-size="size"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      @current-change="fetchProducts"
      @size-change="fetchProducts"
      style="margin-top:16px;justify-content:flex-end"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="editId ? '编辑商品' : '新增商品'"
      width="520px" @closed="resetForm">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width:100%">
            <el-option v-for="cat in categories" :key="cat.id"
              :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="上架">
          <el-switch v-model="form.isOnSale" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

// ===== 列表数据 =====
const loading = ref(false)
const products = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const keyword = ref('')

// ===== 分类数据 =====
const categories = ref([])
const getCategoryName = (id) => {
  const cat = categories.value.find(c => c.id === id)
  return cat ? cat.name : ''
}

// ===== 弹窗数据 =====
const dialogVisible = ref(false)
const saving = ref(false)
const editId = ref(null) // null=新增，有值=编辑
const form = ref({ name: '', description: '', categoryId: null, price: 0, stock: 0, isOnSale: 1 })

// ===== 加载商品列表 =====
const fetchProducts = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/admin/products', {
      params: { keyword: keyword.value, page: page.value, size: size.value }
    })
    products.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// ===== 加载分类 =====
const fetchCategories = async () => {
  const res = await request.get('/api/categories')
  categories.value = res.data
}

// ===== 打开弹窗（新增/编辑） =====
const openDialog = (row) => {
  if (row) {
    editId.value = row.id
    form.value = {
      name: row.name,
      description: row.description || '',
      categoryId: row.categoryId,
      price: row.price,
      stock: row.stock,
      isOnSale: row.isOnSale
    }
  } else {
    editId.value = null
  }
  dialogVisible.value = true
}

// ===== 保存 =====
const handleSave = async () => {
  saving.value = true
  try {
    await request.post('/api/admin/products', {
      id: editId.value,         // null → 新增，有值 → 更新
      ...form.value
    })
    ElMessage.success(editId.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    fetchProducts()              // 刷新列表
  } finally {
    saving.value = false
  }
}

// ===== 删除 =====
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除「${row.name}」？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await request.delete(`/api/admin/products/${row.id}`)
    ElMessage.success('删除成功')
    fetchProducts()
  }).catch(() => {})
}

// ===== 重置表单 =====
const resetForm = () => {
  form.value = { name: '', description: '', categoryId: null, price: 0, stock: 0, isOnSale: 1 }
  editId.value = null
}

onMounted(() => {
  fetchCategories()
  fetchProducts()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}
</style>
