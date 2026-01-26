import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { adminService } from "@/services/admin"
import type { VendorTokenVO, VendorTokenCreateReqDto, VendorTokenUpdateReqDto } from "@/types/api"

interface VendorTokenManagerProps {
  vendorId: number
  vendorName: string
  initialTokens: VendorTokenVO[]
  onUpdate: () => void
}

export function VendorTokenManager({
  vendorId,
  vendorName,
  initialTokens,
  onUpdate,
}: VendorTokenManagerProps) {
  const [tokens, setTokens] = useState<VendorTokenVO[]>(initialTokens)
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [editingToken, setEditingToken] = useState<VendorTokenVO | null>(null)
  const [loading, setLoading] = useState(false)

  // 创建表单状态
  const [createForm, setCreateForm] = useState<VendorTokenCreateReqDto>({
    apiKey: "",
    tokenName: "",
    isActive: 1,
  })

  // 编辑表单状态
  const [editForm, setEditForm] = useState<VendorTokenUpdateReqDto>({
    apiKey: "",
    tokenName: "",
    isActive: 1,
  })

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!createForm.apiKey || !createForm.tokenName) {
      alert("请填写所有必填字段")
      return
    }

    setLoading(true)
    try {
      await adminService.createVendorToken(vendorId, createForm)
      setCreateForm({ apiKey: "", tokenName: "", isActive: 1 })
      setShowCreateForm(false)
      onUpdate()
    } catch (error) {
      console.error("创建 Token 失败:", error)
      alert("创建 Token 失败，请重试")
    } finally {
      setLoading(false)
    }
  }

  const handleEdit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!editingToken) return

    setLoading(true)
    try {
      await adminService.updateVendorToken(vendorId, editingToken.id, editForm)
      setEditingToken(null)
      onUpdate()
    } catch (error) {
      console.error("更新 Token 失败:", error)
      alert("更新 Token 失败，请重试")
    } finally {
      setLoading(false)
    }
  }

  const startEdit = (token: VendorTokenVO) => {
    setEditingToken(token)
    setEditForm({
      apiKey: token.apiKey,
      tokenName: token.tokenName,
      isActive: token.isActive ? 1 : 0,
      expiresAt: token.expiresAt,
    })
    setShowCreateForm(false)
  }

  const cancelEdit = () => {
    setEditingToken(null)
    setEditForm({ apiKey: "", tokenName: "", isActive: 1 })
  }

  const formatDate = (dateString?: string) => {
    if (!dateString) return "-"
    return new Date(dateString).toLocaleString("zh-CN")
  }

  useEffect(() => {
    setTokens(initialTokens)
  }, [initialTokens])

  return (
    <Card>
      <CardHeader>
        <CardTitle>管理 Token - {vendorName}</CardTitle>
        <CardDescription>创建和编辑供应商的 API Token</CardDescription>
      </CardHeader>
      <CardContent className="space-y-6">
        {/* Token 列表 */}
        <div>
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg font-semibold">Token 列表</h3>
            {!showCreateForm && !editingToken && (
              <Button size="sm" onClick={() => setShowCreateForm(true)}>
                创建新 Token
              </Button>
            )}
          </div>

          {tokens.length === 0 ? (
            <p className="text-muted-foreground text-sm">暂无 Token</p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Token 名称</TableHead>
                  <TableHead>API Key</TableHead>
                  <TableHead>状态</TableHead>
                  <TableHead>过期时间</TableHead>
                  <TableHead>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {tokens.map((token) => (
                  <TableRow key={token.id}>
                    <TableCell>{token.tokenName}</TableCell>
                    <TableCell className="font-mono text-xs">
                      {token.apiKey.substring(0, 20)}...
                    </TableCell>
                    <TableCell>
                      <span
                        className={token.isActive ? "text-green-600" : "text-gray-500"}
                      >
                        {token.isActive ? "启用" : "禁用"}
                      </span>
                    </TableCell>
                    <TableCell>{formatDate(token.expiresAt)}</TableCell>
                    <TableCell>
                      {editingToken?.id === token.id ? (
                        <Button variant="outline" size="sm" onClick={cancelEdit}>
                          取消
                        </Button>
                      ) : (
                        <Button variant="outline" size="sm" onClick={() => startEdit(token)}>
                          编辑
                        </Button>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </div>

        {/* 创建表单 */}
        {showCreateForm && (
          <Card>
            <CardHeader>
              <CardTitle>创建新 Token</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleCreate} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="create-tokenName">Token 名称 *</Label>
                  <Input
                    id="create-tokenName"
                    value={createForm.tokenName}
                    onChange={(e) =>
                      setCreateForm({ ...createForm, tokenName: e.target.value })
                    }
                    placeholder="例如: 生产环境 Token"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="create-apiKey">API Key *</Label>
                  <Input
                    id="create-apiKey"
                    value={createForm.apiKey}
                    onChange={(e) => setCreateForm({ ...createForm, apiKey: e.target.value })}
                    placeholder="请输入 API Key"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="create-expiresAt">过期时间（可选）</Label>
                  <Input
                    id="create-expiresAt"
                    type="datetime-local"
                    value={
                      createForm.expiresAt
                        ? new Date(createForm.expiresAt).toISOString().slice(0, 16)
                        : ""
                    }
                    onChange={(e) =>
                      setCreateForm({
                        ...createForm,
                        expiresAt: e.target.value ? new Date(e.target.value).toISOString() : undefined,
                      })
                    }
                  />
                </div>
                <div className="flex gap-2">
                  <Button type="submit" disabled={loading}>
                    {loading ? "创建中..." : "创建"}
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => {
                      setShowCreateForm(false)
                      setCreateForm({ apiKey: "", tokenName: "", isActive: 1 })
                    }}
                  >
                    取消
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}

        {/* 编辑表单 */}
        {editingToken && (
          <Card>
            <CardHeader>
              <CardTitle>编辑 Token</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleEdit} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="edit-tokenName">Token 名称 *</Label>
                  <Input
                    id="edit-tokenName"
                    value={editForm.tokenName}
                    onChange={(e) => setEditForm({ ...editForm, tokenName: e.target.value })}
                    placeholder="例如: 生产环境 Token"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="edit-apiKey">API Key *</Label>
                  <Input
                    id="edit-apiKey"
                    value={editForm.apiKey}
                    onChange={(e) => setEditForm({ ...editForm, apiKey: e.target.value })}
                    placeholder="请输入 API Key"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="edit-isActive">状态</Label>
                  <select
                    id="edit-isActive"
                    value={editForm.isActive}
                    onChange={(e) =>
                      setEditForm({ ...editForm, isActive: parseInt(e.target.value) })
                    }
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                  >
                    <option value={1}>启用</option>
                    <option value={0}>禁用</option>
                  </select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="edit-expiresAt">过期时间（可选）</Label>
                  <Input
                    id="edit-expiresAt"
                    type="datetime-local"
                    value={
                      editForm.expiresAt
                        ? new Date(editForm.expiresAt).toISOString().slice(0, 16)
                        : ""
                    }
                    onChange={(e) =>
                      setEditForm({
                        ...editForm,
                        expiresAt: e.target.value ? new Date(e.target.value).toISOString() : undefined,
                      })
                    }
                  />
                </div>
                <div className="flex gap-2">
                  <Button type="submit" disabled={loading}>
                    {loading ? "保存中..." : "保存"}
                  </Button>
                  <Button type="button" variant="outline" onClick={cancelEdit}>
                    取消
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}
      </CardContent>
    </Card>
  )
}
