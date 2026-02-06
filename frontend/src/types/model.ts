export enum ModelCategory {
  CHAT = "chat",
  IMAGE = "image",
  AUDIO = "audio",
  VIDEO = "video",
  EMBEDDING = "embedding",
}

export const ALL_MODEL_CATEGORIES: ModelCategory[] = [
  ModelCategory.CHAT,
  ModelCategory.IMAGE,
  ModelCategory.AUDIO,
  ModelCategory.VIDEO,
  ModelCategory.EMBEDDING,
]

export const MODEL_CATEGORY_LABEL: Record<ModelCategory, string> = {
  [ModelCategory.CHAT]: "对话模型",
  [ModelCategory.IMAGE]: "图像模型",
  [ModelCategory.AUDIO]: "音频模型",
  [ModelCategory.VIDEO]: "视频模型",
  [ModelCategory.EMBEDDING]: "嵌入模型",
}

export function getModelCategoryLabel(category: string): string {
  return MODEL_CATEGORY_LABEL[category as ModelCategory] || category
}
