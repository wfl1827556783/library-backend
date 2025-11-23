import request from '@/utils/request'

export const borrowApi = {
  // 借阅图书
  borrowBook(data) {
    return request({
      url: '/borrows',
      method: 'post',
      data
    })
  },

  // 归还图书
  returnBook(id) {
    return request({
      url: `/borrows/${id}/return`,
      method: 'put'
    })
  },

  // 获取用户的所有借阅记录
  getBorrowsByUser(userId) {
    return request({
      url: `/borrows/user/${userId}`,
      method: 'get'
    })
  },

  // 根据ID获取借阅记录
  getBorrowById(id) {
    return request({
      url: `/borrows/${id}`,
      method: 'get'
    })
  },

  // 获取所有借阅记录
  getAllBorrows() {
    return request({
      url: '/borrows',
      method: 'get'
    })
  }
}



