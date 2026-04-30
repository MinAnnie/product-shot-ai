# Product Shot AI — Project Plan

## Goal

Build a web app that helps small businesses transform simple phone product photos into professional product images with AI-generated backgrounds.

## Core users

Small business owners, creators, restaurants, clothing stores, cosmetics brands, and online sellers who need better visuals for social media, catalogs, marketplaces, or websites.

## Plans

### Free

- 1 business profile
- Limited monthly generations
- Optional watermark in the future

### Paid

- Up to 5 business profiles
- More monthly generations
- No watermark
- Longer image history

## Core entities

- User
- Plan
- Business
- ProductPhoto
- GeneratedImage

## MVP phases

### Phase 1 — Foundation

- Initialize Spring Boot backend
- Initialize Angular frontend
- Configure PostgreSQL
- Add Docker Compose support

### Phase 2 — Business profiles

- User registration/login
- Create business profile
- Enforce business limit by plan
- Store brand answers

### Phase 3 — Image generation

- Upload product photo
- Build prompt from business profile
- Call AI provider
- Store generated result
- Show result in Angular

### Phase 4 — Productization

- Generation quotas
- Paid plan integration
- Gallery/history
- Downloads
- Deployment with Dokploy
