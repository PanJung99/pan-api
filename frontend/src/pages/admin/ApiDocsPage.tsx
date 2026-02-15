import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import { DocLayout } from "@/components/layout/DocLayout"
import { DocSidebar } from "@/components/common/DocSidebar"
import { MarkdownRenderer } from "@/components/common/MarkdownRenderer"
import { docs, type DocKey } from "@/docs/api"

export function ApiDocsPage() {
  const { docId } = useParams<{ docId?: string }>()
  const [content, setContent] = useState("")
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const loadDoc = async () => {
      try {
        setLoading(true)
        setError(null)

        const key = docId || "introduction"
        
        if (!(key in docs)) {
          setError(`文档 "${key}" 不存在`)
          setContent("")
          return
        }

        setContent(docs[key as DocKey])
      } catch (err) {
        console.error("加载文档失败:", err)
        setError("加载文档失败")
        setContent("")
      } finally {
        setLoading(false)
      }
    }

    loadDoc()
  }, [docId])

  return (
    <DocLayout>
      <div className="flex h-[calc(100vh-4rem)]">
        <DocSidebar />
        <main className="flex-1 overflow-y-auto">
          <div className="max-w-4xl mx-auto px-8 py-6">
            {loading ? (
              <div className="text-center py-12">加载中...</div>
            ) : error ? (
              <div className="text-center py-12 text-red-500">{error}</div>
            ) : (
              <div className="bg-white dark:bg-slate-950 rounded-lg p-8">
                <MarkdownRenderer content={content} />
              </div>
            )}
          </div>
        </main>
      </div>
    </DocLayout>
  )
}
