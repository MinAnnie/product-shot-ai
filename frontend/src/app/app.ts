import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import {
  BRAND_STYLE_OPTIONS,
  BUSINESS_TYPE_OPTIONS,
  BrandStyle,
  Business,
  BusinessType,
  CreateBusinessRequest,
} from './businesses/business.model';
import { BusinessService } from './businesses/business.service';
import { ProductPhoto } from './businesses/product-photo.model';
import { ProductPhotoService } from './businesses/product-photo.service';
import { ImageGenerationResult } from './businesses/image-generation.model';
import { ImageGenerationService } from './businesses/image-generation.service';

type AppStep = 'business' | 'photos' | 'generate';

@Component({
  selector: 'app-root',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly businessService = inject(BusinessService);
  private readonly productPhotoService = inject(ProductPhotoService);
  private readonly imageGenerationService = inject(ImageGenerationService);

  protected readonly businessTypes = BUSINESS_TYPE_OPTIONS;
  protected readonly brandStyles = BRAND_STYLE_OPTIONS;
  protected readonly businesses = signal<Business[]>([]);
  protected readonly selectedBusiness = signal<Business | null>(null);
  protected readonly photos = signal<ProductPhoto[]>([]);
  protected readonly selectedPhoto = signal<ProductPhoto | null>(null);
  protected readonly step = signal<AppStep>('business');
  protected readonly loading = signal(false);
  protected readonly loadingPhotos = signal(false);
  protected readonly saving = signal(false);
  protected readonly uploading = signal(false);
  protected readonly generating = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly success = signal<string | null>(null);
  protected readonly selectedFile = signal<File | null>(null);
  protected readonly generationResult = signal<ImageGenerationResult | null>(null);
  protected readonly freeLimitReached = computed(() => this.businesses().length >= 1);

  protected readonly form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    businessType: ['CLOTHING' as BusinessType, [Validators.required]],
    description: ['', [Validators.maxLength(500)]],
    style: ['STREET' as BrandStyle, [Validators.required]],
    brandColors: [''],
    moods: [''],
    defaultChannel: ['Instagram'],
  });

  ngOnInit(): void {
    this.loadBusinesses();
  }

  protected loadBusinesses(): void {
    this.loading.set(true);
    this.error.set(null);

    this.businessService.findAll().subscribe({
      next: (businesses) => {
        this.businesses.set(businesses);
        if (!this.selectedBusiness() && businesses.length > 0) {
          this.chooseBusiness(businesses[0], false);
        }
        this.loading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.error.set(this.resolveErrorMessage(error, 'Could not load businesses.'));
        this.loading.set(false);
      },
    });
  }

  protected createBusiness(): void {
    if (this.form.invalid || this.saving()) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.error.set(null);
    this.success.set(null);

    const raw = this.form.getRawValue();
    const request: CreateBusinessRequest = {
      name: raw.name.trim(),
      businessType: raw.businessType,
      description: raw.description.trim() || undefined,
      style: raw.style,
      brandColors: this.parseCsv(raw.brandColors),
      moods: this.parseCsv(raw.moods),
      defaultChannel: raw.defaultChannel.trim() || undefined,
    };

    this.businessService.create(request).subscribe({
      next: (business) => {
        this.businesses.update((current) => [business, ...current]);
        this.form.reset({
          name: '',
          businessType: 'CLOTHING',
          description: '',
          style: 'STREET',
          brandColors: '',
          moods: '',
          defaultChannel: 'Instagram',
        });
        this.chooseBusiness(business);
        this.success.set('Business profile created. Next, upload a product photo.');
        this.saving.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.error.set(this.resolveErrorMessage(error, 'Could not create business.'));
        this.saving.set(false);
      },
    });
  }

  protected chooseBusiness(business: Business, goToPhotos = true): void {
    this.selectedBusiness.set(business);
    this.selectedPhoto.set(null);
    this.loadPhotos(business.id);
    if (goToPhotos) {
      this.step.set('photos');
    }
  }

  protected loadPhotos(businessId = this.selectedBusiness()?.id): void {
    if (!businessId) {
      return;
    }
    this.loadingPhotos.set(true);
    this.productPhotoService.findAll(businessId).subscribe({
      next: (photos) => {
        this.photos.set(photos);
        this.selectedPhoto.set(photos[0] ?? null);
        this.loadingPhotos.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.error.set(this.resolveErrorMessage(error, 'Could not load product photos.'));
        this.loadingPhotos.set(false);
      },
    });
  }

  protected onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile.set(input.files?.[0] ?? null);
  }

  protected uploadPhoto(): void {
    const business = this.selectedBusiness();
    const file = this.selectedFile();
    if (!business || !file || this.uploading()) {
      return;
    }

    this.uploading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.productPhotoService.upload(business.id, file).subscribe({
      next: (photo) => {
        this.photos.update((current) => [photo, ...current]);
        this.selectedPhoto.set(photo);
        this.selectedFile.set(null);
        this.success.set('Product photo uploaded. Next, generate the AI background.');
        this.uploading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.error.set(this.resolveErrorMessage(error, 'Could not upload product photo.'));
        this.uploading.set(false);
      },
    });
  }

  protected goTo(step: AppStep): void {
    this.step.set(step);
  }

  protected selectPhoto(photo: ProductPhoto): void {
    this.selectedPhoto.set(photo);
    this.generationResult.set(null);
  }

  protected generateImage(): void {
    const photo = this.selectedPhoto();
    if (!photo || this.generating()) {
      return;
    }

    this.generating.set(true);
    this.error.set(null);
    this.success.set(null);

    this.imageGenerationService.generate(photo.id).subscribe({
      next: (result) => {
        this.generationResult.set(result);
        this.success.set('AI background generated.');
        this.generating.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.error.set(this.resolveErrorMessage(error, 'Could not generate AI background.'));
        this.generating.set(false);
      },
    });
  }

  protected optionLabel<T extends string>(value: T, options: { value: T; label: string }[]): string {
    return options.find((option) => option.value === value)?.label ?? value;
  }

  protected formatFileSize(bytes: number): string {
    return `${(bytes / 1024 / 1024).toFixed(2)} MB`;
  }

  private parseCsv(value: string): string[] {
    return value.split(',').map((item) => item.trim()).filter(Boolean);
  }

  private resolveErrorMessage(error: HttpErrorResponse, fallback: string): string {
    if (error.error?.message) {
      return error.error.message;
    }
    return fallback;
  }
}
