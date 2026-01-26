import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import type { RechargePlanResp } from "@/types/api"

interface RechargePlanCardProps {
  plan: RechargePlanResp
  onSelect?: (plan: RechargePlanResp) => void
}

export function RechargePlanCard({ plan, onSelect }: RechargePlanCardProps) {
  return (
    <Card className={plan.isRecommend ? "border-primary" : ""}>
      <CardHeader>
        <CardTitle>{plan.name}</CardTitle>
        <CardDescription>{plan.desc}</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="text-3xl font-bold">¥{plan.price}</div>
        {plan.isRecommend && (
          <div className="mt-2 text-sm text-primary font-medium">推荐套餐</div>
        )}
      </CardContent>
      {onSelect && (
        <CardFooter>
          <Button className="w-full" onClick={() => onSelect(plan)}>
            选择套餐
          </Button>
        </CardFooter>
      )}
    </Card>
  )
}
