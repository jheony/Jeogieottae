USE jeogieottae;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(200) NOT NULL,
    is_deleted TINYINT(1) NOT NULL DEFAULT FALSE,
    UNIQUE KEY uk_users_email (email)
    );

CREATE TABLE IF NOT EXISTS coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    discount_type VARCHAR(50) NOT NULL,
    discount_value BIGINT NOT NULL,
    expires_at DATETIME,
    min_price BIGINT,
    accommodation_type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS accommodations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    location VARCHAR(50) NOT NULL,
    rating DOUBLE NOT NULL,
    view_count BIGINT DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS special_prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    discount BIGINT NOT NULL,
    due_timestamp DATETIME
    );

CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price BIGINT NOT NULL,
    accommodation_id BIGINT NOT NULL,
    special_price_id BIGINT
    );

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    check_in DATETIME NOT NULL,
    check_out DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    coupon_name VARCHAR(100),
    guest_count BIGINT NOT NULL,
    original_price BIGINT NOT NULL,
    discounted_price BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0
    );

