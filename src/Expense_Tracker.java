import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Expense {
    double amount;
    String category;
    String description;
    Date date;

    public Expense(double amount, String category, String description, Date date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }
}

public class Expense_Tracker {

    public static void main(String[] args) {
        String username = "Allen";
        String password = "1234";
        String url = "jdbc:mysql://localhost/expense";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Create table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS expenses (id INT AUTO_INCREMENT PRIMARY KEY, amount DOUBLE, category VARCHAR(50), description VARCHAR(255), date DATE)";
            connection.createStatement().execute(createTableSQL);

            // Insert an expense record
            Expense expense = new Expense(50.0, "Food", "Lunch at restaurant", new Date());
            addExpense(connection, expense);

            connection.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addExpense(Connection connection, Expense expense) throws SQLException {
        String insertSQL = "INSERT INTO expenses (amount, category, description, date) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setDouble(1, expense.amount);
        preparedStatement.setString(2, expense.category);
        preparedStatement.setString(3, expense.description);
        preparedStatement.setDate(4, new java.sql.Date(expense.date.getTime()));

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Expense added successfully!");
        } else {
            System.out.println("Failed to add expense.");
        }
    }
}
