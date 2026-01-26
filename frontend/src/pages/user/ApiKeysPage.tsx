import { useEffect, useState } from "react"
import { UserLayout } from "@/components/layout/UserLayout"
import { ApiKeyCard } from "@/components/common/ApiKeyCard"
import { ApiKeyForm } from "@/components/common/ApiKeyForm"
import { userService } from "@/services/user"
import type { ApiKeyResp, ApiKeyCreateReq } from "@/types/api"

export function ApiKeysPage() {
  const [apiKeys, setApiKeys] = useState<ApiKeyResp[]>([])
  const [showForm, setShowForm] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchApiKeys()
  }, [])

  const fetchApiKeys = async () => {
    try {
      const response = await userService.getApiKeys()
      setApiKeys(response.result || [])
    } catch (error) {
      console.error("获取 API Key 列表失败:", error)
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = async (data: ApiKeyCreateReq) => {
    try {
      await userService.createApiKey(data)
      setShowForm(false)
      fetchApiKeys()
    } catch (error: any) {
      alert(error.message || "创建失败")
      throw error
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm("确定要删除这个 API Key 吗？")) return
    try {
      await userService.deleteApiKey(id)
      fetchApiKeys()
    } catch (error: any) {
      alert(error.message || "删除失败")
    }
  }

  const handleCopy = (key: string) => {
    navigator.clipboard.writeText(key)
    alert("已复制到剪贴板")
  }

  return (
    <UserLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold">API Keys</h1>
          {!showForm && (
            <button
              onClick={() => setShowForm(true)}
              className="px-4 py-2 bg-primary text-primary-foreground rounded-md hover:bg-primary/90"
            >
              创建 API Key
            </button>
          )}
        </div>

        {showForm && (
          <ApiKeyForm onSubmit={handleCreate} onCancel={() => setShowForm(false)} />
        )}

        {loading ? (
          <div className="text-center py-12">加载中...</div>
        ) : apiKeys.length === 0 ? (
          <p className="text-muted-foreground">暂无 API Key</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {apiKeys.map((apiKey) => (
              <ApiKeyCard
                key={apiKey.id}
                apiKey={apiKey}
                onDelete={handleDelete}
                onCopy={handleCopy}
              />
            ))}
          </div>
        )}
      </div>
    </UserLayout>
  )
}
