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

    public Expense_Tracker1() {
        Container container=getContentPane();
        setTitle("Expense tracker");
        setSize(600,800);
        setLayout(new GridLayout(2,1));
        JPanel form= new JPanel(new GridBagLayout());
            GridBagConstraints gb= new GridBagConstraints();

         JPanel CatPanel= new JPanel();
        CatPanel.add(new JLabel("Category:"));
        JTextField catt=new JTextField(20);
        CatPanel.add(catt);
        JPanel AmountPanel= new JPanel();
        AmountPanel.add(new JLabel("Amount:"));
        JTextField amt=new JTextField(20);
        AmountPanel.add(amt);
        JPanel DescPanel= new JPanel();
        DescPanel.add(new JLabel("Description:"));
        JTextField det=new JTextField(20);
        DescPanel.add(det);
        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                addExpense();
            }
        });
        JPanel remButt= new JPanel();
        remButt.add(new JLabel("Id:"));
        JTextField remid=new JTextField(20);
        remButt.add(remid);
        JButton remButton = new JButton("Remove Expense");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //  deleteExpenses();
            }
        });
        remButt.add(remButton);

        JPanel Output=new JPanel();
        JButton showall=new JButton("Print all Expenses");
        showall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  PrintallExpenses();
            }
        });
        Output.add(showall);

        JPanel DatePanel= new JPanel();
        DatePanel.add(new JLabel("Start Date:"));
        JTextField strt=new JTextField(20);
        DatePanel.add(strt);
        DatePanel.add(new JLabel("End date:"));
        JTextField end=new JTextField(20);
        JButton search=new JButton("Search");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  PrintallExpenses();
            }
        });
        DatePanel.add(end);
        Output.add(DatePanel);
        Output.add(search);
        gb.gridx=0;
        gb.gridy=0;
        form.add(CatPanel,gb);
        gb.gridx=0;
        gb.gridy=1;
        form.add(AmountPanel,gb);
        gb.gridx=0;
        gb.gridy=2;
        form.add(DescPanel,gb);
        gb.gridx=0;
        gb.gridy=4;
        form.add(addButton,gb);
        gb.gridx=0;
        gb.gridy=7;
        form.add(remButt,gb);

        add(form);
        add(Output);
        setVisible(true);
    }
    public static void main(String[] args) {
        Expense_Tracker1 ep=new Expense_Tracker1();
    }
}
