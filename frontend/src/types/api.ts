// API 响应通用结构
export interface ResponseDto<T = any> {
  code: number;
  desc: string;
  result: T;
}

// 响应类型别名（基于 OpenAPI 定义）
export type ResponseDtoString = ResponseDto<string>
export type ResponseDtoVoid = ResponseDto<void>
export type ResponseDtoBoolean = ResponseDto<boolean>
export type ResponseDtoUserProfileResp = ResponseDto<UserProfileResp>
export type ResponseDtoListApiKeyResp = ResponseDto<ApiKeyResp[]>
export type ResponseDtoListRechargePlanResp = ResponseDto<RechargePlanResp[]>
export type ResponseDtoListDisplayModelVO = ResponseDto<DisplayModelVO[]>
export type ResponseDtoListModelType = ResponseDto<ModelType[]>
export type ResponseDtoIPageBillVO = ResponseDto<IPage<BillVO>>
export type ResponseDtoBillStatsDto = ResponseDto<BillStatsDto>
export type ResponseDtoDashboardRespDto = ResponseDto<DashboardRespDto>
export type ResponseDtoIPageApiOrderVO = ResponseDto<IPage<ApiOrderVO>>
export type ResponseDtoListVendorVO = ResponseDto<VendorVO[]>
export type ResponseDtoListVenTypeEnum = ResponseDto<string[]>
export type ResponseDtoVendorCreate = ResponseDto<string>
export type ResponseDtoListVendorModelResp = ResponseDto<VendorModelResp[]>
export type ResponseDtoListModelResp = ResponseDto<ModelResp[]>

// 分页响应
export interface IPage<T> {
  size: number;
  current: number;
  total: number;
  pages?: number;
  records: T[];
}

// 用户相关
export interface UserResp {
  username: string;
  phone?: string;
  email: string;
  balance: number;
  createTime: string;
}

export interface ApiKeyResp {
  id: number;
  keyName: string;
  apiKey: string;
  quota: number;
  createTime: string;
}

export interface UserProfileResp {
  user: UserResp;
  apiKeys: ApiKeyResp[];
  todayTokens: number;
  todayRequests: number;
}

// 认证相关
export interface RegisterReq {
  username: string;
  password: string;
  email: string;
}

export interface LoginReq {
  identifier: string;
  password: string;
}

// API Key 相关
export interface ApiKeyCreateReq {
  keyName: string;
  quota: number;
  expireTime: string;
}

// 充值计划
export interface RechargePlanResp {
  planId: number;
  name: string;
  price: number;
  desc: string;
  displayMd?: string;
  isRecommend: number;
  sortOrder: number;
  status: number;
}

// 模型相关
export interface DisplayModelVO {
  id: number;
  name: string;
  displayName: string;
  isFree: boolean;
  unit: string;
  publicInputPrice: number;
  publicOutputPrice: number;
  publicUnifiedPrice: number;
  publicRequestPrice: number;
  category: string;
  description: string;
  modelTypeCode: string;
  modelTypeName: string;
  priceMultiplier: number;
  isActive: number;
}

export interface ModelType {
  id: number;
  code: string;
  name: string;
  description: string;
  iconUrl?: string;
  isDeleted: boolean;
  createdAt: string;
  updatedAt: string;
}

// 账单相关
export interface BillQueryReq {
  billType?: string;
  startTime?: string;
  endTime?: string;
  pageNum?: number;
  pageSize?: number;
}

export interface BillVO {
  id: number;
  billType: string;
  amount: number;
  relatedId: string;
  description: string;
  modelTagId?: number;
  modelTagName?: string;
  createTime: string;
}

export interface BillStats {
  totalAmount: number;
  transactionCount: number;
}

export interface BillStatsDto {
  today: BillStats;
  month: BillStats;
}

// 管理后台相关
export interface DashboardReqDto {
  date?: string;
}

export interface DashboardRespDto {
  userCount: number;
  orderCount: number;
  revenue: number;
}

export interface ApiOrderQueryRequest {
  pageNum?: number;
  pageSize?: number;
  startDate?: string;
  endDate?: string;
}

export interface ApiOrderVO {
  reqId: string;
  totalTokens: number;
  callTime: string;
  userId: number;
  amount: number;
  cost: number;
  modelName: string;
  apiType: string;
}

// 供应商相关
export interface VendorTokenVO {
  id: number;
  vendorId: number;
  apiKey: string;
  tokenName: string;
  isActive: boolean;
  expiresAt?: string;
}

export interface VendorVO {
  id: number;
  name: string;
  apiBaseUrl: string;
  venType: string;
  tokens: VendorTokenVO[];
}

// 创建供应商
export interface VendorCreateReq {
  name: string;
  apiBaseUrl: string;
  /** 后端约定的服务商类型编码，例如 DEEP_SEEK */
  venType: string;
}

export interface VendorTokenCreateReqDto {
  apiKey: string;
  tokenName: string;
  isActive?: number;
  expiresAt?: string;
}

export interface VendorTokenUpdateReqDto {
  apiKey?: string;
  tokenName?: string;
  isActive?: number;
  expiresAt?: string;
}

// 服务商模型相关
export interface VendorModelResp {
  id: number;
  vendorId: number;
  name: string;
}

// 模型管理相关（管理员端）
export interface BindingDetail {
  id: number;
  modelId: number;
  venModelId: number;
  enabled: number;
}

export interface PricingItemResp {
  id: number;
  unit: string;
  priceInput: number;
  priceOutput: number;
  currency?: string;
  isActive: number;
}

export interface ModelResp {
  id: number;
  name: string;
  displayName: string;
  isFree: boolean;
  category: string;
  description: string;
  isActive: number;
  bindings: BindingDetail[];
  pricingItems: PricingItemResp[];
}

// 创建模型请求
export interface PricingItemCreateReq {
  modelId?: number;
  type?: string;
  unit: string;
  priceInput: number;
  priceOutput: number;
}

export interface ModelCreateReq {
  name: string;
  displayName: string;
  isFree: boolean;
  category: string;
  platformType: string;
  description?: string;
  vendorModelIds: number[];
  pricingItems: PricingItemCreateReq[];
}

// 更新模型请求
export interface ModelUpdateReq {
  name: string;
  displayName: string;
  isFree: boolean;
  category: string;
  platformType: string;
  description?: string;
  vendorModelIds: number[];
  pricingItems: PricingItemCreateReq[];
}

// 更新模型状态请求
export interface ModelStatusUpdateReq {
  enabled: boolean;
}
