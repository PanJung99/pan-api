import { create } from "zustand"

interface AuthState {
  isAuthenticated: boolean
  setToken: (token: string | null) => void
  checkAuth: () => void
  logout: () => Promise<void>
}

/**
 * Auth store - 使用Cookie-based认证
 * JWT token从responseBody.result获取，前端手动设置到usif cookie
 */
export const useAuthStore = create<AuthState>((set) => ({
  isAuthenticated: false,

  // 设置token到cookie
  setToken: (token: string | null) => {
    if (token) {
      // 设置cookie: usif=token，有效期7天
      const expiresDate = new Date()
      expiresDate.setTime(expiresDate.getTime() + 7 * 24 * 60 * 60 * 1000)
      document.cookie = `usif=${token}; path=/; expires=${expiresDate.toUTCString()}`
      set({ isAuthenticated: true })
    } else {
      // 清除cookie
      document.cookie = `usif=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;`
      set({ isAuthenticated: false })
    }
  },

  // 检查认证状态（检查usif cookie是否存在）
  checkAuth: () => {
    const hasCookie = document.cookie.includes("usif=")
    set({ isAuthenticated: hasCookie })
  },

  // 登出
  logout: async () => {
    // 清除cookie
    document.cookie = `usif=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;`
    set({ isAuthenticated: false })
  },
}))
