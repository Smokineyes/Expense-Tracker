import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

class Expenses {
    double amount;
    String category;
    String description;
    Date date;

    public Expenses(double amount, String category, String description, Date date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }
}

public class Expense_Tracker1 extends JFrame {

    private JTextField catt, amt, det, remid, strt, end,catSearch;
    private JTextArea outputArea;

    private String username = "Allen";
    private String password = "1234";
    private String url = "jdbc:mysql://localhost/expense";

    public Expense_Tracker1() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            // Create table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS expenses (id INT AUTO_INCREMENT PRIMARY KEY, amount DOUBLE, category VARCHAR(50), description VARCHAR(255), date DATE)";
            connection.createStatement().execute(createTableSQL);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Container container = getContentPane();
        setTitle("Expense Tracker");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        JPanel form = new JPanel();
        GridBagConstraints gb = new GridBagConstraints();
        gb.fill = GridBagConstraints.HORIZONTAL;

        // Category Panel
        JPanel CatPanel = new JPanel(new FlowLayout());
        CatPanel.add(new JLabel("Category:"));
        catt = new JTextField(20);
        CatPanel.add(catt);
        gb.gridx = 0;
        gb.gridy = 0;
        form.add(CatPanel, gb);

        // Amount Panel
        JPanel AmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        AmountPanel.add(new JLabel("Amount:"));
        amt = new JTextField(20);
        AmountPanel.add(amt);
        gb.gridx = 0;
        gb.gridy = 1;
        form.add(AmountPanel, gb);

        // Description Panel
        JPanel DescPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        DescPanel.add(new JLabel("Description:"));
        det = new JTextField(20);
        DescPanel.add(det);
        gb.gridx = 0;
        gb.gridy = 2;
        form.add(DescPanel, gb);

        // Add Button
        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        gb.gridx = 0;
        gb.gridy = 3;
        form.add(addButton, gb);

