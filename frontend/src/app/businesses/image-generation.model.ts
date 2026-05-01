export interface ImageGenerationResult {
  provider: string;
  model: string;
  prompt: string;
  images: GeneratedImage[];
}

export interface GeneratedImage {
  url: string;
  width?: number;
  height?: number;
  contentType?: string;
}
