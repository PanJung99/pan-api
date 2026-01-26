import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import type { VendorCreateReq } from "@/types/api"
import { VenType, VEN_TYPE_DEFAULT_BASE_URL } from "@/types/vendor"

export type DeepSeekVendorFormValue = {
  name: string
  apiBaseUrl: string
}

type Props = {
  value: DeepSeekVendorFormValue
  onChange: (value: DeepSeekVendorFormValue) => void
  disabled?: boolean
}

export function createDeepSeekVendorPayload(value: DeepSeekVendorFormValue): VendorCreateReq {
  return {
    name: value.name.trim(),
    apiBaseUrl: value.apiBaseUrl.trim() || VEN_TYPE_DEFAULT_BASE_URL[VenType.DEEP_SEEK] || "",
    venType: VenType.DEEP_SEEK,
  }
}

export function DeepSeekVendorForm({ value, onChange, disabled }: Props) {
  return (
    <div className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="vendor-name">服务商名称</Label>
        <Input
          id="vendor-name"
          placeholder="例如：DeepSeek-高可用渠道"
          value={value.name}
          disabled={disabled}
          onChange={(e) => onChange({ ...value, name: e.target.value })}
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="vendor-api-base">API Base URL</Label>
        <Input
          id="vendor-api-base"
          placeholder={VEN_TYPE_DEFAULT_BASE_URL[VenType.DEEP_SEEK] || "https://api.deepseek.com/"}
          value={value.apiBaseUrl}
          disabled={disabled}
          onChange={(e) => onChange({ ...value, apiBaseUrl: e.target.value })}
        />
      </div>
    </div>
  )
}

