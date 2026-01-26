import { useEffect, useState } from "react"
import { UserLayout } from "@/components/layout/UserLayout"
import { RechargePlanCard } from "@/components/common/RechargePlanCard"
import { planService } from "@/services/plan"
import type { RechargePlanResp } from "@/types/api"

export function RechargePage() {
  const [plans, setPlans] = useState<RechargePlanResp[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchPlans = async () => {
      try {
        const response = await planService.getPlans()
        setPlans(response.result || [])
      } catch (error) {
        console.error("获取充值套餐失败:", error)
      } finally {
        setLoading(false)
      }
    }
    fetchPlans()
  }, [])

  const handleSelectPlan = (plan: RechargePlanResp) => {
    // TODO: 实现充值逻辑
    alert(`选择套餐: ${plan.name}，价格: ¥${plan.price}`)
  }

  return (
    <UserLayout>
      <div className="space-y-6">
        <h1 className="text-3xl font-bold">充值</h1>

        {loading ? (
          <div className="text-center py-12">加载中...</div>
        ) : plans.length === 0 ? (
          <p className="text-muted-foreground">暂无充值套餐</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {plans.map((plan) => (
              <RechargePlanCard key={plan.planId} plan={plan} onSelect={handleSelectPlan} />
            ))}
          </div>
        )}
      </div>
    </UserLayout>
  )
}
