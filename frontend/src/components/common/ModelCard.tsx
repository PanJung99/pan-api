import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import type { DisplayModelVO } from "@/types/api"
import { getModelCategoryLabel } from "@/types/model"
import { toast } from "sonner"

interface ModelCardProps {
  model: DisplayModelVO
}

export function ModelCard({ model }: ModelCardProps) {
  const handleCopy = () => {
    navigator.clipboard.writeText(model.name)
    toast.success("复制成功")
  }

  return (
    <Card>
      <CardHeader className="relative">
        <div className="absolute top-6 right-6">
          <Badge variant="secondary">{getModelCategoryLabel(model.category)}</Badge>
        </div>
        <CardTitle>{model.displayName}</CardTitle>
        <CardDescription className="flex items-center gap-2">
          <span className="font-mono text-xs">{model.name}</span>
          <button
            onClick={handleCopy}
            className="inline-flex items-center justify-center rounded hover:bg-accent p-1"
            title="复制模型标识"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="14"
              height="14"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <rect width="14" height="14" x="8" y="8" rx="2" ry="2" />
              <path d="M4 16c-1.1 0-2-.9-2-2V4c0-1.1.9-2 2-2h10c1.1 0 2 .9 2 2" />
            </svg>
          </button>
        </CardDescription>
        {model.description && (
          <p className="text-sm text-muted-foreground">{model.description}</p>
        )}
      </CardHeader>
      <CardContent className="space-y-2">
        {model.publicInputPrice > 0 && (
          <div className="flex justify-between text-sm">
            <span className="text-muted-foreground">输入价格:</span>
            <span>¥{model.publicInputPrice}/{model.unit}</span>
          </div>
        )}
        {model.publicOutputPrice > 0 && (
          <div className="flex justify-between text-sm">
            <span className="text-muted-foreground">输出价格:</span>
            <span>¥{model.publicOutputPrice}/{model.unit}</span>
          </div>
        )}
        {model.publicUnifiedPrice > 0 && (
          <div className="flex justify-between text-sm">
            <span className="text-muted-foreground">统一价格:</span>
            <span>¥{model.publicUnifiedPrice}/{model.unit}</span>
          </div>
        )}
        {model.isFree && (
          <div className="text-sm text-green-600 font-medium">免费模型</div>
        )}
      </CardContent>
    </Card>
  )
}
