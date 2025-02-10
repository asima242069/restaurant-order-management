public class User {
    private int userId;
    private String name;
    private String email;
    private String role;

    public User(int userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;  // Убедитесь, что роль передается и сохраняется
    }

    public String getRole() {
        return role;  // Убедитесь, что метод правильно возвращает роль
    }

    public int getUserId() {
        return userId;
    }
}
