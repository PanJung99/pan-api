import apiClient from "@/utils/api"
import type { ResponseDtoListRechargePlanResp } from "@/types/api"

export const planService = {
  // 获取充值套餐列表
  getPlans: async (): Promise<ResponseDtoListRechargePlanResp> => {
    const response = await apiClient.get<ResponseDtoListRechargePlanResp>("/be/plans")
    return response.data
  },
}
