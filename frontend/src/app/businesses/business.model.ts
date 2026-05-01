export type BusinessType =
  | 'CLOTHING'
  | 'RESTAURANT'
  | 'COSMETICS'
  | 'TECHNOLOGY'
  | 'JEWELRY'
  | 'DECORATION'
  | 'BAKERY'
  | 'OTHER';

export type BrandStyle =
  | 'STREET'
  | 'ELEGANT'
  | 'CUTE'
  | 'MINIMALIST'
  | 'VINTAGE'
  | 'LUXURY'
  | 'NATURAL'
  | 'COLORFUL';

export interface Business {
  id: string;
  name: string;
  businessType: BusinessType;
  description?: string;
  style: BrandStyle;
  brandColors: string[];
  moods: string[];
  defaultChannel?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateBusinessRequest {
  name: string;
  businessType: BusinessType;
  description?: string;
  style: BrandStyle;
  brandColors: string[];
  moods: string[];
  defaultChannel?: string;
}

export interface SelectOption<T extends string> {
  value: T;
  label: string;
}

export const BUSINESS_TYPE_OPTIONS: SelectOption<BusinessType>[] = [
  { value: 'CLOTHING', label: 'Clothing' },
  { value: 'RESTAURANT', label: 'Restaurant' },
  { value: 'COSMETICS', label: 'Cosmetics' },
  { value: 'TECHNOLOGY', label: 'Technology' },
  { value: 'JEWELRY', label: 'Jewelry' },
  { value: 'DECORATION', label: 'Decoration' },
  { value: 'BAKERY', label: 'Bakery' },
  { value: 'OTHER', label: 'Other' },
];

export const BRAND_STYLE_OPTIONS: SelectOption<BrandStyle>[] = [
  { value: 'STREET', label: 'Street' },
  { value: 'ELEGANT', label: 'Elegant' },
  { value: 'CUTE', label: 'Cute' },
  { value: 'MINIMALIST', label: 'Minimalist' },
  { value: 'VINTAGE', label: 'Vintage' },
  { value: 'LUXURY', label: 'Luxury' },
  { value: 'NATURAL', label: 'Natural' },
  { value: 'COLORFUL', label: 'Colorful' },
];
