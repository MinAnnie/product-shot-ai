import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductPhoto } from './product-photo.model';

@Injectable({ providedIn: 'root' })
export class ProductPhotoService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/businesses';

  findAll(businessId: string): Observable<ProductPhoto[]> {
    return this.http.get<ProductPhoto[]>(`${this.baseUrl}/${businessId}/photos`);
  }

  upload(businessId: string, file: File): Observable<ProductPhoto> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<ProductPhoto>(`${this.baseUrl}/${businessId}/photos`, formData);
  }
}
