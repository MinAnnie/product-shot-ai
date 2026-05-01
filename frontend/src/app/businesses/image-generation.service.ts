import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ImageGenerationResult } from './image-generation.model';

@Injectable({ providedIn: 'root' })
export class ImageGenerationService {
  private readonly http = inject(HttpClient);

  generate(photoId: string): Observable<ImageGenerationResult> {
    return this.http.post<ImageGenerationResult>(`/api/photos/${photoId}/generations`, {});
  }
}
