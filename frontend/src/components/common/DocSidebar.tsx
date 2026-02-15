import { Link, useLocation } from "react-router-dom"
import { cn } from "@/utils/cn"
import { docsConfig } from "@/docs.config"

export function DocSidebar() {
  const location = useLocation()
  const currentPath = location.pathname

  const isActive = (path: string) => {
    return currentPath === `/admin/api-docs/${path}`
  }

  return (
    <aside className="w-64 border-r bg-slate-50 dark:bg-slate-900 h-full overflow-y-auto">
      <div className="p-4">
        <h2 className="text-sm font-semibold text-slate-500 dark:text-slate-400 mb-4">
          文档导航
        </h2>
        <nav className="space-y-6">
          {docsConfig.map((category) => (
            <div key={category.title}>
              <h3 className="text-xs font-medium text-slate-400 dark:text-slate-500 uppercase tracking-wider mb-2">
                {category.title}
              </h3>
              <ul className="space-y-1">
                {category.items.map((item) => (
                  <li key={item.id}>
                    <Link
                      to={`/admin/api-docs/${item.path}`}
                      className={cn(
                        "block px-3 py-2 text-sm rounded-md transition-colors",
                        isActive(item.path)
                          ? "bg-primary text-primary-foreground font-medium"
                          : "text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-800"
                      )}
                    >
                      {item.title}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </nav>
      </div>
    </aside>
  )
}
