import { useState } from "react"
import { adminService } from "@/services/admin"
import { VEN_TYPE_LABEL, parseVenType } from "@/types/vendor"

type BackendVenType = string | { type: string; description?: string }

export type VendorTypeOption = {
  code: string
  label: string
}

/**
 * 规范化后端返回的服务商类型列表
 * 支持 string[] 或 {type, description}[] 两种格式
 */
function normalizeVendorTypeOptions(list: BackendVenType[]): VendorTypeOption[] {
  return (list || [])
    .map((item) => {
      if (typeof item === "string") {
        const venType = parseVenType(item)
        return { code: item, label: venType ? VEN_TYPE_LABEL[venType] : item }
      }
      if (item && typeof item === "object") {
        const code = item.type
        const venType = parseVenType(code)
        // 优先使用后端 description，其次用前端 label，再次用 code
        const label = item.description || (venType ? VEN_TYPE_LABEL[venType] : code)
        return { code, label }
      }
      return null
    })
    .filter((x): x is VendorTypeOption => Boolean(x?.code))
}

/**
 * 获取服务商类型列表的 Hook
 * 自动处理加载状态和错误
 */
export function useVendorTypes() {
  const [typeOptions, setTypeOptions] = useState<VendorTypeOption[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)

  const loadTypes = async () => {
    if (loading) return
    setLoading(true)
    setError(null)
    try {
      const resp = await adminService.getVendorTypes()
      const rawList = (resp.result || []) as BackendVenType[]
      const options = normalizeVendorTypeOptions(rawList)
      setTypeOptions(options)
    } catch (e) {
      const err = e instanceof Error ? e : new Error("获取服务商类型失败")
      setError(err)
      console.error("获取服务商类型失败:", e)
    } finally {
      setLoading(false)
    }
  }

  return {
    typeOptions,
    loading,
    error,
    loadTypes,
  }
}

/**
 * 根据 code 获取服务商类型的显示标签
 * 如果提供了 typeOptions，优先从其中查找；否则使用前端默认映射
 */
export function getVendorTypeLabel(
  code: string,
  typeOptions?: VendorTypeOption[]
): string {
  if (typeOptions) {
    const found = typeOptions.find((opt) => opt.code === code)
    if (found) return found.label
  }
  const venType = parseVenType(code)
  return venType ? VEN_TYPE_LABEL[venType] : code
}
