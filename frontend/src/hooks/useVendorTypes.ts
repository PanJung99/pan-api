import { useState } from "react"
import { adminService } from "@/services/admin"

type BackendVenType = string | { type: string; name?: string; apiBaseUrl?: string }

export type VendorTypeOption = {
  code: string
  label: string
  apiBaseUrl?: string
}

function normalizeVendorTypeOptions(list: BackendVenType[]): VendorTypeOption[] {
  return (list || [])
    .map((item) => {
      if (typeof item === "string") {
        return { code: item, label: item }
      }
      if (item && typeof item === "object") {
        const code = item.type
        const label = item.name || code
        const apiBaseUrl = item.apiBaseUrl
        return { code, label, apiBaseUrl }
      }
      return null
    })
    .filter((x): x is VendorTypeOption => Boolean(x?.code))
}

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

export function getVendorTypeLabel(
  code: string,
  typeOptions?: VendorTypeOption[]
): string {
  if (typeOptions) {
    const found = typeOptions.find((opt) => opt.code === code)
    if (found) return found.label
  }
  return code
}
