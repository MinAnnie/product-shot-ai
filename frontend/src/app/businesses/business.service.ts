import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Business, CreateBusinessRequest } from './business.model';

@Injectable({ providedIn: 'root' })
export class BusinessService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/businesses';

  findAll(): Observable<Business[]> {
    return this.http.get<Business[]>(this.baseUrl);
  }

  create(request: CreateBusinessRequest): Observable<Business> {
    return this.http.post<Business>(this.baseUrl, request);
  }
}
