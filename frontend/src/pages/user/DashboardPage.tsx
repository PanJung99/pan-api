import { useEffect, useState } from "react"
import { UserLayout } from "@/components/layout/UserLayout"
import { StatsCard } from "@/components/common/StatsCard"
import { ApiKeyCard } from "@/components/common/ApiKeyCard"
import { userService } from "@/services/user"
import type { UserProfileResp } from "@/types/api"

export function DashboardPage() {
  const [profile, setProfile] = useState<UserProfileResp | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await userService.getProfile()
        setProfile(response.result)
      } catch (error) {
        console.error("获取用户信息失败:", error)
      } finally {
        setLoading(false)
      }
    }
    fetchProfile()
  }, [])

  const handleDeleteApiKey = async (id: number) => {
    if (!confirm("确定要删除这个 API Key 吗？")) return
    try {
      await userService.deleteApiKey(id)
      const response = await userService.getProfile()
      setProfile(response.result)
    } catch (error: any) {
      alert(error.message || "删除失败")
    }
  }

  const handleCopyApiKey = (key: string) => {
    navigator.clipboard.writeText(key)
    alert("已复制到剪贴板")
  }

  if (loading) {
    return (
      <UserLayout>
        <div className="text-center py-12">加载中...</div>
      </UserLayout>
    )
  }

  if (!profile) {
    return (
      <UserLayout>
        <div className="text-center py-12">加载失败</div>
      </UserLayout>
    )
  }

  return (
    <UserLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold mb-6">仪表盘</h1>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <StatsCard title="账户余额" value={`¥${profile.user.balance}`} />
          <StatsCard title="今日 Token" value={profile.todayTokens.toLocaleString()} />
          <StatsCard title="今日请求" value={profile.todayRequests} />
        </div>

        <div>
          <h2 className="text-2xl font-semibold mb-4">我的 API Keys</h2>
          {profile.apiKeys.length === 0 ? (
            <p className="text-muted-foreground">暂无 API Key，请前往 API Keys 页面创建</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {profile.apiKeys.map((apiKey) => (
                <ApiKeyCard
                  key={apiKey.id}
                  apiKey={apiKey}
                  onDelete={handleDeleteApiKey}
                  onCopy={handleCopyApiKey}
                />
              ))}
            </div>
          )}
        </div>
      </div>
    </UserLayout>
  )
}
