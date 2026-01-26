import { useEffect, useState } from "react"
import { UserLayout } from "@/components/layout/UserLayout"
import { StatsCard } from "@/components/common/StatsCard"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { billingService } from "@/services/billing"
import { formatDateTime } from "@/utils/date"
import type { BillVO, BillStatsDto } from "@/types/api"

export function BillingPage() {
  const [bills, setBills] = useState<BillVO[]>([])
  const [stats, setStats] = useState<BillStatsDto | null>(null)
  const [loading, setLoading] = useState(true)
  const [pageNum, setPageNum] = useState(1)
  const [total, setTotal] = useState(0)
  const pageSize = 10

  useEffect(() => {
    fetchBills()
    fetchStats()
  }, [pageNum])

  const fetchBills = async () => {
    try {
      const response = await billingService.getBills({
        pageNum,
        pageSize,
      })
      setBills(response.result?.records || [])
      setTotal(response.result?.total || 0)
    } catch (error) {
      console.error("获取账单列表失败:", error)
    } finally {
      setLoading(false)
    }
  }

  const fetchStats = async () => {
    try {
      const response = await billingService.getBillStats()
      setStats(response.result)
    } catch (error) {
      console.error("获取账单统计失败:", error)
    }
  }

  return (
    <UserLayout>
      <div className="space-y-6">
        <h1 className="text-3xl font-bold">账单</h1>

        {stats && (
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            <StatsCard
              title="今日支出"
              value={`¥${stats.today.totalAmount.toFixed(2)}`}
              description={`${stats.today.transactionCount} 笔交易`}
            />
            <StatsCard
              title="本月支出"
              value={`¥${stats.month.totalAmount.toFixed(2)}`}
              description={`${stats.month.transactionCount} 笔交易`}
            />
          </div>
        )}

        <div>
          <h2 className="text-2xl font-semibold mb-4">账单记录</h2>
          {loading ? (
            <div className="text-center py-12">加载中...</div>
          ) : bills.length === 0 ? (
            <p className="text-muted-foreground">暂无账单记录</p>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>时间</TableHead>
                    <TableHead>类型</TableHead>
                    <TableHead>金额</TableHead>
                    <TableHead>描述</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {bills.map((bill) => (
                    <TableRow key={bill.id}>
                      <TableCell>{formatDateTime(bill.createTime)}</TableCell>
                      <TableCell>{bill.billType}</TableCell>
                      <TableCell>¥{bill.amount.toFixed(2)}</TableCell>
                      <TableCell>{bill.description}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
              <div className="mt-4 flex items-center justify-between">
                <div className="text-sm text-muted-foreground">
                  共 {total} 条记录
                </div>
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
      </div>
    </UserLayout>
  )
}
