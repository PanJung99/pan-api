import apiClient from "@/utils/api"
import type {
  ResponseDtoUserProfileResp,
  ResponseDtoListApiKeyResp,
  ResponseDtoVoid,
  ApiKeyCreateReq,
} from "@/types/api"

export const userService = {
  // 获取用户信息
  getProfile: async (): Promise<ResponseDtoUserProfileResp> => {
    const response = await apiClient.get<ResponseDtoUserProfileResp>("/be/user/profile")
    return response.data
  },

  // 获取 API Key 列表
  getApiKeys: async (): Promise<ResponseDtoListApiKeyResp> => {
    const response = await apiClient.get<ResponseDtoListApiKeyResp>("/be/keys")
    return response.data
  },

  // 创建 API Key
  createApiKey: async (data: ApiKeyCreateReq): Promise<ResponseDtoVoid> => {
    const response = await apiClient.post<ResponseDtoVoid>("/be/keys", data)
    return response.data
  },

  // 删除 API Key
  deleteApiKey: async (id: number): Promise<ResponseDtoVoid> => {
    const response = await apiClient.delete<ResponseDtoVoid>(`/be/keys/${id}`)
    return response.data
  },
}
