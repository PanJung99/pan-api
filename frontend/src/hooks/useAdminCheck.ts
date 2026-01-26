import { useEffect, useState } from "react"
import { adminService } from "@/services/admin"

export function useAdminCheck() {
  const [isAdmin, setIsAdmin] = useState<boolean>(false)
  const [loading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    const checkAdmin = async () => {
      try {
        const hasAdminAccess = await adminService.checkAdmin()
        setIsAdmin(hasAdminAccess)
      } catch (error) {
        console.error("检查管理员权限失败:", error)
        setIsAdmin(false)
      } finally {
        setLoading(false)
      }
    }

    checkAdmin()
  }, [])

  return { isAdmin, loading }
}
