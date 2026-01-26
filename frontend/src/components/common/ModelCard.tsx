import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import type { DisplayModelVO } from "@/types/api"

interface ModelCardProps {
  model: DisplayModelVO
}

export function ModelCard({ model }: ModelCardProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{model.displayName || model.name}</CardTitle>
        <CardDescription>{model.description || model.category}</CardDescription>
      </CardHeader>
      <CardContent className="space-y-2">
        <div className="flex justify-between text-sm">
          <span className="text-muted-foreground">类型:</span>
          <span>{model.modelTypeName}</span>
        </div>
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
