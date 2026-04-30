# product-shot-ai

AI-powered Spring Boot and Angular web app for generating professional product photo backgrounds using personalized business profiles and brand styles.

## Project structure

```txt
product-shot-ai/
  backend/              # Spring Boot API
  frontend/             # Angular app
  docs/                 # Planning and technical documentation
  scripts/              # Utility scripts
  docker-compose.yml    # Local/Dokploy deployment stack
  .env.example          # Environment variable template
```

## Product concept

Users create business profiles with brand context such as business type, style, colors, and mood. Then they upload product photos and the app generates professional AI backgrounds based on each business profile.

## Planned flow

1. User registers and logs in.
2. User creates one or more business profiles.
3. Free users can create 1 business.
4. Paid users can create up to 5 businesses.
5. Each business stores answers about brand style and target use.
6. User uploads product photos inside a business.
7. The app generates professional backgrounds using AI.
8. User downloads or saves generated images.

## Tech stack

- **Frontend:** Angular
- **Backend:** Spring Boot
- **Database:** PostgreSQL
- **Deployment:** Docker Compose / Dokploy
- **AI provider:** To be decided, initially planned around an external image generation API

## Local setup

Copy the environment file:

```bash
cp .env.example .env
```

Start services:

```bash
docker compose up -d
```


## Main modules planned

### Backend

- Auth and users
- Plans and limits
- Business profiles
- Product photos
- AI generations
- Image storage

### Frontend

- Authentication pages
- Dashboard
- Business creation wizard
- Business detail page
- Product photo upload
- Generation results gallery
```
