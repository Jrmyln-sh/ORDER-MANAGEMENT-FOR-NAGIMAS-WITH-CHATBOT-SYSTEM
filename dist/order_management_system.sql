/*CREATE DATABASE*/
CREATE DATABASE order_management_system;

/*CREATE TABLE*/
CREATE TABLE admin_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username_admin VARCHAR(50) NOT NULL UNIQUE,
    password_admin VARCHAR(255) NOT NULL,
    uid VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT UNIQUE NOT NULL,
    customer_name VARCHAR(50) NOT NULL UNIQUE,
    customer_username VARCHAR(50) NOT NULL UNIQUE,
    customer_password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    reset_code VARCHAR(255),
    reset_code_expiration DATETIME
);

CREATE TABLE guest_customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guest_id INT UNIQUE NOT NULL,
    guest_name_customer VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT UNIQUE NOT NULL,
    categories VARCHAR(50),
    product_name VARCHAR(50),
    price DECIMAL(10, 2),
    quantity INT,
    product_status VARCHAR(20),
    product_image BLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE customer_orders (
    customer_order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    transaction_number VARCHAR(50) UNIQUE NOT NULL,
    order_status ENUM('Pending', 'Processing', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending',
    order_type ENUM('Dine-In', 'Take-Out') NOT NULL DEFAULT 'Dine-In',
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL
);

CREATE TABLE guest_orders (
    guest_order_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_id INT,
    transaction_number VARCHAR(50) UNIQUE NOT NULL,
    order_status ENUM('Pending', 'Processing', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending',
    order_type ENUM('Dine-In', 'Take-Out') NOT NULL DEFAULT 'Dine-In',
    FOREIGN KEY (guest_id) REFERENCES guest_customers(id) ON DELETE SET NULL
);

CREATE TABLE customer_order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES customer_orders(customer_order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE guest_order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES guest_orders(guest_order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

/*SELECTING TABLE*/
SELECT * FROM admin_users;
SELECT * FROM customers;
SELECT * FROM guest_customers;
SELECT * FROM products;
SELECT * FROM customer_orders;
SELECT * FROM guest_orders;
SELECT * FROM customer_order_items;
SELECT * FROM guest_order_items;

/*DROPING TABLE*/
DROP admin_users;
DROP customers;
DROP guest_customers;
DROP products;
DROP customer_orders;
DROP guest_orders;
DROP customer_order_items;
DROP guest_order_items;

/*INSERTING VALUES*/
INSERT INTO admin_users (username_admin, password_admin) 
VALUES ('miguel', 'miguel123'),
('john', 'john123'),
('jeremy', 'jeremy123'),
('mark', 'mark123'),
('joshua', 'joshua123');

INSERT INTO customers (customer_name, customer_username, customer_password, email) 
VALUES ('miguel soriano', 'miguel123', 'miguel123', 'officialmiguel14@gmail.com'),
('John Christofer', 'tof123', 'tof123', 'johnchristoferlucero14@gmail.com'),
('Lei Loquis', 'lei123', 'lei123', 'leiaustria161@gmail.com');

/*ALTER TABLE*/
ALTER TABLE customer_orders 
ADD COLUMN order_type ENUM('Dine-In', 'Take-Out') NOT NULL DEFAULT 'Dine-In' 
AFTER order_status;


ALTER TABLE guest_orders 
ADD COLUMN order_type ENUM('Dine-In', 'Take-Out') NOT NULL DEFAULT 'Dine-In' 
AFTER order_status;