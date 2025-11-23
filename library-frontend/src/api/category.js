import request from '@/utils/request'

export const categoryApi = {
  // 添加分类
  addCategory(data) {
    return request({
      url: '/categories',
      method: 'post',
      data
    })
  },

  // 获取所有分类
  getAllCategories() {
    return request({
      url: '/categories',
      method: 'get'
    })
  },

  // 根据ID获取分类
  getCategoryById(id) {
    return request({
      url: `/categories/${id}`,
      method: 'get'
    })
  },

  // 更新分类
  updateCategory(id, data) {
    return request({
      url: `/categories/${id}`,
      method: 'put',
      data
    })
  },

  // 删除分类
  deleteCategory(id) {
    return request({
      url: `/categories/${id}`,
      method: 'delete'
    })
  }
}



