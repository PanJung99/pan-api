import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { adminService } from "@/services/admin"
import type { ModelCreateReq, PricingItemCreateReq, VendorModelResp, VendorVO } from "@/types/api"
import { toast } from "sonner"

type Props = {
  open: boolean
  onOpenChange: (open: boolean) => void
  onCreated?: () => void
}

export function ModelCreateDialog({ open, onOpenChange, onCreated }: Props) {
  const [submitting, setSubmitting] = useState(false)
  const [vendorModels, setVendorModels] = useState<VendorModelResp[]>([])
  const [vendors, setVendors] = useState<VendorVO[]>([])
  const [loading, setLoading] = useState(false)

  const [formData, setFormData] = useState<ModelCreateReq>({
    name: "",
    displayName: "",
    isFree: false,
    category: "chat",
    platformType: "DEEP_SEEK",
    description: "",
    vendorModelIds: [],
    pricingItems: [
      {
        unit: "mtokens",
        priceInput: 0,
        priceOutput: 0,
      },
    ],
  })

  // 加载服务商模型列表和服务商列表
  useEffect(() => {
    if (open) {
      const fetchData = async () => {
        setLoading(true)
        try {
          const [vendorModelsResponse, vendorsResponse] = await Promise.all([
            adminService.getVendorModels(),
            adminService.getVendors(),
          ])
          setVendorModels(vendorModelsResponse.result || [])
          setVendors(vendorsResponse.result || [])
        } catch (error) {
          console.error("加载数据失败:", error)
          toast.error("加载数据失败")
        } finally {
          setLoading(false)
        }
      }
      fetchData()
    } else {
      // 关闭时重置表单
      setFormData({
        name: "",
        displayName: "",
        isFree: false,
        category: "chat",
        platformType: "DEEP_SEEK",
        description: "",
        vendorModelIds: [],
        pricingItems: [
          {
            unit: "mtokens",
            priceInput: 0,
            priceOutput: 0,
          },
        ],
      })
    }
  }, [open])

  const handleClose = () => {
    if (submitting) return
    onOpenChange(false)
  }

  const handleSubmit = async () => {
    // 验证必填字段
    if (!formData.name.trim() || !formData.displayName.trim()) {
      toast.error("请填写模型名称和展示名称")
      return
    }

    if (formData.vendorModelIds.length === 0) {
      toast.error("请至少选择一个服务商模型")
      return
    }

    if (!formData.isFree && formData.pricingItems.length === 0) {
      toast.error("非免费模型需要至少一个计费项")
      return
    }

    setSubmitting(true)
    try {
      await adminService.createModel(formData)
      toast.success("创建成功")
      onOpenChange(false)
      onCreated?.()
    } catch (error: any) {
      console.error("创建模型失败:", error)
      const errorMessage = error.response?.data?.desc || error.message || "创建模型失败，请稍后重试"
      toast.error(errorMessage)
    } finally {
      setSubmitting(false)
    }
  }

  const handleVendorModelToggle = (vendorModelId: number) => {
    setFormData((prev) => ({
      ...prev,
      vendorModelIds: prev.vendorModelIds.includes(vendorModelId)
        ? prev.vendorModelIds.filter((id) => id !== vendorModelId)
        : [...prev.vendorModelIds, vendorModelId],
    }))
  }

  const handlePricingItemChange = (
    index: number,
    field: keyof PricingItemCreateReq,
    value: string | number
  ) => {
    setFormData((prev) => ({
      ...prev,
      pricingItems: prev.pricingItems.map((item, i) =>
        i === index ? { ...item, [field]: value } : item
      ),
    }))
  }

  const addPricingItem = () => {
    setFormData((prev) => ({
      ...prev,
      pricingItems: [
        ...prev.pricingItems,
        {
          unit: "mtokens",
          priceInput: 0,
          priceOutput: 0,
        },
      ],
    }))
  }

  const removePricingItem = (index: number) => {
    setFormData((prev) => ({
      ...prev,
      pricingItems: prev.pricingItems.filter((_, i) => i !== index),
    }))
  }

  // 按服务商分组服务商模型
  const vendorModelsByVendor = vendors.map((vendor) => ({
    vendor,
    models: vendorModels.filter((vm) => vm.vendorId === vendor.id),
  }))

  if (!open) return null

  return (
    <div className="fixed inset-0 z-50">
      <div className="absolute inset-0 bg-black/40" onClick={handleClose} />
      <div className="absolute inset-0 flex items-center justify-center p-4 overflow-y-auto">
        <div className="w-full max-w-4xl rounded-lg border bg-background shadow-lg max-h-[90vh] overflow-y-auto">
          <div className="flex items-center justify-between border-b px-6 py-4 sticky top-0 bg-background">
            <h2 className="text-lg font-semibold">创建模型</h2>
            <Button variant="outline" size="sm" onClick={handleClose} disabled={submitting}>
              关闭
            </Button>
          </div>

          <div className="px-6 py-4 space-y-6">
            {/* 基本信息 */}
            <div className="space-y-4">
              <h3 className="text-md font-semibold">基本信息</h3>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="name">模型标识 *</Label>
                  <Input
                    id="name"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    placeholder="例如: deepseek-chat"
                    disabled={submitting}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="displayName">展示名称 *</Label>
                  <Input
                    id="displayName"
                    value={formData.displayName}
                    onChange={(e) => setFormData({ ...formData, displayName: e.target.value })}
                    placeholder="例如: DeepSeek Chat"
                    disabled={submitting}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="category">类别 *</Label>
                  <select
                    id="category"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                    value={formData.category}
                    onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                    disabled={submitting}
                  >
                    <option value="chat">对话</option>
                    <option value="image">绘画</option>
                    <option value="audio">语音</option>
                    <option value="video">视频</option>
                    <option value="embedding">向量</option>
                  </select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="platformType">平台类型 *</Label>
                  <select
                    id="platformType"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                    value={formData.platformType}
                    onChange={(e) => setFormData({ ...formData, platformType: e.target.value })}
                    disabled={submitting}
                  >
                    <option value="DEEP_SEEK">DEEP_SEEK</option>
                  </select>
                </div>
                <div className="space-y-2 col-span-2">
                  <Label htmlFor="description">描述</Label>
                  <textarea
                    id="description"
                    className="flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    placeholder="模型描述信息"
                    disabled={submitting}
                  />
                </div>
                <div className="space-y-2">
                  <Label>
                    <input
                      type="checkbox"
                      checked={formData.isFree}
                      onChange={(e) => setFormData({ ...formData, isFree: e.target.checked })}
                      disabled={submitting}
                      className="mr-2"
                    />
                    免费模型
                  </Label>
                </div>
              </div>
            </div>

            {/* 服务商模型绑定 */}
            <div className="space-y-4">
              <h3 className="text-md font-semibold">服务商模型绑定 *</h3>
              {loading ? (
                <p className="text-sm text-muted-foreground">加载中...</p>
              ) : vendorModelsByVendor.length === 0 ? (
                <p className="text-sm text-muted-foreground">暂无服务商模型</p>
              ) : (
                <div className="space-y-4 max-h-60 overflow-y-auto border rounded p-4">
                  {vendorModelsByVendor.map(({ vendor, models }) => (
                    <div key={vendor.id} className="space-y-2">
                      <div className="font-medium text-sm">{vendor.name}</div>
                      <div className="space-y-1 pl-4">
                        {models.map((vm) => (
                          <label key={vm.id} className="flex items-center space-x-2 text-sm">
                            <input
                              type="checkbox"
                              checked={formData.vendorModelIds.includes(vm.id)}
                              onChange={() => handleVendorModelToggle(vm.id)}
                              disabled={submitting}
                            />
                            <span>{vm.name}</span>
                          </label>
                        ))}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* 计费项 */}
            {!formData.isFree && (
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <h3 className="text-md font-semibold">计费项</h3>
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={addPricingItem}
                    disabled={submitting}
                  >
                    添加计费项
                  </Button>
                </div>
                <div className="space-y-4">
                  {formData.pricingItems.map((item, index) => (
                    <div key={index} className="border rounded p-4 space-y-4">
                      <div className="flex items-center justify-between">
                        <span className="text-sm font-medium">计费项 {index + 1}</span>
                        {formData.pricingItems.length > 1 && (
                          <Button
                            type="button"
                            variant="outline"
                            size="sm"
                            onClick={() => removePricingItem(index)}
                            disabled={submitting}
                          >
                            删除
                          </Button>
                        )}
                      </div>
                      <div className="grid grid-cols-3 gap-4">
                        <div className="space-y-2">
                          <Label>单位 *</Label>
                          <select
                            className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                            value={item.unit}
                            onChange={(e) =>
                              handlePricingItemChange(index, "unit", e.target.value)
                            }
                            disabled={submitting}
                          >
                            <option value="mtokens">百万tokens</option>
                            <option value="mchars">百万字符</option>
                            <option value="times">次</option>
                            <option value="nums">个</option>
                            <option value="minutes">分钟</option>
                            <option value="seconds">秒</option>
                            <option value="none">无</option>
                          </select>
                        </div>
                        <div className="space-y-2">
                          <Label>输入单价 *</Label>
                          <Input
                            type="number"
                            step="0.0001"
                            value={item.priceInput}
                            onChange={(e) =>
                              handlePricingItemChange(
                                index,
                                "priceInput",
                                parseFloat(e.target.value) || 0
                              )
                            }
                            disabled={submitting}
                          />
                        </div>
                        <div className="space-y-2">
                          <Label>输出单价 *</Label>
                          <Input
                            type="number"
                            step="0.0001"
                            value={item.priceOutput}
                            onChange={(e) =>
                              handlePricingItemChange(
                                index,
                                "priceOutput",
                                parseFloat(e.target.value) || 0
                              )
                            }
                            disabled={submitting}
                          />
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>

          <div className="flex items-center justify-end gap-2 border-t px-6 py-4 sticky bottom-0 bg-background">
            <Button variant="outline" onClick={handleClose} disabled={submitting}>
              取消
            </Button>
            <Button onClick={handleSubmit} disabled={submitting || loading}>
              {submitting ? "创建中..." : "创建"}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}
