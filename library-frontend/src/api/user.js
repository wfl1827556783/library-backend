import request from '@/utils/request'

export const userApi = {
  // 用户注册
  register(data) {
    return request({
      url: '/users/register',
      method: 'post',
      data
    })
  },

  // 用户登录
  login(data) {
    return request({
      url: '/users/login',
      method: 'post',
      data
    })
  },

  // 获取所有用户
  getAllUsers() {
    return request({
      url: '/users',
      method: 'get'
    })
  },

  // 根据ID获取用户
  getUserById(id) {
    return request({
      url: `/users/${id}`,
      method: 'get'
    })
  },

  // 更新用户
  updateUser(id, data) {
    return request({
      url: `/users/${id}`,
      method: 'put',
      data
    })
  },

  // 删除用户
  deleteUser(id) {
    return request({
      url: `/users/${id}`,
      method: 'delete'
    })
  },

  // 退出登录
  logout() {
    return request({
      url: '/users/logout',
      method: 'post'
    })
  }
}

