import { format, parseISO } from "date-fns"
import { zhCN } from "date-fns/locale"

export function formatDate(date: string | Date, pattern: string = "yyyy-MM-dd HH:mm:ss"): string {
  try {
    const dateObj = typeof date === "string" ? parseISO(date) : date
    return format(dateObj, pattern, { locale: zhCN })
  } catch {
    return date.toString()
  }
}

export function formatDateShort(date: string | Date): string {
  return formatDate(date, "yyyy-MM-dd")
}

export function formatDateTime(date: string | Date): string {
  return formatDate(date, "yyyy-MM-dd HH:mm:ss")
}
