<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409eff;">
              <el-icon><Reading /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalBooks }}</div>
              <div class="stat-label">图书总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67c23a;">
              <el-icon><FolderOpened /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalCategories }}</div>
              <div class="stat-label">分类总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #e6a23c;">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalBorrows }}</div>
              <div class="stat-label">借阅记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #f56c6c;">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalUsers }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近借阅</span>
            </div>
          </template>
          <el-table :data="recentBorrows" style="width: 100%">
            <el-table-column prop="bookTitle" label="图书名称" />
            <el-table-column prop="username" label="借阅人" />
            <el-table-column prop="borrowTime" label="借阅时间" />
            <el-table-column prop="returned" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.returned ? 'success' : 'warning'">
                  {{ scope.row.returned ? '已归还' : '未归还' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>热门图书</span>
            </div>
          </template>
          <el-table :data="popularBooks" style="width: 100%">
            <el-table-column prop="title" label="图书名称" />
            <el-table-column prop="author" label="作者" />
            <el-table-column prop="stock" label="库存" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { bookApi } from '@/api/book'
import { borrowApi } from '@/api/borrow'
import { categoryApi } from '@/api/category'
import { userApi } from '@/api/user'
import { Reading, FolderOpened, Document, User } from '@element-plus/icons-vue'

const stats = ref({
  totalBooks: 0,
  totalCategories: 0,
  totalBorrows: 0,
  totalUsers: 0
})

const recentBorrows = ref([])
const popularBooks = ref([])

const loadStats = async () => {
  try {
    const [booksRes, categoriesRes, borrowsRes, usersRes] = await Promise.all([
      bookApi.getAllBooks(),
      categoryApi.getAllCategories(),
      borrowApi.getAllBorrows(),
      userApi.getAllUsers()
    ])

    stats.value.totalBooks = booksRes.data?.length || 0
    stats.value.totalCategories = categoriesRes.data?.length || 0
    stats.value.totalBorrows = borrowsRes.data?.length || 0
    stats.value.totalUsers = usersRes.data?.length || 0

    // 最近借阅（取前5条）
    recentBorrows.value = (borrowsRes.data || []).slice(0, 5).map(item => ({
      ...item,
      borrowTime: item.borrowTime ? new Date(item.borrowTime).toLocaleString() : '-'
    }))

    // 热门图书（取前5本）
    popularBooks.value = (booksRes.data || []).slice(0, 5)
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24px;
  margin-right: 20px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.card-header {
  font-weight: 500;
  font-size: 16px;
}
</style>



