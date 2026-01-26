import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import { AdminLayout } from "@/components/layout/AdminLayout"
import { VendorTokenManager } from "@/components/common/VendorTokenManager"
import { adminService } from "@/services/admin"
import type { VendorVO } from "@/types/api"

export function VendorTokensPage() {
  const { vendorId } = useParams<{ vendorId: string }>()
  const navigate = useNavigate()
  const [vendor, setVendor] = useState<VendorVO | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchVendor = async () => {
      if (!vendorId) {
        navigate("/admin/vendors")
        return
      }

      try {
        const response = await adminService.getVendors()
        const foundVendor = response.result?.find((v) => v.id === Number.parseInt(vendorId))
        if (!foundVendor) {
          navigate("/admin/vendors")
          return
        }
        setVendor(foundVendor)
      } catch (error) {
        console.error("获取服务商信息失败:", error)
        navigate("/admin/vendors")
      } finally {
        setLoading(false)
      }
    }

    fetchVendor()
  }, [vendorId, navigate])

  const handleClose = () => {
    navigate("/admin/vendors")
  }

  const handleUpdate = async () => {
    if (!vendorId) return
    try {
      const response = await adminService.getVendors()
      const foundVendor = response.result?.find((v) => v.id === Number.parseInt(vendorId))
      if (foundVendor) {
        setVendor(foundVendor)
      }
    } catch (error) {
      console.error("刷新服务商信息失败:", error)
    }
  }

  if (loading) {
    return (
      <AdminLayout>
        <div className="text-center py-12">加载中...</div>
      </AdminLayout>
    )
  }

  if (!vendor) {
    return (
      <AdminLayout>
        <div className="text-center py-12">服务商不存在</div>
      </AdminLayout>
    )
  }

  return (
    <AdminLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold">Token 管理</h1>
          <button
            onClick={handleClose}
            className="text-sm text-muted-foreground hover:text-foreground"
          >
            ← 返回服务商列表
          </button>
        </div>

        <VendorTokenManager
          vendorId={vendor.id}
          vendorName={vendor.name}
          initialTokens={vendor.tokens || []}
          onUpdate={handleUpdate}
        />
      </div>
    </AdminLayout>
  )
}
