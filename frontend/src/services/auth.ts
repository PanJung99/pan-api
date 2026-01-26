import apiClient from "@/utils/api"
import type { RegisterReq, LoginReq, ResponseDto, ResponseDtoString } from "@/types/api"

export const authService = {
  // 用户注册
  register: async (data: RegisterReq): Promise<ResponseDto<string>> => {
    const response = await apiClient.post<ResponseDto<string>>("/be/auth/register", data)
    return response.data
  },

  // 用户登录
  login: async (data: LoginReq): Promise<ResponseDtoString> => {
    const response = await apiClient.post<ResponseDtoString>("/be/auth/login", data)
    return response.data
  },
}