        // Remove Expense Panel
        JPanel remButt = new JPanel(new FlowLayout(FlowLayout.LEFT));
        remButt.add(new JLabel("Id:"));
        remid = new JTextField(20);
        remButt.add(remid);
        JButton remButton = new JButton("Remove Expense");
        remButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExpense();
            }
        });
        remButt.add(remButton);
        gb.gridx = 0;
        gb.gridy = 4;
        form.add(remButt, gb);

        // Date Panel
        JPanel DatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        DatePanel.add(new JLabel("Start Date:"));
        strt = new JTextField(20);
        DatePanel.add(strt);
        DatePanel.add(new JLabel("End Date:"));
        end = new JTextField(20);
        DatePanel.add(end);
        JButton search = new JButton("Search");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterExpenseDate();
            }
        });
        DatePanel.add(search);
        gb.gridx = 0;
        gb.gridy = 5;
        form.add(DatePanel, gb);


        JPanel CatSearchPanel = new JPanel();
        CatSearchPanel.add(new JLabel("Category:"));
        catSearch = new JTextField(20);
        CatSearchPanel.add(catSearch);
        JButton catSearchButton = new JButton("Search by Category");
        catSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    filterExpenseCat();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        CatSearchPanel.add(catSearchButton);
        form.add(CatSearchPanel);
        container.add(form);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Show All Expenses Button
        JPanel showAllPanel = new JPanel();
        JButton showAll = new JButton("Print all Expenses");
        showAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printAllExpense();
            }
        });

        JButton summaryButton = new JButton("Show Summary");
        summaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showSummary();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        showAllPanel.add(showAll);
        showAllPanel.add(summaryButton);
        outputPanel.add(showAllPanel, BorderLayout.SOUTH);

        container.add(outputPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Expense_Tracker1();
            }
        });
    }

    private void addExpense() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            double amount = Double.parseDouble(amt.getText());
            String category = catt.getText();
            String description = det.getText();
            Date date = new Date();

            Expenses expense = new Expenses(amount, category, description, date);
            String insertSQL = "INSERT INTO expenses (amount, category, description, date) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setDouble(1, expense.amount);
            preparedStatement.setString(2, expense.category);
            preparedStatement.setString(3, expense.description);
            preparedStatement.setDate(4, new java.sql.Date(expense.date.getTime()));
            preparedStatement.executeUpdate();

            outputArea.setText("Expense added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("Error adding expense: " + e.getMessage());
        }
    }

    private void deleteExpense() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            int id = Integer.parseInt(remid.getText());
            String deleteSQL = "DELETE FROM expenses WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                outputArea.setText("Expense deleted successfully!");
            } else {
                outputArea.setText("No expense found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("Error deleting expense: " + e.getMessage());
        }
    }

    private void printAllExpense() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String retrieve = "SELECT * FROM expenses";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(retrieve);
            StringBuilder output = new StringBuilder();

            output.append(String.format("%-10s %-10s %-20s %-30s %-10s%n", "ID", "Amount", "Category", "Description", "Date"));
            output.append("----------------------------------------------------------------------------------------\n");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double amount = resultSet.getDouble("amount");
                String category = resultSet.getString("category");
                String description = resultSet.getString("description");
                Date date = resultSet.getDate("date");

                output.append(String.format("%-10d %-10.2f %-20s %-30s %-10s%n", id, amount, category, description, date.toString()));
            }

            outputArea.setText(output.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("Error retrieving expenses: " + e.getMessage());
        }
    }

    private void filterExpenseDate() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(strt.getText());
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end.getText());

            String retrieve = "SELECT * FROM expenses WHERE date >= ? AND date <= ?";
            PreparedStatement stmt = connection.prepareStatement(retrieve);
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet resultSet = stmt.executeQuery();

            StringBuilder output = new StringBuilder();
            output.append(String.format("%-10s %-10s %-20s %-30s %-10s%n", "ID", "Amount", "Category", "Description", "Date"));
            output.append("----------------------------------------------------------------------------------------\n");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double amount = resultSet.getDouble("amount");
                String category = resultSet.getString("category");
                String description = resultSet.getString("description");
                Date date = resultSet.getDate("date");

                output.append(String.format("%-10d %-10.2f %-20s %-30s %-10s%n", id, amount, category, description, date.toString()));
            }

            outputArea.setText(output.toString());
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            outputArea.setText("Error retrieving expenses: " + e.getMessage());
        }
    }
    private void filterExpenseCat() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String category = catSearch.getText();
        String retrieve = "SELECT * FROM expenses WHERE category = ?";
        PreparedStatement stmt = connection.prepareStatement(retrieve);
        stmt.setString(1, category);
        ResultSet resultSet = stmt.executeQuery();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-10s %-20s %-30s %-10s%n", "ID", "Amount", "Category", "Description", "Date"));
        sb.append("----------------------------------------------------------------------------------------\n");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            double amount = resultSet.getDouble("amount");
            String cat = resultSet.getString("category");
            String description = resultSet.getString("description");
            Date date = resultSet.getDate("date");

            sb.append(String.format("%-10d %-10.2f %-20s %-30s %-10s%n", id, amount, cat, description, date.toString()));
        }
        outputArea.setText(sb.toString());}
        catch (SQLException e) {
            e.printStackTrace();
            outputArea.setText("Error retrieving expenses: " + e.getMessage());
        }

    }

    private double retrieveAmount(String category) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String retrieve = "SELECT amount FROM expenses WHERE category = ?";
        PreparedStatement stmt = connection.prepareStatement(retrieve);
        stmt.setString(1, category);
        ResultSet resultSet = stmt.executeQuery();
        double sum = 0;
        while (resultSet.next()) {
            sum += resultSet.getDouble("amount");
        }
        return sum;}
    }

    private void showSummary() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String retrieve = "SELECT * FROM expenses";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(retrieve);
        double sum = 0;
        List<String> categories = new ArrayList<>();
        while (resultSet.next()) {
            sum += resultSet.getDouble("amount");
            String category = resultSet.getString("category");
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }
        Map<String, Double> map = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.##");
        for (String cat : categories) {
            map.put(cat, Double.valueOf(df.format(100 * retrieveAmount(cat) / sum)));
        }

        outputArea.setText(map.toString()+"");}
    }
}
