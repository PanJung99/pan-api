import { Link, useLocation } from "react-router-dom"
import { Header } from "@/components/layout/Header"
import { cn } from "@/utils/cn"

interface UserLayoutProps {
  children: React.ReactNode
}

const navItems = [
  { path: "/user/dashboard", label: "仪表盘" },
  { path: "/user/api-keys", label: "API Keys" },
  { path: "/user/billing", label: "账单" },
  { path: "/user/recharge", label: "充值" },
  { path: "/user/models", label: "模型列表" },
]

export function UserLayout({ children }: UserLayoutProps) {
  const location = useLocation()

  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      <div className="flex flex-1">
        <aside className="w-64 border-r">
          <nav className="p-4 space-y-2">
            {navItems.map((item) => (
              <Link
                key={item.path}
                to={item.path}
                className={cn(
                  "block px-4 py-2 rounded-md text-sm transition-colors",
                  location.pathname === item.path
                    ? "bg-primary text-primary-foreground"
                    : "hover:bg-muted"
                )}
              >
                {item.label}
              </Link>
            ))}
          </nav>
        </aside>
        <main className="flex-1 p-6">{children}</main>
      </div>
    </div>
  )
}
