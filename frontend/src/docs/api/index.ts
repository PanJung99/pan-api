import introduction from "./introduction.md?raw"
import quickStart from "./quick-start.md?raw"
import authentication from "./authentication.md?raw"

export const docs = {
  introduction,
  "quick-start": quickStart,
  authentication,
}

export type DocKey = keyof typeof docs
