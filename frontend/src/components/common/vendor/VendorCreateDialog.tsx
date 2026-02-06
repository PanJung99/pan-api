import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import { adminService } from "@/services/admin"
import type { VendorCreateReq } from "@/types/api"
import { useVendorTypes } from "@/hooks/useVendorTypes"
import { getVendorFormConfig, type VendorFormData } from "./vendorFormConfigs"

type Props = {
  open: boolean
  onOpenChange: (open: boolean) => void
  onCreated?: () => void
}

export function VendorCreateDialog({ open, onOpenChange, onCreated }: Props) {
  const { typeOptions, loading: loadingTypes, loadTypes } = useVendorTypes()
  const [submitting, setSubmitting] = useState(false)
  const [selectedType, setSelectedType] = useState<string | "">("")
  const [formData, setFormData] = useState<VendorFormData | null>(null)

  const formConfig = selectedType ? getVendorFormConfig(selectedType) : undefined

  // 打开弹窗时加载类型列表
  useEffect(() => {
    if (open) {
      void loadTypes()
      if (typeOptions.length > 0 && !selectedType) {
        setSelectedType(typeOptions[0]?.code || "")
      }
    } else {
      setFormData(null)
      setSelectedType("")
    }
  }, [open])

  // 当类型列表加载完成后，设置默认选中项
  useEffect(() => {
    if (open && typeOptions.length > 0 && !selectedType) {
      setSelectedType(typeOptions[0]?.code || "")
    }
  }, [open, typeOptions, selectedType])

  // 当选择类型时，初始化表单数据
  useEffect(() => {
    if (selectedType && formConfig) {
      const typeOption = typeOptions.find((t) => t.code === selectedType)
      const apiBaseUrl = typeOption?.apiBaseUrl
      setFormData(formConfig.getInitialValue(apiBaseUrl))
    } else {
      setFormData(null)
    }
  }, [selectedType, formConfig, typeOptions])

  const handleClose = () => {
    if (submitting) return
    onOpenChange(false)
  }

  const buildPayload = (): VendorCreateReq | null => {
    if (!selectedType || !formConfig || !formData) {
      alert("请选择服务商类型")
      return null
    }

    return formConfig.createPayload(formData)
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
    } catch (error: any) {
      console.error("创建服务商失败:", error)
      const errorMessage = error.response?.data?.desc || error.message || "创建服务商失败，请稍后重试"
      alert(errorMessage)
    } finally {
      setSubmitting(false)
    }
  }

  const renderForm = () => {
    if (!selectedType) {
      return <p className="text-sm text-muted-foreground">请先选择服务商类型。</p>
    }

    if (!formConfig) {
      return (
        <div className="p-4 border rounded bg-muted/50">
          <p className="text-sm text-muted-foreground">
            该服务商类型的创建表单暂未实现，请选择其他类型。
          </p>
        </div>
      )
    }

    if (!formData) {
      return <p className="text-sm text-muted-foreground">加载表单中...</p>
    }

    const FormComponent = formConfig.FormComponent
    return <FormComponent value={formData} onChange={setFormData} disabled={submitting} />
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
                value={selectedType}
                disabled={loadingTypes || submitting}
                onChange={(e) => setSelectedType(e.target.value)}
              >
                <option value="" disabled>
                  {loadingTypes ? "类型加载中..." : "请选择服务商类型"}
                </option>
                {typeOptions.map(({ code, label }) => (
                  <option key={code} value={code}>
                    {label}
                  </option>
                ))}
              </select>
            </div>

            {renderForm()}
          </div>

          <div className="flex items-center justify-end gap-2 border-t px-6 py-4">
            <Button variant="outline" onClick={handleClose} disabled={submitting}>
              取消
            </Button>
            <Button onClick={handleSubmit} disabled={submitting || loadingTypes || !formConfig}>
              {submitting ? "创建中..." : "创建"}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}
