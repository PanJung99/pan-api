import { Link, useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { useAuthStore } from "@/store/auth"
import { useAuthCheck } from "@/hooks/useAuthCheck"
import { useAdminCheck } from "@/hooks/useAdminCheck"

interface MainLayoutProps {
  children: React.ReactNode
}

export function MainLayout({ children }: MainLayoutProps) {
  const navigate = useNavigate()
  const { logout } = useAuthStore()
  const { isAuthenticated } = useAuthCheck()
  const { isAdmin } = useAdminCheck()

  const handleLogout = () => {
    logout()
    navigate("/login")
  }

  return (
    <div className="min-h-screen flex flex-col">
      <header className="border-b">
        <div className="container mx-auto px-4 py-4 flex items-center justify-between">
          <Link to="/" className="text-xl font-bold">
            Pan API
          </Link>
          <nav className="flex items-center gap-4">
            {isAuthenticated ? (
              <>
                <Link to="/user/dashboard">
                  <Button variant="ghost">用户中心</Button>
                </Link>
                {isAdmin && (
                  <Link to="/admin/dashboard">
                    <Button variant="ghost">管理后台</Button>
                  </Link>
                )}
                <Button variant="outline" onClick={handleLogout}>
                  退出登录
                </Button>
              </>
            ) : (
              <>
                <Link to="/login">
                  <Button variant="ghost">登录</Button>
                </Link>
                <Link to="/register">
                  <Button>注册</Button>
                </Link>
              </>
            )}
          </nav>
        </div>
      </header>
      <main className="flex-1">{children}</main>
      <footer className="border-t py-4">
        <div className="container mx-auto px-4 text-center text-sm text-muted-foreground">
          © 2024 Pan API. All rights reserved.
        </div>
      </footer>
    </div>
  )
}
