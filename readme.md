# Restaurant Order Management System

## Description
The Restaurant Order Management System allows users to register, log in, view available dishes, place orders, and track statistics. Administrators can manage dishes and view order statistics. All orders and transactions are recorded in the database.

## Features

- **Registration and Authentication**:
  - Ability to register as a user or an administrator.
  - Ability to log in for further actions.

- **User Menu**:
  - View available dishes.
  - Ability to place an order by selecting a dish and specifying the quantity.

- **Admin Menu**:
  - View all dishes.
  - Add, remove, and update dishes.
  - View order and transaction statistics.

- **Statistics**:
  - View the total number of orders.
  - View the number of available dishes.
  - View the number of ordered dishes by quantity.
  - View customer statistics.

## Database Structure

### `users` Table:
- `user_id` (INT): Unique identifier for the user.
- `name` (VARCHAR): User's name.
- `email` (VARCHAR): User's email address.
- `role` (VARCHAR): User's role (Admin/Customer).

### `dishes` Table:
- `dish_id` (INT): Unique identifier for the dish.
- `name` (VARCHAR): Dish name.
- `category` (VARCHAR): Dish category (e.g., "Italian", "Chinese").
- `price` (DECIMAL): Dish price.
- `available` (BOOLEAN): Availability of the dish for ordering.

### `orders` Table:
- `order_id` (INT): Unique identifier for the order.
- `user_id` (INT): Identifier of the user who placed the order.
- `dish_id` (INT): Identifier of the dish ordered.
- `quantity` (INT): Quantity of the ordered dish.

### `transactions` Table:
- `transaction_id` (INT): Unique identifier for the transaction.
- `user_id` (INT): Identifier of the user who made the transaction.
- `total_amount` (DECIMAL): Total amount of the transaction.
- `quantity` (INT): Quantity of dishes purchased.
- `transaction_date` (TIMESTAMP): Date and time of the transaction.

## Requirements

To run the project on your computer, the following must be installed:

- Java 8 or higher.
- PostgreSQL.
- JDBC driver for PostgreSQL.

## Installation and Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/restaurant-order-management.git
   cd restaurant-order-management
   ```

Make sure PostgreSQL is installed and the database with the required structure is created. If the database hasn’t been created yet, run the following SQL queries to create the tables:

```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    role VARCHAR(50)
);

CREATE TABLE dishes (
    dish_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(50),
    price DECIMAL(10, 2),
    available BOOLEAN
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id INT,
    dish_id INT,
    quantity INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id)
);

CREATE TABLE transactions (
    transaction_id SERIAL PRIMARY KEY,
    user_id INT,
    total_amount DECIMAL(10, 2),
    quantity INT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

Add sample data to the `users` and `dishes` tables (for testing). Example data for the `dishes` table:

```sql
INSERT INTO dishes (name, category, price, available) VALUES
('Pasta Carbonara', 'Italian', 33.43, FALSE),
('Pizza Margherita', 'Italian', 20.71, TRUE),
('Caesar Salad', 'Salad', 43.16, FALSE),
('Chicken Curry', 'Indian', 9.44, FALSE),
('Vegetable Stir Fry', 'Chinese', 37.78, TRUE);
```

Compile and run the project.

## Example Usage
- Log in as a user or an administrator.
  - For users:
    - View available dishes.
    - Place an order by selecting a dish and specifying the quantity.
  - For administrators:
    - View statistics on orders and dishes.
    - Add, update, or delete dishes.

## Important Notes
- Login is done by user ID.
- The administrator must enter a password to access the admin menu.
- When placing an order, an entry is made in the `orders` table, and a transaction is recorded in the `transactions` table.
