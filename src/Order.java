import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private List<Dish> dishes;
    private String status; // "In Progress", "Completed", "Cancelled"

    public Order(int orderId, int userId, List<Dish> dishes, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.dishes = dishes;
        this.status = status;
    }

    // Getters and Setters
}
