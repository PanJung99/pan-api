import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import type { VendorCreateReq } from "@/types/api"

export type DouBaoVendorFormValue = {
  name: string
  apiBaseUrl: string
}

type Props = {
  value: DouBaoVendorFormValue
  onChange: (value: DouBaoVendorFormValue) => void
  disabled?: boolean
}

export function createDouBaoVendorPayload(value: DouBaoVendorFormValue): VendorCreateReq {
  return {
    name: value.name.trim(),
    apiBaseUrl: value.apiBaseUrl.trim(),
    venType: "DOU_BAO",
  }
}

export function DouBaoVendorForm({ value, onChange, disabled }: Props) {
  return (
    <div className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="vendor-name">服务商名称</Label>
        <Input
          id="vendor-name"
          placeholder="例如：豆包-高可用渠道"
          value={value.name}
          disabled={disabled}
          onChange={(e) => onChange({ ...value, name: e.target.value })}
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="vendor-api-base">API Base URL</Label>
        <Input
          id="vendor-api-base"
          placeholder="请输入 API Base URL"
          value={value.apiBaseUrl}
          disabled={disabled}
          onChange={(e) => onChange({ ...value, apiBaseUrl: e.target.value })}
        />
      </div>
    </div>
  )
}
