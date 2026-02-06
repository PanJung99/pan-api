import apiClient from "@/utils/api"
import { formatDate } from "@/utils/date"
import axios from "axios"
import type {
  DashboardReqDto,
  ResponseDtoDashboardRespDto,
  ApiOrderQueryRequest,
  ResponseDtoIPageApiOrderVO,
  ResponseDtoListVendorVO,
  ResponseDtoListVenTypeEnum,
  ResponseDtoVendorCreate,
  VendorCreateReq,
  VendorTokenCreateReqDto,
  VendorTokenUpdateReqDto,
  ResponseDtoBoolean,
  ResponseDto,
  ResponseDtoListVendorModelResp,
  ResponseDtoListModelResp,
  ResponseDtoString,
  ModelCreateReq,
  ModelUpdateReq,
  ModelStatusUpdateReq,
  AdminUserReq,
  ResponseDtoIPageAdminUserResp,
} from "@/types/api"

export const adminService = {
  // 检查管理员权限
  checkAdmin: async (): Promise<boolean> => {
    try {
      // 直接使用 axios 调用，避免响应拦截器将 403 当作错误处理
      const response = await axios.get<ResponseDto<string>>("/be-admin/common/check", {
        withCredentials: true,
      })
      return response.data.code === 200
    } catch (error: any) {
      // 403 表示无权限，这是正常情况，返回 false
      if (error.response?.status === 403) {
        return false
      }
      // 其他错误也返回 false
      return false
    }
  },
  // 获取管理仪表盘数据
  getDashboard: async (data: DashboardReqDto): Promise<ResponseDtoDashboardRespDto> => {
    const params = new URLSearchParams()
    const date = data.date || formatDate(new Date(), "yyyy-MM-dd'T'HH:mm:ss")
    params.append("date", date)
    const response = await apiClient.get<ResponseDtoDashboardRespDto>(
      "/be-admin/common/dashboard",
      { params }
    )
    return response.data
  },

  // 获取订单列表
  getOrders: async (data: ApiOrderQueryRequest): Promise<ResponseDtoIPageApiOrderVO> => {
    const response = await apiClient.get<ResponseDtoIPageApiOrderVO>("/be-admin/orders", {
      params: data,
    })
    return response.data
  },

  // 获取供应商列表
  getVendors: async (): Promise<ResponseDtoListVendorVO> => {
    const response = await apiClient.get<ResponseDtoListVendorVO>("/be-admin/vendors")
    return response.data
  },

  // 获取供应商类型列表（用于创建供应商时展示）
  getVendorTypes: async (): Promise<ResponseDtoListVenTypeEnum> => {
    const response = await apiClient.get<ResponseDtoListVenTypeEnum>("/be-admin/vendors/types")
    return response.data
  },

  // 创建供应商
  createVendor: async (data: VendorCreateReq): Promise<ResponseDtoVendorCreate> => {
    const response = await apiClient.post<ResponseDtoVendorCreate>("/be-admin/vendors", data)
    return response.data
  },

  // 创建供应商 Token
  createVendorToken: async (
    vendorId: number,
    data: VendorTokenCreateReqDto
  ): Promise<ResponseDtoBoolean> => {
    const response = await apiClient.post<ResponseDtoBoolean>(
      `/be-admin/vendors/${vendorId}/tokens`,
      data
    )
    return response.data
  },

  // 更新供应商 Token
  updateVendorToken: async (
    vendorId: number,
    tokenId: number,
    data: VendorTokenUpdateReqDto
  ): Promise<ResponseDtoBoolean> => {
    const response = await apiClient.put<ResponseDtoBoolean>(
      `/be-admin/vendors/${vendorId}/tokens/${tokenId}`,
      data
    )
    return response.data
  },

  // 获取全部服务商的全部模型
  getVendorModels: async (): Promise<ResponseDtoListVendorModelResp> => {
    const response = await apiClient.get<ResponseDtoListVendorModelResp>(
      "/be-admin/vendors/models"
    )
    return response.data
  },

  // 获取模型列表（管理员端，包含绑定关系和价格信息）
  getModels: async (): Promise<ResponseDtoListModelResp> => {
    const response = await apiClient.get<ResponseDtoListModelResp>("/be-admin/models")
    return response.data
  },

  // 创建模型
  createModel: async (data: ModelCreateReq): Promise<ResponseDtoString> => {
    const response = await apiClient.post<ResponseDtoString>("/be-admin/models", data)
    return response.data
  },

  // 更新模型
  updateModel: async (id: number, data: ModelUpdateReq): Promise<ResponseDtoString> => {
    const response = await apiClient.put<ResponseDtoString>(`/be-admin/models/${id}`, data)
    return response.data
  },

  // 切换模型状态
  toggleModelStatus: async (id: number, data: ModelStatusUpdateReq): Promise<ResponseDtoBoolean> => {
    const response = await apiClient.patch<ResponseDtoBoolean>(`/be-admin/models/${id}/status`, data)
    return response.data
  },

  // 切换模型绑定状态
  toggleBindingStatus: async (id: number, data: ModelStatusUpdateReq): Promise<ResponseDtoBoolean> => {
    const response = await apiClient.patch<ResponseDtoBoolean>(`/be-admin/models/bindings/${id}/status`, data)
    return response.data
  },

  // 获取用户列表
  getUsers: async (data: AdminUserReq): Promise<ResponseDtoIPageAdminUserResp> => {
    const response = await apiClient.get<ResponseDtoIPageAdminUserResp>("/be-admin/users", {
      params: data,
    })
    return response.data
  },
}
