import axios, { AxiosInstance, AxiosError } from "axios"
import type { ResponseDto } from "@/types/api"

// 创建 axios 实例
const apiClient: AxiosInstance = axios.create({
  baseURL: "",
  timeout: 30000,
  withCredentials: true, // 支持 Cookie
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config: any) => {
    return config
  },
  (error: any) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response: any) => {
    const data: ResponseDto = response.data
    if (data.code === 200) {
      return response
    } else {
      return Promise.reject(new Error(data.desc || "请求失败"))
    }
  },
  (error: AxiosError<ResponseDto>) => {
    // 获取错误信息
    const errorMessage = error.response?.data?.desc || error.message || "请求失败"
    
    if (error.response?.status === 401) {
      // 如果当前不在登录页，才跳转到登录页
      const isLoginPage = window.location.pathname === "/login"
      const isLoginRequest = error.config?.url?.includes("/auth/login")
      
      if (!isLoginPage && !isLoginRequest) {
        // 清除 cookie
        document.cookie = `usif=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;`
        window.location.href = "/login"
      }
    }
    
    return Promise.reject(new Error(errorMessage))
  }
)

export default apiClient
