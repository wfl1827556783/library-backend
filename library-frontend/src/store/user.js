import { defineStore } from 'pinia'
import { userApi } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null,
    token: localStorage.getItem('token') || ''
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.userInfo?.role === 'admin'
  },

  actions: {
    async login(loginData) {
      try {
        const res = await userApi.login(loginData)
        if (res.code === 200 && res.data) {
          // 保存JWT token和用户信息
          this.token = res.data.token || ''
          this.userInfo = res.data.user || res.data
          localStorage.setItem('token', this.token)
          localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
          return { success: true }
        }
        return { success: false, message: res.message || '登录失败' }
      } catch (error) {
        return { success: false, message: error.message || '登录失败' }
      }
    },

    async register(registerData) {
      try {
        const res = await userApi.register(registerData)
        if (res.code === 200) {
          return { success: true, data: res.data }
        }
        return { success: false, message: res.message }
      } catch (error) {
        return { success: false, message: error.message }
      }
    },

    async logout() {
      try {
        // 调用后端退出接口（可选）
        await userApi.logout()
      } catch (error) {
        // 即使后端调用失败，也清除本地数据
        console.error('退出登录失败:', error)
      } finally {
        // 清除本地数据
        this.userInfo = null
        this.token = ''
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
      }
    },

    loadUserInfo() {
      const userInfoStr = localStorage.getItem('userInfo')
      if (userInfoStr) {
        this.userInfo = JSON.parse(userInfoStr)
      }
    }
  }
})

