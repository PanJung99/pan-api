import { useEffect, useState } from "react"
import { UserLayout } from "@/components/layout/UserLayout"
import { ModelCard } from "@/components/common/ModelCard"
import { modelService } from "@/services/model"
import type { DisplayModelVO } from "@/types/api"

export function ModelsPage() {
  const [models, setModels] = useState<DisplayModelVO[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchModels = async () => {
      try {
        const response = await modelService.getModels()
        setModels(response.result || [])
      } catch (error) {
        console.error("获取模型列表失败:", error)
      } finally {
        setLoading(false)
      }
    }
    fetchModels()
  }, [])

  return (
    <UserLayout>
      <div className="space-y-6">
        <h1 className="text-3xl font-bold">模型列表</h1>

        {loading ? (
          <div className="text-center py-12">加载中...</div>
        ) : models.length === 0 ? (
          <p className="text-muted-foreground">暂无模型</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {models.map((model) => (
              <ModelCard key={model.id} model={model} />
            ))}
          </div>
        )}
      </div>
    </UserLayout>
  )
}
