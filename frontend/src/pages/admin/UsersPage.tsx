import { useEffect, useState } from "react"
import { AdminLayout } from "@/components/layout/AdminLayout"
import { BalanceAdjustDialog } from "@/components/common/user/BalanceAdjustDialog"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Button } from "@/components/ui/button"
import { adminService } from "@/services/admin"
import { formatDateTime } from "@/utils/date"
import type { AdminUserResp } from "@/types/api"

export function AdminUsersPage() {
  const [users, setUsers] = useState<AdminUserResp[]>([])
  const [loading, setLoading] = useState(true)
  const [pageNum, setPageNum] = useState(1)
  const [total, setTotal] = useState(0)
  const pageSize = 20
  const [dialogOpen, setDialogOpen] = useState(false)
  const [selectedUser, setSelectedUser] = useState<AdminUserResp | null>(null)

  useEffect(() => {
    fetchUsers()
  }, [pageNum])

  const fetchUsers = async () => {
    try {
      const response = await adminService.getUsers({
        pageNum,
        pageSize,
      })
      setUsers(response.result?.records || [])
      setTotal(response.result?.total || 0)
    } catch (error) {
      console.error("获取用户列表失败:", error)
    } finally {
      setLoading(false)
    }
  }

  const handleAdjustBalance = (user: AdminUserResp) => {
    setSelectedUser(user)
    setDialogOpen(true)
  }

  const handleAdjusted = () => {
    fetchUsers()
  }

  return (
    <AdminLayout>
      <div className="space-y-6">
        <h1 className="text-3xl font-bold">用户管理</h1>

        {loading ? (
          <div className="text-center py-12">加载中...</div>
        ) : users.length === 0 ? (
          <p className="text-muted-foreground">暂无用户</p>
        ) : (
          <>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>ID</TableHead>
                  <TableHead>用户名</TableHead>
                  <TableHead>邮箱</TableHead>
                  <TableHead>手机号</TableHead>
                  <TableHead>余额</TableHead>
                  <TableHead>注册时间</TableHead>
                  <TableHead>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {users.map((user) => (
                  <TableRow key={user.id}>
                    <TableCell>{user.id}</TableCell>
                    <TableCell>{user.username}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.phone || "-"}</TableCell>
                    <TableCell>¥{(user.balance ?? 0).toFixed(2)}</TableCell>
                    <TableCell>{formatDateTime(user.createTime)}</TableCell>
                    <TableCell>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleAdjustBalance(user)}
                      >
                        调整余额
                      </Button>
                    </TableCell>
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

      {selectedUser && (
        <BalanceAdjustDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          userId={selectedUser.id}
          username={selectedUser.username}
          currentBalance={selectedUser.balance ?? 0}
          onAdjusted={handleAdjusted}
        />
      )}
    </AdminLayout>
  )
}
