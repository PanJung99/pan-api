// 前端自维护的服务商类型枚举，仅用于映射到表单/UI

export enum VenType {
  COMMON = "COMMON",
  DEEP_SEEK = "DEEP_SEEK",
  GLM = "GLM",
  DOU_BAO = "DOU_BAO",
  XUN_FEI = "XUN_FEI",
}

export const ALL_VEN_TYPES: VenType[] = [
  VenType.COMMON,
  VenType.DEEP_SEEK,
  VenType.GLM,
  VenType.DOU_BAO,
  VenType.XUN_FEI,
]

export const VEN_TYPE_LABEL: Record<VenType, string> = {
  [VenType.COMMON]: "通用类型",
  [VenType.DEEP_SEEK]: "DeepSeek（深度求索）",
  [VenType.GLM]: "智谱清言（GLM）",
  [VenType.DOU_BAO]: "字节跳动豆包(火山引擎)",
  [VenType.XUN_FEI]: "讯飞星火",
}

// 某些类型可以定义推荐的 base URL，表单可以选择性使用
export const VEN_TYPE_DEFAULT_BASE_URL: Partial<Record<VenType, string>> = {
  [VenType.DEEP_SEEK]: "https://api.deepseek.com/",
}

/** 将后端返回的字符串安全映射为前端 VenType（只负责表单映射） */
export function parseVenType(code: string | null | undefined): VenType | undefined {
  if (!code) return undefined
  if ((ALL_VEN_TYPES as string[]).includes(code)) {
    return code as VenType
  }
  return undefined
}

