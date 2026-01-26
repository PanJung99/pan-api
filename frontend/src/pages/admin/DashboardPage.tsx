import { useEffect, useState } from "react"
import { AdminLayout } from "@/components/layout/AdminLayout"
import { StatsCard } from "@/components/common/StatsCard"
import { adminService } from "@/services/admin"
import type { DashboardRespDto } from "@/types/api"

export function AdminDashboardPage() {
  const [dashboard, setDashboard] = useState<DashboardRespDto | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const response = await adminService.getDashboard({})
        setDashboard(response.result)
      } catch (error) {
        console.error("获取仪表盘数据失败:", error)
      } finally {
        setLoading(false)
      }
    }
    fetchDashboard()
  }, [])

  return (
    <AdminLayout>
      <div className="space-y-6">
        <h1 className="text-3xl font-bold">管理仪表盘</h1>

        {loading ? (
          <div className="text-center py-12">加载中...</div>
        ) : dashboard ? (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <StatsCard title="用户数" value={dashboard.userCount} />
            <StatsCard title="订单数" value={dashboard.orderCount} />
            <StatsCard title="营收" value={`¥${(dashboard.revenue ?? 0).toFixed(2)}`} />
          </div>
        ) : (
          <p className="text-muted-foreground">加载失败</p>
        )}
      </div>
    </AdminLayout>
  )
}
