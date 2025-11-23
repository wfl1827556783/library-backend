import request from '@/utils/request'

export const bookApi = {
  // 添加图书
  addBook(data) {
    return request({
      url: '/books',
      method: 'post',
      data
    })
  },

  // 获取所有图书
  getAllBooks(params) {
    return request({
      url: '/books',
      method: 'get',
      params
    })
  },

  // 根据ID获取图书
  getBookById(id) {
    return request({
      url: `/books/${id}`,
      method: 'get'
    })
  },

  // 更新图书
  updateBook(id, data) {
    return request({
      url: `/books/${id}`,
      method: 'put',
      data
    })
  },

  // 删除图书
  deleteBook(id) {
    return request({
      url: `/books/${id}`,
      method: 'delete'
    })
  }
}



