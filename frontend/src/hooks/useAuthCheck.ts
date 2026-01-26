import { useEffect } from "react"
import { useAuthStore } from "@/store/auth"

export function useAuthCheck() {
  const { isAuthenticated, checkAuth } = useAuthStore()

  useEffect(() => {
    checkAuth()
  }, [checkAuth])

  return { isAuthenticated }
}
