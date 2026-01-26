import { useEffect, useState } from "react"
import { MainLayout } from "@/components/layout/MainLayout"
import { ModelCard } from "@/components/common/ModelCard"
import { modelService } from "@/services/model"
import type { DisplayModelVO } from "@/types/api"

export function HomePage() {
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
    <MainLayout>
      <div className="container mx-auto px-4 py-12">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold mb-4">Pan API - LLM API 聚合平台</h1>
          <p className="text-xl text-muted-foreground">
            统一的 LLM API 接口，支持多种模型，简单易用
          </p>
        </div>

        <div className="mb-12">
          <h2 className="text-2xl font-semibold mb-6">支持的模型</h2>
          {loading ? (
            <div className="text-center py-12">加载中...</div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {models.map((model) => (
                <ModelCard key={model.id} model={model} />
              ))}
            </div>
          )}
        </div>
      </div>
    </MainLayout>
  )
}
