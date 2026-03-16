CREATE DATABASE IF NOT EXISTS car_rental2 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE car_rental2;

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    role VARCHAR(30) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vehicle (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vehicle_no VARCHAR(30) NOT NULL UNIQUE,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(100) NOT NULL,
    color VARCHAR(30),
    seat_count INT,
    daily_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    hot_flag TINYINT NOT NULL DEFAULT 0,
    cover_image VARCHAR(255),
    description VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    gender VARCHAR(10),
    driver_license_no VARCHAR(50) NOT NULL UNIQUE,
    id_card_no VARCHAR(30) NOT NULL UNIQUE,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rental_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(40) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    pickup_time DATETIME NOT NULL,
    return_time DATETIME NOT NULL,
    daily_rent DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    order_status VARCHAR(30) NOT NULL,
    remark VARCHAR(500),
    created_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_order_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE IF NOT EXISTS banner (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    redirect_url VARCHAR(255),
    sort INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_vehicle_search ON vehicle (brand, model, status);
CREATE INDEX idx_vehicle_hot ON vehicle (hot_flag, status, updated_at);
CREATE INDEX idx_order_query ON rental_order (order_status, customer_id, vehicle_id, pickup_time);

INSERT INTO sys_user (username, password, real_name, role, status)
VALUES ('admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi8Qm2YkgL1A6Q671O5jM90SEzGyF2K', '系统管理员', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    real_name = VALUES(real_name),
    role = VALUES(role),
    status = VALUES(status),
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO vehicle (vehicle_no, brand, model, color, seat_count, daily_price, status, hot_flag, cover_image, description)
VALUES
('京A10001', 'Toyota', 'Camry 2.0', 'Black', 5, 299.00, 'AVAILABLE', 1, '/static/camry.jpg', '商务出行热门车型'),
('京A10002', 'Tesla', 'Model 3', 'White', 5, 499.00, 'AVAILABLE', 1, '/static/model3.jpg', '新能源热门车型')
ON DUPLICATE KEY UPDATE
    brand = VALUES(brand),
    model = VALUES(model),
    color = VALUES(color),
    seat_count = VALUES(seat_count),
    daily_price = VALUES(daily_price),
    status = VALUES(status),
    hot_flag = VALUES(hot_flag),
    cover_image = VALUES(cover_image),
    description = VALUES(description),
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO customer (customer_name, phone, gender, driver_license_no, id_card_no, status)
VALUES
('张三', '13800000001', '男', 'DL10000001', '110101199001010011', 1),
('李四', '13800000002', '女', 'DL10000002', '110101199202020022', 1)
ON DUPLICATE KEY UPDATE
    customer_name = VALUES(customer_name),
    phone = VALUES(phone),
    gender = VALUES(gender),
    id_card_no = VALUES(id_card_no),
    status = VALUES(status),
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO banner (title, image_url, redirect_url, sort, status)
VALUES
('春季租车优惠', '/banner/spring-sale.jpg', '/promotion/spring-sale', 1, 1),
('新能源专场', '/banner/new-energy.jpg', '/promotion/ev', 2, 1)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    image_url = VALUES(image_url),
    redirect_url = VALUES(redirect_url),
    sort = VALUES(sort),
    status = VALUES(status),
    updated_at = CURRENT_TIMESTAMP;

EXPLAIN
SELECT id, order_no, customer_id, vehicle_id, pickup_time
FROM rental_order
WHERE order_status = 'CREATED' AND customer_id = 1
ORDER BY pickup_time DESC;
