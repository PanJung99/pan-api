import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { AdminLayout } from "@/components/layout/AdminLayout"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { VendorCreateButton } from "@/components/common/vendor/VendorCreateButton"
import { adminService } from "@/services/admin"
import type { VendorVO } from "@/types/api"
import { useVendorTypes, getVendorTypeLabel } from "@/hooks/useVendorTypes"

export function AdminVendorsPage() {
  const navigate = useNavigate()
  const [vendors, setVendors] = useState<VendorVO[]>([])
  const [loading, setLoading] = useState(true)
  const { typeOptions, loadTypes } = useVendorTypes()

  const fetchVendors = async () => {
    try {
      const response = await adminService.getVendors()
      setVendors(response.result || [])
    } catch (error) {
      console.error("获取服务商列表失败:", error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchVendors()
    // 页面加载时也加载类型列表，用于翻译显示
    void loadTypes()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const handleManageTokens = (vendorId: number) => {
    navigate(`/admin/vendors/${vendorId}/tokens`)
  }

  return (
    <AdminLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between gap-4">
          <h1 className="text-3xl font-bold">服务商管理</h1>
          <VendorCreateButton onCreated={fetchVendors} />
        </div>

        {loading ? (
          <div className="text-center py-12">加载中...</div>
        ) : vendors.length === 0 ? (
          <p className="text-muted-foreground">暂无服务商</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {vendors.map((vendor) => {
              const typeLabel = getVendorTypeLabel(vendor.venType, typeOptions)
              return (
                <Card key={vendor.id}>
                  <CardHeader>
                    <CardTitle>{vendor.name}</CardTitle>
                    <CardDescription>{vendor.apiBaseUrl}</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-2">
                      <div className="text-sm">
                        <span className="text-muted-foreground">类型:</span>{" "}
                        {typeLabel}（{vendor.venType}）
                      </div>
                      <div className="text-sm">
                        <span className="text-muted-foreground">Token 数量:</span>{" "}
                        {vendor.tokens?.length || 0}
                      </div>
                      <div className="mt-4">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleManageTokens(vendor.id)}
                        >
                          管理 Token
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              )
            })}
          </div>
        )}
      </div>
    </AdminLayout>
  )
}
