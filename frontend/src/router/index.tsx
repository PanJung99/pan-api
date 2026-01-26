import { createBrowserRouter, Navigate } from "react-router-dom"
import { useEffect } from "react"
import { useAuthStore } from "@/store/auth"
import { HomePage } from "@/pages/landing/HomePage"
import { LoginPage } from "@/pages/landing/LoginPage"
import { RegisterPage } from "@/pages/landing/RegisterPage"
import { DashboardPage } from "@/pages/user/DashboardPage"
import { ApiKeysPage } from "@/pages/user/ApiKeysPage"
import { BillingPage } from "@/pages/user/BillingPage"
import { RechargePage } from "@/pages/user/RechargePage"
import { ModelsPage } from "@/pages/user/ModelsPage"
import { AdminDashboardPage } from "@/pages/admin/DashboardPage"
import { AdminOrdersPage } from "@/pages/admin/OrdersPage"
import { AdminModelsPage } from "@/pages/admin/ModelsPage"
import { AdminVendorsPage } from "@/pages/admin/VendorsPage"
import { VendorTokensPage } from "@/pages/admin/VendorTokensPage"
import { VendorModelsPage } from "@/pages/admin/VendorModelsPage"

// 路由守卫组件
function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isChecked, checkAuth } = useAuthStore()
  
  useEffect(() => {
    checkAuth()
  }, [])
  
  if (!isChecked) {
    return null // 等待认证检查完成
  }
  
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />
}

export const router = createBrowserRouter([
  {
    path: "/",
    element: <HomePage />,
  },
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/register",
    element: <RegisterPage />,
  },
  {
    path: "/user",
    element: (
      <ProtectedRoute>
        <Navigate to="/user/dashboard" replace />
      </ProtectedRoute>
    ),
  },
  {
    path: "/user/dashboard",
    element: (
      <ProtectedRoute>
        <DashboardPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/user/api-keys",
    element: (
      <ProtectedRoute>
        <ApiKeysPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/user/billing",
    element: (
      <ProtectedRoute>
        <BillingPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/user/recharge",
    element: (
      <ProtectedRoute>
        <RechargePage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/user/models",
    element: (
      <ProtectedRoute>
        <ModelsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin",
    element: (
      <ProtectedRoute>
        <Navigate to="/admin/dashboard" replace />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin/dashboard",
    element: (
      <ProtectedRoute>
        <AdminDashboardPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin/orders",
    element: (
      <ProtectedRoute>
        <AdminOrdersPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin/models",
    element: (
      <ProtectedRoute>
        <AdminModelsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin/vendors/models",
    element: (
      <ProtectedRoute>
        <VendorModelsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin/vendors/:vendorId/tokens",
    element: (
      <ProtectedRoute>
        <VendorTokensPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin/vendors",
    element: (
      <ProtectedRoute>
        <AdminVendorsPage />
      </ProtectedRoute>
    ),
  },
])
