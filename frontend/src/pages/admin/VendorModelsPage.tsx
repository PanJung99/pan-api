import { useEffect, useState, useMemo } from "react"
import { AdminLayout } from "@/components/layout/AdminLayout"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { adminService } from "@/services/admin"
import type { VendorModelResp, VendorVO } from "@/types/api"

export function VendorModelsPage() {
  const [vendorModels, setVendorModels] = useState<VendorModelResp[]>([])
  const [vendors, setVendors] = useState<VendorVO[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [modelsResponse, vendorsResponse] = await Promise.all([
          adminService.getVendorModels(),
          adminService.getVendors(),
        ])
        setVendorModels(modelsResponse.result || [])
        setVendors(vendorsResponse.result || [])
      } catch (error) {
        console.error("获取服务商模型失败:", error)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  // 按服务商ID分组模型
  const modelsByVendor = useMemo(() => {
    const grouped = new Map<number, VendorModelResp[]>()
    vendorModels.forEach((model) => {
      const existing = grouped.get(model.vendorId) || []
      grouped.set(model.vendorId, [...existing, model])
    })
    return grouped
  }, [vendorModels])

  // 获取服务商名称
  const getVendorName = (vendorId: number) => {
    const vendor = vendors.find((v) => v.id === vendorId)
    return vendor?.name || `服务商 #${vendorId}`
  }

  if (loading) {
    return (
      <AdminLayout>
        <div className="space-y-6">
          <h1 className="text-3xl font-bold">服务商模型</h1>
          <div className="text-center py-12">加载中...</div>
        </div>
      </AdminLayout>
    )
  }

  if (vendorModels.length === 0) {
    return (
      <AdminLayout>
        <div className="space-y-6">
          <h1 className="text-3xl font-bold">服务商模型</h1>
          <p className="text-muted-foreground">暂无服务商模型</p>
        </div>
      </AdminLayout>
    )
  }

  return (
    <AdminLayout>
      <div className="space-y-6">
        <h1 className="text-3xl font-bold">服务商模型</h1>

        <div className="space-y-6">
          {Array.from(modelsByVendor.entries()).map(([vendorId, models]) => (
            <Card key={vendorId}>
              <CardHeader>
                <CardTitle>{getVendorName(vendorId)}</CardTitle>
                <CardDescription>
                  共 {models.length} 个模型
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {models.map((model) => (
                    <div
                      key={model.id}
                      className="p-4 border rounded-lg hover:bg-muted/50 transition-colors"
                    >
                      <div className="font-medium">{model.name}</div>
                      <div className="text-sm text-muted-foreground mt-1">
                        ID: {model.id}
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </AdminLayout>
  )
}
