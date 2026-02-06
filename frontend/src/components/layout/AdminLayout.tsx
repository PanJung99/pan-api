import { Link, useLocation, useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { useAuthStore } from "@/store/auth"
import { cn } from "@/utils/cn"

interface AdminLayoutProps {
  children: React.ReactNode
}

const navItems = [
  { path: "/admin/dashboard", label: "仪表盘" },
  { path: "/admin/users", label: "用户管理" },
  { path: "/admin/orders", label: "订单管理" },
  { path: "/admin/models", label: "模型管理" },
  { path: "/admin/vendors", label: "服务商管理" },
  { path: "/admin/vendors/models", label: "服务商模型" },
]

export function AdminLayout({ children }: AdminLayoutProps) {
  const location = useLocation()
  const navigate = useNavigate()
  const { logout } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate("/login")
  }

  return (
    <div className="min-h-screen flex flex-col">
      <header className="border-b">
        <div className="container mx-auto px-4 py-4 flex items-center justify-between">
          <Link to="/" className="text-xl font-bold">
            Pan API - 管理后台
          </Link>
          <Button variant="outline" onClick={handleLogout}>
            退出登录
          </Button>
        </div>
      </header>
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
