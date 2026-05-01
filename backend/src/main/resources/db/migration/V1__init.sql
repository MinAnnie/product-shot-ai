CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    plan VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE businesses (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    business_type VARCHAR(40) NOT NULL,
    description VARCHAR(500),
    style VARCHAR(40) NOT NULL,
    default_channel VARCHAR(60),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_businesses_user_id ON businesses(user_id);

CREATE TABLE business_brand_colors (
    business_id UUID NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    color VARCHAR(40) NOT NULL
);

CREATE INDEX idx_business_brand_colors_business_id ON business_brand_colors(business_id);

CREATE TABLE business_moods (
    business_id UUID NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    mood VARCHAR(60) NOT NULL
);

CREATE INDEX idx_business_moods_business_id ON business_moods(business_id);
