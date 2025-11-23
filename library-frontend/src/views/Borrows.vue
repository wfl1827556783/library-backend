<template>
  <div class="borrows-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>借阅管理</span>
          <el-button type="primary" @click="handleBorrow">借阅图书</el-button>
        </div>
      </template>

      <div class="filter-bar">
        <el-select
          v-model="filterStatus"
          placeholder="筛选状态"
          style="width: 200px; margin-right: 10px;"
          clearable
          @change="loadBorrows"
        >
          <el-option label="未归还" value="false" />
          <el-option label="已归还" value="true" />
        </el-select>
        <el-button @click="loadBorrows">刷新</el-button>
      </div>

      <el-table :data="borrows" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="bookTitle" label="图书名称" />
        <el-table-column prop="username" label="借阅人" width="120" />
        <el-table-column prop="borrowTime" label="借阅时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.borrowTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="returnTime" label="归还时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.returnTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="returned" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.returned ? 'success' : 'warning'">
              {{ scope.row.returned ? '已归还' : '未归还' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button
              v-if="!scope.row.returned"
              type="success"
              size="small"
              @click="handleReturn(scope.row)"
            >
              归还
            </el-button>
            <span v-else style="color: #909399;">已归还</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 借阅对话框 -->
    <el-dialog
      v-model="borrowDialogVisible"
      title="借阅图书"
      width="500px"
    >
      <el-form
        ref="borrowFormRef"
        :model="borrowForm"
        :rules="borrowRules"
        label-width="80px"
      >
        <el-form-item label="用户" prop="userId">
          <el-select v-model="borrowForm.userId" placeholder="选择用户" style="width: 100%">
            <el-option
              v-for="user in users"
              :key="user.id"
              :label="user.username"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="图书" prop="bookId">
          <el-select v-model="borrowForm.bookId" placeholder="选择图书" style="width: 100%">
            <el-option
              v-for="book in availableBooks"
              :key="book.id"
              :label="`${book.title} (库存: ${book.stock})`"
              :value="book.id"
              :disabled="book.stock <= 0"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="borrowDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBorrowSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { borrowApi } from '@/api/borrow'
import { bookApi } from '@/api/book'
import { userApi } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const borrows = ref([])
const users = ref([])
const availableBooks = ref([])
const loading = ref(false)
const filterStatus = ref('')
const borrowDialogVisible = ref(false)
const borrowFormRef = ref(null)

const borrowForm = reactive({
  userId: null,
  bookId: null
})

const borrowRules = {
  userId: [{ required: true, message: '请选择用户', trigger: 'change' }],
  bookId: [{ required: true, message: '请选择图书', trigger: 'change' }]
}

const loadBorrows = async () => {
  loading.value = true
  try {
    const res = await borrowApi.getAllBorrows()
    let data = res.data || []
    
    // 筛选状态
    if (filterStatus.value !== '') {
      const status = filterStatus.value === 'true'
      data = data.filter(item => item.returned === status)
    }
    
    borrows.value = data
  } catch (error) {
    ElMessage.error('加载借阅记录失败')
  } finally {
    loading.value = false
  }
}

const loadUsers = async () => {
  try {
    const res = await userApi.getAllUsers()
    users.value = res.data || []
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  }
}

const loadAvailableBooks = async () => {
  try {
    const res = await bookApi.getAllBooks()
    availableBooks.value = (res.data || []).filter(book => book.stock > 0)
  } catch (error) {
    ElMessage.error('加载图书列表失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}

const handleBorrow = () => {
  borrowForm.userId = null
  borrowForm.bookId = null
  loadAvailableBooks()
  borrowDialogVisible.value = true
}

const handleBorrowSubmit = async () => {
  if (!borrowFormRef.value) return
  
  await borrowFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await borrowApi.borrowBook(borrowForm)
        ElMessage.success('借阅成功')
        borrowDialogVisible.value = false
        loadBorrows()
        loadAvailableBooks()
      } catch (error) {
        ElMessage.error('借阅失败')
      }
    }
  })
}

const handleReturn = async (row) => {
  try {
    await ElMessageBox.confirm('确定要归还这本图书吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await borrowApi.returnBook(row.id)
    ElMessage.success('归还成功')
    loadBorrows()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('归还失败')
    }
  }
}

onMounted(() => {
  loadBorrows()
  loadUsers()
  loadAvailableBooks()
})
</script>

<style scoped>
.borrows-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
</style>



