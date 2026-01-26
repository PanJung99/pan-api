import { useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import type { ApiKeyCreateReq } from "@/types/api"

interface ApiKeyFormProps {
  onSubmit: (data: ApiKeyCreateReq) => Promise<void>
  onCancel?: () => void
}

export function ApiKeyForm({ onSubmit, onCancel }: ApiKeyFormProps) {
  const [keyName, setKeyName] = useState("")
  const [quota, setQuota] = useState("")
  const [expireTime, setExpireTime] = useState("")
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!keyName || !quota || !expireTime) {
      alert("请填写所有字段")
      return
    }

    setLoading(true)
    try {
      await onSubmit({
        keyName,
        quota: parseFloat(quota),
        expireTime: new Date(expireTime).toISOString(),
      })
      setKeyName("")
      setQuota("")
      setExpireTime("")
    } catch (error) {
      console.error("创建失败:", error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>创建 API Key</CardTitle>
        <CardDescription>创建一个新的 API Key 用于调用接口</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="keyName">Key 名称</Label>
            <Input
              id="keyName"
              value={keyName}
              onChange={(e) => setKeyName(e.target.value)}
              placeholder="例如: 生产环境 Key"
              required
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="quota">限额</Label>
            <Input
              id="quota"
              type="number"
              step="0.01"
              value={quota}
              onChange={(e) => setQuota(e.target.value)}
              placeholder="例如: 1000"
              required
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="expireTime">过期时间</Label>
            <Input
              id="expireTime"
              type="datetime-local"
              value={expireTime}
              onChange={(e) => setExpireTime(e.target.value)}
              required
            />
          </div>
          <div className="flex gap-2">
            <Button type="submit" disabled={loading}>
              {loading ? "创建中..." : "创建"}
            </Button>
            {onCancel && (
              <Button type="button" variant="outline" onClick={onCancel}>
                取消
              </Button>
            )}
          </div>
        </form>
      </CardContent>
    </Card>
  )
}
