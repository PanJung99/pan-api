import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { toast } from "sonner"

interface CodeExample {
  language: string
  title: string
  code: string
}

interface CodeTabsProps {
  examples: CodeExample[]
  baseUrl?: string
}

export function CodeTabs({ examples, baseUrl }: CodeTabsProps) {
  const [activeIndex, setActiveIndex] = useState(0)

  const handleCopy = (code: string) => {
    const processedCode = processCode(code)
    navigator.clipboard.writeText(processedCode)
    toast.success("复制成功")
  }

  const getBaseUrl = () => {
    if (baseUrl) return baseUrl
    return window.location.origin
  }

  const processCode = (code: string) => {
    return code.replace('{{baseUrl}}', getBaseUrl())
  }

  return (
    <Card>
      <CardHeader className="pb-3">
        <div className="flex justify-between">
          <div className="flex gap-2">
            {examples.map((example, index) => (
              <Button
                key={index}
                variant={activeIndex === index ? "default" : "outline"}
                size="sm"
                onClick={() => setActiveIndex(index)}
              >
                {example.language}
              </Button>
            ))}
          </div>
          <Button variant="ghost" size="sm" onClick={() => handleCopy(examples[activeIndex].code)}>
            复制
          </Button>
        </div>
      </CardHeader>
      <CardContent className="pt-0">
        <pre className="overflow-x-auto bg-muted p-4 rounded-md text-sm">
          <code>{processCode(examples[activeIndex].code)}</code>
        </pre>
      </CardContent>
    </Card>
  )
}
