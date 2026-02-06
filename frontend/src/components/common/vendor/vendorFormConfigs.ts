import type { VendorCreateReq } from "@/types/api"
import { DeepSeekVendorForm, createDeepSeekVendorPayload, type DeepSeekVendorFormValue } from "./DeepSeekVendorForm"
import { DouBaoVendorForm, createDouBaoVendorPayload, type DouBaoVendorFormValue } from "./DouBaoVendorForm"

export type VendorFormData = DeepSeekVendorFormValue | DouBaoVendorFormValue

export type VendorFormConfig = {
  venType: string
  FormComponent: React.ComponentType<{
    value: VendorFormData
    onChange: (value: VendorFormData) => void
    disabled?: boolean
  }>
  createPayload: (value: VendorFormData) => VendorCreateReq
  getInitialValue: (apiBaseUrl?: string) => VendorFormData
}

export const VENDOR_FORM_CONFIGS: Record<string, VendorFormConfig> = {
  DEEP_SEEK: {
    venType: "DEEP_SEEK",
    FormComponent: DeepSeekVendorForm,
    createPayload: createDeepSeekVendorPayload,
    getInitialValue: (apiBaseUrl) => ({
      name: "",
      apiBaseUrl: apiBaseUrl || "",
    }),
  },
  DOU_BAO: {
    venType: "DOU_BAO",
    FormComponent: DouBaoVendorForm,
    createPayload: createDouBaoVendorPayload,
    getInitialValue: (apiBaseUrl) => ({
      name: "",
      apiBaseUrl: apiBaseUrl || "",
    }),
  },
}

export function getVendorFormConfig(venType: string): VendorFormConfig | undefined {
  return VENDOR_FORM_CONFIGS[venType]
}

export function getSupportedVenTypes(): string[] {
  return Object.keys(VENDOR_FORM_CONFIGS)
}
