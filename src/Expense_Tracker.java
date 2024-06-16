import java.sql.*;
import java.text.ParseException;
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
//            Expense expense = new Expense(80.0, "Food", "Lunch at restaurant", new Date());
//            addExpense(connection, expense);


                filterExpenseDate(connection,"2024-06-10","2024-06-16");

            connection.close();
        } catch (SQLException | ParseException e) {
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
    public static void printallExpense(Connection connection) throws SQLException {
        String retrieve="Select * from expenses";
        Statement stmt= connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(retrieve);
        while (resultSet.next()){
            System.out.println(resultSet.getString("amount"));
        }

    }
    public static void deleteExpense(Connection connection,int id) throws SQLException {
        String update="delete from expenses where id="+id;
        Statement stmt= connection.createStatement();
        int del = stmt.executeUpdate(update);
       printallExpense(connection);

    }
    public static void filterExpenseDate(Connection connection,String startdate,String enddate) throws SQLException, ParseException {
        Date End= new SimpleDateFormat("yyyy-MM-dd").parse(enddate);
        Date Start= new SimpleDateFormat("yyyy-MM-dd").parse(startdate);
        String retrieve = "SELECT * FROM expenses WHERE date >= ? and date<=?";
        PreparedStatement stmt = connection.prepareStatement(retrieve);
        stmt.setDate(1, new java.sql.Date(Start.getTime()));
        stmt.setDate(2, new java.sql.Date(End.getTime()));
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            System.out.println("Amount: " + resultSet.getDouble("amount"));
            System.out.println("Category: " + resultSet.getString("category"));
            System.out.println("Description: " + resultSet.getString("description"));
            System.out.println("Date: " + resultSet.getDate("date"));
            System.out.println("-----");
        }
    }
    public static void filterExpenseCat(Connection connection,String cat) throws SQLException {
        String retrieve="Select * from expenses where category="+cat;
        Statement stmt= connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(retrieve);
        while (resultSet.next()){
            System.out.println(resultSet.getString("amount"));
        }

    }
}
