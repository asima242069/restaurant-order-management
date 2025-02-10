import java.sql.*;
import java.util.Scanner;

public class MainApp {
    private static DatabaseHandler databaseHandler = new DatabaseHandler();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Restaurant Order Management ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number (1-3).");
                continue;
            }

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void login() {
        System.out.print("Enter your user ID: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());
        User user = getUserFromDatabase(userId);

        if (user == null) {
            System.out.println("User not found. Please register first.");
            return;
        }

        // Отладка: выводим роль пользователя
        String role = user.getRole();
        System.out.println("User role: '" + role + "'");

        // Проверка на пустое значение роли
        if (role == null || role.trim().isEmpty()) {
            System.out.println("Role is missing or empty in the database.");
            return;
        }

        // Приводим роль к верхнему регистру и проверяем
        if ("ADMIN".equals(role.toUpperCase())) {
            // Если это администратор, проверяем пароль
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine();
            if ("adminpassword123".equals(password)) {
                adminMenu();  // Запускаем меню администратора
            } else {
                System.out.println("Invalid password.");
            }
        } else if ("CUSTOMER".equals(role.toUpperCase())) {
            // Если это покупатель, запускаем меню покупателя
            customerMenu(user);
        } else {
            System.out.println("Invalid role.");
        }
    }



    private static void register() {
        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());

        if (getUserFromDatabase(userId) != null) {
            System.out.println("User ID already exists.");
            return;
        }

        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter role (Admin/Customer): ");
        String role = scanner.nextLine().trim();

        // Admin registration password check
        if ("Admin".equalsIgnoreCase(role)) {
            System.out.print("Enter admin registration password: ");
            String password = scanner.nextLine();
            if (!"adminpassword123".equals(password)) {
                System.out.println("Invalid admin password. Registration failed.");
                return;
            }
        }

        String query = "INSERT INTO users (user_id, name, email, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, role);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User registered successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View all dishes");
            System.out.println("2. Add a new dish");
            System.out.println("3. Delete a dish");
            System.out.println("4. Update dish details");
            System.out.println("5. View statistics");
            System.out.println("6. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    viewAllDishes();
                    break;
                case 2:
                    addNewDish();
                    break;
                case 3:
                    deleteDish();
                    break;
                case 4:
                    updateDishDetails();
                    break;
                case 5:
                    viewStatistics();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerMenu(User customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. View all dishes");
            System.out.println("2. Place an order");
            System.out.println("3. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    viewAllDishes();
                    break;
                case 2:
                    placeOrder(customer);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAllDishes() {
        String query = "SELECT * FROM dishes";
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\n=== List of Dishes ===");
            while (resultSet.next()) {
                System.out.printf("Dish ID: %d, Name: %s, Price: %.2f, Category: %s, Available: %b%n",
                        resultSet.getInt("dish_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("category"),
                        resultSet.getBoolean("available"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addNewDish() {
        System.out.print("Enter dish name: ");
        String name = scanner.nextLine();

        System.out.print("Enter dish description: ");
        String description = scanner.nextLine();

        System.out.print("Enter dish price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter dish category: ");
        String category = scanner.nextLine();

        String query = "INSERT INTO dishes (name, description, price, category) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setString(4, category);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Dish added successfully!");
            } else {
                System.out.println("Failed to add dish.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteDish() {
        System.out.print("Enter the Dish ID to delete: ");
        int dishId = Integer.parseInt(scanner.nextLine());

        String query = "DELETE FROM dishes WHERE dish_id = ?";
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, dishId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Dish deleted successfully!");
            } else {
                System.out.println("Dish not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateDishDetails() {
        System.out.print("Enter the Dish ID to update: ");
        int dishId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new name (leave blank to keep unchanged): ");
        String name = scanner.nextLine();

        System.out.print("Enter new description (leave blank to keep unchanged): ");
        String description = scanner.nextLine();

        System.out.print("Enter new price (-1 to keep unchanged): ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter new category (leave blank to keep unchanged): ");
        String category = scanner.nextLine();

        String query = "UPDATE dishes SET name = COALESCE(NULLIF(?, ''), name), description = COALESCE(NULLIF(?, ''), description), " +
                "price = COALESCE(NULLIF(?, -1), price), category = COALESCE(NULLIF(?, ''), category) WHERE dish_id = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setString(4, category);
            statement.setInt(5, dishId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Dish updated successfully!");
            } else {
                System.out.println("Dish not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewStatistics() {
        // Подключаемся к базе данных
        try (Connection connection = databaseHandler.getConnection()) {
            // 1. Общее количество заказов
            String ordersQuery = "SELECT COUNT(*) FROM orders";
            try (PreparedStatement ordersStatement = connection.prepareStatement(ordersQuery);
                 ResultSet ordersResultSet = ordersStatement.executeQuery()) {
                if (ordersResultSet.next()) {
                    int totalOrders = ordersResultSet.getInt(1);
                    System.out.println("Total orders: " + totalOrders);
                }
            }

            // 2. Общее количество доступных блюд
            String dishesQuery = "SELECT COUNT(*) FROM dishes WHERE available = true";
            try (PreparedStatement dishesStatement = connection.prepareStatement(dishesQuery);
                 ResultSet dishesResultSet = dishesStatement.executeQuery()) {
                if (dishesResultSet.next()) {
                    int availableDishes = dishesResultSet.getInt(1);
                    System.out.println("Available dishes: " + availableDishes);
                }
            }

            // 3. Общее количество заказанных блюд (по каждому блюду)
            String orderedDishesQuery = "SELECT d.name, SUM(o.quantity) AS total_ordered FROM orders o " +
                    "JOIN dishes d ON o.dish_id = d.dish_id GROUP BY d.name";
            try (PreparedStatement orderedDishesStatement = connection.prepareStatement(orderedDishesQuery);
                 ResultSet orderedDishesResultSet = orderedDishesStatement.executeQuery()) {
                System.out.println("\n=== Ordered Dishes ===");
                while (orderedDishesResultSet.next()) {
                    String dishName = orderedDishesResultSet.getString("name");
                    int totalOrdered = orderedDishesResultSet.getInt("total_ordered");
                    System.out.printf("%s: %d ordered%n", dishName, totalOrdered);
                }
            }

            // 4. Общее количество клиентов
            String customersQuery = "SELECT COUNT(*) FROM users WHERE role = 'Customer'";
            try (PreparedStatement customersStatement = connection.prepareStatement(customersQuery);
                 ResultSet customersResultSet = customersStatement.executeQuery()) {
                if (customersResultSet.next()) {
                    int totalCustomers = customersResultSet.getInt(1);
                    System.out.println("\nTotal customers: " + totalCustomers);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void placeOrder(User customer) {
        // Запрос всех доступных блюд
        String query = "SELECT * FROM dishes WHERE available = true"; // Запрос доступных блюд
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Вывод всех доступных блюд
            System.out.println("\n=== Available Dishes ===");
            int counter = 1; // Счётчик для отображения блюд
            while (resultSet.next()) {
                int dishId = resultSet.getInt("dish_id");
                String dishName = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                System.out.printf("%d. Dish ID: %d, Name: %s, Price: %.2f%n", counter++, dishId, dishName, price);
            }

            if (counter == 1) {
                System.out.println("No available dishes at the moment.");
                return;
            }

            // Запрашиваем выбор блюда у пользователя
            System.out.print("\nEnter the dish number to order: ");
            int dishChoice = Integer.parseInt(scanner.nextLine().trim());

            // Запрашиваем количество для выбранного блюда
            System.out.print("Enter the quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            // Запрос для получения выбранного блюда
            String dishQuery = "SELECT * FROM dishes WHERE dish_id = ?";
            try (PreparedStatement dishStatement = connection.prepareStatement(dishQuery)) {
                dishStatement.setInt(1, dishChoice);
                ResultSet dishResultSet = dishStatement.executeQuery();

                if (dishResultSet.next()) {
                    boolean isAvailable = dishResultSet.getBoolean("available");
                    if (isAvailable) {
                        double price = dishResultSet.getDouble("price");
                        double totalAmount = price * quantity;  // Рассчитываем общую сумму заказа

                        // Вставляем заказ в базу данных
                        String insertOrderQuery = "INSERT INTO orders (user_id, dish_id, quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertOrderQuery)) {
                            insertStatement.setInt(1, customer.getUserId());
                            insertStatement.setInt(2, dishChoice);
                            insertStatement.setInt(3, quantity);
                            int rowsInserted = insertStatement.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Your order has been placed successfully!");

                                // Записываем транзакцию в таблицу transactions
                                String insertTransactionQuery = "INSERT INTO transactions (user_id, total_amount, quantity) VALUES (?, ?, ?)";
                                try (PreparedStatement transactionStatement = connection.prepareStatement(insertTransactionQuery)) {
                                    transactionStatement.setInt(1, customer.getUserId());
                                    transactionStatement.setDouble(2, totalAmount);
                                    transactionStatement.setInt(3, quantity); // Записываем количество купленных блюд
                                    int rowsTransactionInserted = transactionStatement.executeUpdate();
                                    if (rowsTransactionInserted > 0) {
                                        System.out.println("Transaction recorded successfully.");
                                    } else {
                                        System.out.println("Failed to record the transaction.");
                                    }
                                }
                            } else {
                                System.out.println("Failed to place the order.");
                            }
                        }
                    } else {
                        System.out.println("Sorry, the selected dish is not available.");
                    }
                } else {
                    System.out.println("Dish not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static User getUserFromDatabase(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                System.out.println("Retrieved role for user " + userId + ": '" + role + "'");
                return new User(userId, resultSet.getString("name"), resultSet.getString("email"), role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
