export interface DocItem {
  id: string
  title: string
  path: string
  children?: DocItem[]
}

export interface DocCategory {
  title: string
  items: DocItem[]
}

export const docsConfig: DocCategory[] = [
  {
    title: "快速开始",
    items: [
      {
        id: "introduction",
        title: "简介",
        path: "introduction",
      },
      {
        id: "quick-start",
        title: "快速开始",
        path: "quick-start",
      },
    ],
  },
  {
    title: "API 文档",
    items: [
      {
        id: "authentication",
        title: "认证方式",
        path: "authentication",
      },
    ],
  },
]

export const getDocPath = (docId: string): string => {
  for (const category of docsConfig) {
    for (const item of category.items) {
      if (item.id === docId) {
        return `/admin/api-docs/${item.path}`
      }
    }
  }
  return "/admin/api-docs"
}

export const getDocTitle = (docId: string): string => {
  for (const category of docsConfig) {
    for (const item of category.items) {
      if (item.id === docId) {
        return item.title
      }
    }
  }
  return "文档"
}
