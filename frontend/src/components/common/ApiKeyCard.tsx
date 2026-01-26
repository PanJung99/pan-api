import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { formatDateTime } from "@/utils/date"
import type { ApiKeyResp } from "@/types/api"

interface ApiKeyCardProps {
  apiKey: ApiKeyResp
  onDelete: (id: number) => void
  onCopy?: (key: string) => void
}

export function ApiKeyCard({ apiKey, onDelete, onCopy }: ApiKeyCardProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{apiKey.keyName}</CardTitle>
        <CardDescription>创建时间: {formatDateTime(apiKey.createTime)}</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <div>
          <p className="text-sm text-muted-foreground mb-1">API Key</p>
          <div className="flex items-center gap-2">
            <code className="flex-1 px-3 py-2 bg-muted rounded text-sm font-mono break-all">
              {apiKey.apiKey}
            </code>
            {onCopy && (
              <Button size="sm" variant="outline" onClick={() => onCopy(apiKey.apiKey)}>
                复制
              </Button>
            )}
          </div>
        </div>
        <div>
          <p className="text-sm text-muted-foreground">限额: {apiKey.quota}</p>
        </div>
        <Button variant="destructive" size="sm" onClick={() => onDelete(apiKey.id)}>
          删除
        </Button>
      </CardContent>
    </Card>
  )
}
