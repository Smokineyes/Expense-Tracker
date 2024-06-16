import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.*;

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
        summery(connection);
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
        System.out.printf("%-10s %-10s %-20s %-30s %-10s%n", "ID", "Amount", "Category", "Description", "Date");
        System.out.println("----------------------------------------------------------------------------------------");

        // Print table rows
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            double amount = resultSet.getDouble("amount");
            String category = resultSet.getString("category");
            String description = resultSet.getString("description");
            Date date = resultSet.getDate("date");

            System.out.printf("%-10d %-10.2f %-20s %-30s %-10s%n", id, amount, category, description, date.toString());
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

        System.out.printf("%-10s %-10s %-20s %-30s %-10s%n", "ID", "Amount", "Category", "Description", "Date");
        System.out.println("----------------------------------------------------------------------------------------");

        // Print table rows
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            double amount = resultSet.getDouble("amount");
            String category = resultSet.getString("category");
            String description = resultSet.getString("description");
            Date date = resultSet.getDate("date");

            System.out.printf("%-10d %-10.2f %-20s %-30s %-10s%n", id, amount, category, description, date.toString());
        }
    }
    public static void filterExpenseCat(Connection connection,String cat) throws SQLException {
        String retrieve="Select * from expenses where category="+cat;
        Statement stmt= connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(retrieve);
        System.out.printf("%-10s %-10s %-20s %-30s %-10s%n", "ID", "Amount", "Category", "Description", "Date");
        System.out.println("----------------------------------------------------------------------------------------");

        // Print table rows
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            double amount = resultSet.getDouble("amount");
            String category = resultSet.getString("category");
            String description = resultSet.getString("description");
            Date date = resultSet.getDate("date");

            System.out.printf("%-10d %-10.2f %-20s %-30s %-10s%n", id, amount, category, description, date.toString());
        }

    }
    public static double retrieveAmount(Connection connection,String category) throws SQLException {
        String retrieve = "SELECT amount FROM expenses WHERE category = ?";
        PreparedStatement stmt = connection.prepareStatement(retrieve);
        stmt.setString(1, category);
        ResultSet resultSet = stmt.executeQuery();
        double sum = 0;
        while (resultSet.next()) {
            sum += resultSet.getDouble("amount");
        }
        return sum;
    }
    public static void summery(Connection connection) throws SQLException {
        String retrieve="Select * from expenses ";
        Statement stmt= connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(retrieve);
        double sum=0;
        List <String>categories= new ArrayList<String>();
        while (resultSet.next()){
            sum+= resultSet.getDouble("amount");
            String category = resultSet.getString("category");
            if (categories.stream().noneMatch(c -> c.equalsIgnoreCase(category))){
                categories.add(resultSet.getString("category"));
            }
        }
        Map<String, Double> map = new HashMap<String, Double>();
        DecimalFormat df = new DecimalFormat("#.##");
// method to add the key,value pair in hashmap
        for (String cat:categories){
            map.put(cat, Double.valueOf(df.format(100*retrieveAmount(connection,cat)/sum )));

        }
        System.out.println(map);
    }
}
