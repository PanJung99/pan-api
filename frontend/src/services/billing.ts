import apiClient from "@/utils/api"
import type {
  BillQueryReq,
  ResponseDtoIPageBillVO,
  ResponseDtoBillStatsDto,
} from "@/types/api"

export const billingService = {
  // 获取账单列表
  getBills: async (params: BillQueryReq): Promise<ResponseDtoIPageBillVO> => {
    const response = await apiClient.get<ResponseDtoIPageBillVO>("/be/billing", { params })
    return response.data
  },

  // 获取账单统计
  getBillStats: async (): Promise<ResponseDtoBillStatsDto> => {
    const response = await apiClient.get<ResponseDtoBillStatsDto>("/be/billing/stats")
    return response.data
  },
}
