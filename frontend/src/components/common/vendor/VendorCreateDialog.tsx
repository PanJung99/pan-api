import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import { adminService } from "@/services/admin"
import type { VendorCreateReq } from "@/types/api"
import { VenType, parseVenType } from "@/types/vendor"
import { useVendorTypes } from "@/hooks/useVendorTypes"
import { DeepSeekVendorForm, createDeepSeekVendorPayload, type DeepSeekVendorFormValue } from "./DeepSeekVendorForm"

type Props = {
  open: boolean
  onOpenChange: (open: boolean) => void
  onCreated?: () => void
}

export function VendorCreateDialog({ open, onOpenChange, onCreated }: Props) {
  const { typeOptions, loading: loadingTypes, loadTypes } = useVendorTypes()
  const [submitting, setSubmitting] = useState(false)
  const [selectedBackendType, setSelectedBackendType] = useState<string | "">("")

  // DeepSeek 表单状态（只实现这一种，其他类型先打样）
  const [deepSeekForm, setDeepSeekForm] = useState<DeepSeekVendorFormValue>({
    name: "",
    apiBaseUrl: "",
  })

  const currentVenType: VenType | undefined = parseVenType(selectedBackendType)

  // 打开弹窗时加载类型列表
  useEffect(() => {
    if (open) {
      void loadTypes()
      // 设置默认选中项
      if (typeOptions.length > 0 && !selectedBackendType) {
        const preferred =
          typeOptions.find((t) => t.code === VenType.DEEP_SEEK)?.code ?? typeOptions[0]?.code ?? ""
        setSelectedBackendType(preferred)
      }
    } else {
      // 关闭时重置表单
      setDeepSeekForm({ name: "", apiBaseUrl: "" })
      setSelectedBackendType("")
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open])

  // 当类型列表加载完成后，设置默认选中项
  useEffect(() => {
    if (open && typeOptions.length > 0 && !selectedBackendType) {
      const preferred =
        typeOptions.find((t) => t.code === VenType.DEEP_SEEK)?.code ?? typeOptions[0]?.code ?? ""
      setSelectedBackendType(preferred)
    }
  }, [open, typeOptions, selectedBackendType])

  const handleClose = () => {
    if (submitting) return
    onOpenChange(false)
  }

  const buildPayload = (): VendorCreateReq | null => {
    if (!selectedBackendType) {
      alert("请选择服务商类型")
      return null
    }

    const venType = parseVenType(selectedBackendType)
    if (venType === VenType.DEEP_SEEK) {
      if (!deepSeekForm.name.trim() || !deepSeekForm.apiBaseUrl.trim()) {
        alert("请填写完整的 DeepSeek 服务商信息")
        return null
      }
      return createDeepSeekVendorPayload(deepSeekForm)
    }

    // 其他类型暂未实现，先给出提示
    alert("该类型的创建表单暂未实现，请选择 DeepSeek 测试流程")
    return null
  }

  const handleSubmit = async () => {
    const payload = buildPayload()
    if (!payload) return

    setSubmitting(true)
    try {
      await adminService.createVendor(payload)
      alert("创建成功")
      onOpenChange(false)
      onCreated?.()
    } catch (e) {
      console.error("创建服务商失败:", e)
      alert("创建服务商失败，请稍后重试")
    } finally {
      setSubmitting(false)
    }
  }

  const renderForm = () => {
    if (!selectedBackendType) {
      return <p className="text-sm text-muted-foreground">请先选择服务商类型。</p>
    }

    const venType = currentVenType
    if (venType === VenType.DEEP_SEEK) {
      return (
        <DeepSeekVendorForm
          value={deepSeekForm}
          onChange={setDeepSeekForm}
          disabled={submitting}
        />
      )
    }

    // 其他类型打样：先只展示占位说明
    return (
      <p className="text-sm text-muted-foreground">
        类型 {selectedBackendType} 的自定义表单暂未实现。当前仅支持 DeepSeek 作为示例。
      </p>
    )
  }

  if (!open) return null

  return (
    <div className="fixed inset-0 z-50">
      <div className="absolute inset-0 bg-black/40" onClick={handleClose} />
      <div className="absolute inset-0 flex items-center justify-center p-4">
        <div className="w-full max-w-lg rounded-lg border bg-background shadow-lg">
          <div className="flex items-center justify-between border-b px-6 py-4">
            <h2 className="text-lg font-semibold">创建服务商</h2>
            <Button variant="outline" size="sm" onClick={handleClose} disabled={submitting}>
              关闭
            </Button>
          </div>

          <div className="px-6 py-4 space-y-4">
            <div className="space-y-2">
              <label className="text-sm font-medium" htmlFor="vendor-type">
                服务商类型
              </label>
              <select
                id="vendor-type"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                value={selectedBackendType}
                disabled={loadingTypes || submitting}
                onChange={(e) => setSelectedBackendType(e.target.value)}
              >
                <option value="" disabled>
                  {loadingTypes ? "类型加载中..." : "请选择服务商类型"}
                </option>
                {typeOptions.map(({ code, label }) => {
                  return (
                    <option key={code} value={code}>
                      {label}
                    </option>
                  )
                })}
              </select>
            </div>

            {renderForm()}
          </div>

          <div className="flex items-center justify-end gap-2 border-t px-6 py-4">
            <Button variant="outline" onClick={handleClose} disabled={submitting}>
              取消
            </Button>
            <Button onClick={handleSubmit} disabled={submitting || loadingTypes}>
              {submitting ? "创建中..." : "创建"}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}
