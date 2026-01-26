import { useEffect, useState } from "react"
import { AdminLayout } from "@/components/layout/AdminLayout"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { adminService } from "@/services/admin"
import type { ModelResp, VendorModelResp } from "@/types/api"
import { ModelCreateDialog } from "@/components/common/model/ModelCreateDialog"

export function AdminModelsPage() {
  const [models, setModels] = useState<ModelResp[]>([])
  const [vendorModels, setVendorModels] = useState<VendorModelResp[]>([])
  const [loading, setLoading] = useState(true)
  const [createDialogOpen, setCreateDialogOpen] = useState(false)

  const fetchData = async () => {
    try {
      const [modelsResponse, vendorModelsResponse] = await Promise.all([
        adminService.getModels(),
        adminService.getVendorModels(),
      ])
      setModels(modelsResponse.result || [])
      setVendorModels(vendorModelsResponse.result || [])
    } catch (error) {
      console.error("获取模型列表失败:", error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  // 获取服务商模型名称
  const getVendorModelName = (venModelId: number) => {
    const vendorModel = vendorModels.find((vm) => vm.id === venModelId)
    return vendorModel?.name || `模型 #${venModelId}`
  }

  // 获取服务商名称
  const getVendorName = (venModelId: number) => {
    const vendorModel = vendorModels.find((vm) => vm.id === venModelId)
    if (!vendorModel) return ""
    // 这里需要从服务商列表获取名称，暂时返回 ID
    return `服务商 #${vendorModel.vendorId}`
  }

  const getCategoryLabel = (category: string) => {
    const map: Record<string, string> = {
      chat: "对话",
      image: "绘画",
      audio: "语音",
      video: "视频",
      embedding: "向量",
    }
    return map[category] || category
  }

  const getUnitLabel = (unit: string) => {
    const map: Record<string, string> = {
      mtokens: "千tokens",
      mchars: "千字符",
      times: "次",
      nums: "个",
      minutes: "分钟",
      seconds: "秒",
      none: "无",
    }
    return map[unit] || unit
  }

  if (loading) {
    return (
      <AdminLayout>
        <div className="space-y-6">
          <h1 className="text-3xl font-bold">模型管理</h1>
          <div className="text-center py-12">加载中...</div>
        </div>
      </AdminLayout>
    )
  }

  return (
    <AdminLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between gap-4">
          <h1 className="text-3xl font-bold">模型管理</h1>
          <Button onClick={() => setCreateDialogOpen(true)}>创建模型</Button>
        </div>

        {models.length === 0 ? (
          <p className="text-muted-foreground">暂无模型</p>
        ) : (
          <div className="space-y-6">
            {models.map((model) => (
              <Card key={model.id}>
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div>
                      <CardTitle>{model.displayName || model.name}</CardTitle>
                      <CardDescription>{model.description || "无描述"}</CardDescription>
                    </div>
                    <div className="flex items-center gap-2">
                      {model.isFree && (
                        <span className="px-2 py-1 text-xs bg-green-100 text-green-800 rounded">
                          免费
                        </span>
                      )}
                      <span
                        className={`px-2 py-1 text-xs rounded ${
                          model.isActive === 1
                            ? "bg-green-100 text-green-800"
                            : "bg-gray-100 text-gray-800"
                        }`}
                      >
                        {model.isActive === 1 ? "启用" : "禁用"}
                      </span>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                    <div>
                      <span className="text-muted-foreground">标识:</span> {model.name}
                    </div>
                    <div>
                      <span className="text-muted-foreground">类别:</span>{" "}
                      {getCategoryLabel(model.category)}
                    </div>
                    <div>
                      <span className="text-muted-foreground">绑定数量:</span>{" "}
                      {model.bindings.length}
                    </div>
                    <div>
                      <span className="text-muted-foreground">计费项数量:</span>{" "}
                      {model.pricingItems.length}
                    </div>
                  </div>

                  {model.pricingItems.length > 0 && (
                    <div>
                      <h4 className="text-sm font-medium mb-2">计费项</h4>
                      <div className="overflow-x-auto">
                        <Table>
                          <TableHeader>
                            <TableRow>
                              <TableHead>单位</TableHead>
                              <TableHead>输入单价</TableHead>
                              <TableHead>输出单价</TableHead>
                              <TableHead>状态</TableHead>
                            </TableRow>
                          </TableHeader>
                          <TableBody>
                            {model.pricingItems.map((item) => (
                              <TableRow key={item.id}>
                                <TableCell>{getUnitLabel(item.unit)}</TableCell>
                                <TableCell>¥{item.priceInput}</TableCell>
                                <TableCell>¥{item.priceOutput}</TableCell>
                                <TableCell>
                                  {item.isActive === 1 ? (
                                    <span className="text-green-600">启用</span>
                                  ) : (
                                    <span className="text-gray-400">禁用</span>
                                  )}
                                </TableCell>
                              </TableRow>
                            ))}
                          </TableBody>
                        </Table>
                      </div>
                    </div>
                  )}

                  {model.bindings.length > 0 && (
                    <div>
                      <h4 className="text-sm font-medium mb-2">服务商模型绑定</h4>
                      <div className="space-y-2">
                        {model.bindings.map((binding) => (
                          <div
                            key={binding.id}
                            className="flex items-center justify-between p-2 border rounded text-sm"
                          >
                            <div>
                              <span className="text-muted-foreground">服务商模型:</span>{" "}
                              {getVendorModelName(binding.venModelId)}
                            </div>
                            <div>
                              {binding.enabled === 1 ? (
                                <span className="text-green-600">已启用</span>
                              ) : (
                                <span className="text-gray-400">已禁用</span>
                              )}
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </CardContent>
              </Card>
            ))}
          </div>
        )}

        <ModelCreateDialog
          open={createDialogOpen}
          onOpenChange={setCreateDialogOpen}
          onCreated={fetchData}
        />
      </div>
    </AdminLayout>
  )
}
