# Restaurant Order Management System

## Описание
Система управления заказами в ресторане позволяет пользователям зарегистрироваться, войти в систему, просматривать доступные блюда, делать заказы и отслеживать статистику. Администраторы могут управлять блюдами и просматривать статистику по заказам. Все заказы и транзакции записываются в базу данных.

## Функции

- **Регистрация и авторизация**:
  - Возможность зарегистрироваться в системе как пользователь или администратор.
  - Возможность войти в систему для выполнения дальнейших действий.

- **Меню пользователя**:
  - Просмотр доступных блюд.
  - Возможность сделать заказ с выбором блюда и количества.

- **Меню администратора**:
  - Просмотр всех блюд.
  - Добавление, удаление и обновление блюд.
  - Просмотр статистики по заказам и транзакциям.

- **Статистика**:
  - Просмотр общего количества заказов.
  - Просмотр количества доступных блюд.
  - Просмотр заказанных блюд по их количеству.
  - Просмотр статистики по клиентам.

## Структура базы данных

### Таблица `users`:
- `user_id` (INT): Уникальный идентификатор пользователя.
- `name` (VARCHAR): Имя пользователя.
- `email` (VARCHAR): Электронная почта пользователя.
- `role` (VARCHAR): Роль пользователя (Admin/Customer).

### Таблица `dishes`:
- `dish_id` (INT): Уникальный идентификатор блюда.
- `name` (VARCHAR): Название блюда.
- `category` (VARCHAR): Категория блюда (например, "Italian", "Chinese").
- `price` (DECIMAL): Цена блюда.
- `available` (BOOLEAN): Доступность блюда для заказа.

### Таблица `orders`:
- `order_id` (INT): Уникальный идентификатор заказа.
- `user_id` (INT): Идентификатор пользователя, который сделал заказ.
- `dish_id` (INT): Идентификатор блюда, которое заказано.
- `quantity` (INT): Количество заказанных блюд.

### Таблица `transactions`:
- `transaction_id` (INT): Уникальный идентификатор транзакции.
- `user_id` (INT): Идентификатор пользователя, который совершил транзакцию.
- `total_amount` (DECIMAL): Общая сумма транзакции.
- `quantity` (INT): Количество купленных блюд.
- `transaction_date` (TIMESTAMP): Дата и время транзакции.

## Требования

Для запуска проекта на вашем компьютере должны быть установлены:

- Java 8 или выше.
- PostgreSQL (или другая база данных, поддерживающая SQL).
- JDBC-драйвер для PostgreSQL (или другой, если используете другую СУБД).

## Установка и запуск

1. Склонируйте репозиторий:

   ```bash
   git clone https://github.com/your-username/restaurant-order-management.git
   cd restaurant-order-management
Убедитесь, что у вас установлен PostgreSQL и создана база данных с нужной структурой. Если база данных еще не создана, выполните запросы для создания таблиц:

sql
Copy
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
Внесите данные в таблицы users и dishes (для тестирования). Пример данных для таблицы dishes:

sql
Copy
INSERT INTO dishes (name, category, price, available) VALUES
('Pasta Carbonara', 'Italian', 33.43, FALSE),
('Pizza Margherita', 'Italian', 20.71, TRUE),
('Caesar Salad', 'Salad', 43.16, FALSE),
('Chicken Curry', 'Indian', 9.44, FALSE),
('Vegetable Stir Fry', 'Chinese', 37.78, TRUE);
Скомпилируйте и запустите проект:

Для компиляции и запуска проекта используйте следующую команду:

bash
Copy
javac MainApp.java
java MainApp
Следуйте инструкциям в консоли для входа в систему, регистрации и работы с меню.

Пример использования
Войдите в систему как пользователь или администратор.
Для пользователя:
Просмотрите доступные блюда.
Сделайте заказ, выбрав блюдо и указав количество.
Для администратора:
Просмотрите статистику по заказам и блюдам.
Добавьте, обновите или удалите блюда.
Важные замечания
Вход в систему осуществляется по ID пользователя.
Администратор должен ввести пароль для доступа к меню администратора.
При размещении заказа происходит запись в таблицу orders, а также транзакция фиксируется в таблице transactions.