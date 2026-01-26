import apiClient from "@/utils/api"
import type {
  ResponseDtoListDisplayModelVO,
  ResponseDtoListModelType,
} from "@/types/api"

export const modelService = {
  // 获取模型列表
  getModels: async (): Promise<ResponseDtoListDisplayModelVO> => {
    const response = await apiClient.get<ResponseDtoListDisplayModelVO>("/be/models")
    return response.data
  },

  // 获取模型类型列表
  getModelTypes: async (): Promise<ResponseDtoListModelType> => {
    const response = await apiClient.get<ResponseDtoListModelType>("/be/models/types")
    return response.data
  },
}
