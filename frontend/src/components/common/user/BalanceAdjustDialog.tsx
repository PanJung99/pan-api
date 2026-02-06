import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { adminService } from "@/services/admin"
import type { AdminBalanceAdjustReq } from "@/types/api"

type Props = {
  open: boolean
  onOpenChange: (open: boolean) => void
  userId: number
  username: string
  currentBalance: number
  onAdjusted?: () => void
}

export function BalanceAdjustDialog({
  open,
  onOpenChange,
  userId,
  username,
  currentBalance,
  onAdjusted,
}: Props) {
  const [submitting, setSubmitting] = useState(false)
  const [amount, setAmount] = useState("")
  const [reason, setReason] = useState("")

  const handleClose = () => {
    if (submitting) return
    onOpenChange(false)
  }

  const handleSubmit = async () => {
    const amountNum = Number.parseFloat(amount)
    if (Number.isNaN(amountNum) || amountNum === 0) {
      alert("请输入有效的调整金额")
      return
    }

    if (!reason.trim()) {
      alert("请输入调整原因")
      return
    }

    setSubmitting(true)
    try {
      const payload: AdminBalanceAdjustReq = {
        userId,
        amount: amountNum,
        reason: reason.trim(),
      }
      await adminService.adjustBalance(userId, payload)
      alert("余额调整成功")
      setAmount("")
      setReason("")
      onOpenChange(false)
      onAdjusted?.()
    } catch (error: any) {
      console.error("调整余额失败:", error)
      const errorMessage = error.response?.data?.desc || error.message || "调整余额失败，请稍后重试"
      alert(errorMessage)
    } finally {
      setSubmitting(false)
    }
  }

  if (!open) return null

  return (
    <div className="fixed inset-0 z-50">
      <div className="absolute inset-0 bg-black/40" onClick={handleClose} />
      <div className="absolute inset-0 flex items-center justify-center p-4">
        <div className="w-full max-w-md rounded-lg border bg-background shadow-lg">
          <div className="flex items-center justify-between border-b px-6 py-4">
            <h2 className="text-lg font-semibold">调整用户余额</h2>
            <Button variant="outline" size="sm" onClick={handleClose} disabled={submitting}>
              关闭
            </Button>
          </div>

          <div className="px-6 py-4 space-y-4">
            <div className="space-y-1">
              <p className="text-sm text-muted-foreground">
                用户：<span className="font-medium text-foreground">{username}</span>
              </p>
              <p className="text-sm text-muted-foreground">
                当前余额：<span className="font-medium text-foreground">¥{currentBalance.toFixed(2)}</span>
              </p>
            </div>

            <div className="space-y-2">
              <Label htmlFor="amount">调整金额（正数增加，负数减少）</Label>
              <Input
                id="amount"
                type="number"
                step="0.01"
                placeholder="例如：100 或 -50"
                value={amount}
                disabled={submitting}
                onChange={(e) => setAmount(e.target.value)}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="reason">调整原因</Label>
              <Input
                id="reason"
                placeholder="请输入调整原因"
                value={reason}
                disabled={submitting}
                onChange={(e) => setReason(e.target.value)}
              />
            </div>
          </div>

          <div className="flex items-center justify-end gap-2 border-t px-6 py-4">
            <Button variant="outline" onClick={handleClose} disabled={submitting}>
              取消
            </Button>
            <Button onClick={handleSubmit} disabled={submitting}>
              {submitting ? "处理中..." : "确认调整"}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}
