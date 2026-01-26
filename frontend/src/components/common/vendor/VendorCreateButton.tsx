import { useState } from "react"
import { Button } from "@/components/ui/button"
import { VendorCreateDialog } from "./VendorCreateDialog"

type Props = {
  onCreated?: () => void
}

/**
 * 创建服务商按钮组件
 * 只负责触发打开弹窗，所有逻辑由 VendorCreateDialog 承载
 */
export function VendorCreateButton({ onCreated }: Props) {
  const [open, setOpen] = useState(false)

  return (
    <>
      <Button onClick={() => setOpen(true)}>创建服务商</Button>
      <VendorCreateDialog open={open} onOpenChange={setOpen} onCreated={onCreated} />
    </>
  )
}
