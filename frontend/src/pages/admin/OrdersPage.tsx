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
import { adminService } from "@/services/admin"
import { formatDateTime } from "@/utils/date"
import type { ApiOrderVO } from "@/types/api"

export function AdminOrdersPage() {
  const [orders, setOrders] = useState<ApiOrderVO[]>([])
  const [loading, setLoading] = useState(true)
  const [pageNum, setPageNum] = useState(1)
  const [total, setTotal] = useState(0)
  const pageSize = 20

  useEffect(() => {
    fetchOrders()
  }, [pageNum])

  const fetchOrders = async () => {
    try {
      const response = await adminService.getOrders({
        pageNum,
        pageSize,
      })
      setOrders(response.result?.records || [])
      setTotal(response.result?.total || 0)
    } catch (error) {
      console.error("获取订单列表失败:", error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <AdminLayout>
      <div className="space-y-6">
        <h1 className="text-3xl font-bold">订单管理</h1>

        {loading ? (
          <div className="text-center py-12">加载中...</div>
        ) : orders.length === 0 ? (
          <p className="text-muted-foreground">暂无订单</p>
        ) : (
          <>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>请求ID</TableHead>
                  <TableHead>用户ID</TableHead>
                  <TableHead>模型</TableHead>
                  <TableHead>Token数</TableHead>
                  <TableHead>金额</TableHead>
                  <TableHead>成本</TableHead>
                  <TableHead>调用时间</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {orders.map((order) => (
                  <TableRow key={order.reqId}>
                    <TableCell className="font-mono text-sm">{order.reqId}</TableCell>
                    <TableCell>{order.userId}</TableCell>
                    <TableCell>{order.modelName}</TableCell>
                    <TableCell>{(order.totalTokens ?? 0).toLocaleString()}</TableCell>
                    <TableCell>¥{(order.amount ?? 0).toFixed(4)}</TableCell>
                    <TableCell>¥{(order.cost ?? 0).toFixed(4)}</TableCell>
                    <TableCell>{formatDateTime(order.callTime)}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
            <div className="mt-4 flex items-center justify-between">
              <div className="text-sm text-muted-foreground">共 {total} 条记录</div>
              <div className="flex gap-2">
                <button
                  onClick={() => setPageNum((p) => Math.max(1, p - 1))}
                  disabled={pageNum === 1}
                  className="px-4 py-2 border rounded-md disabled:opacity-50"
                >
                  上一页
                </button>
                <button
                  onClick={() => setPageNum((p) => p + 1)}
                  disabled={pageNum * pageSize >= total}
                  className="px-4 py-2 border rounded-md disabled:opacity-50"
                >
                  下一页
                </button>
              </div>
            </div>
          </>
        )}
      </div>
    </AdminLayout>
  )
}
