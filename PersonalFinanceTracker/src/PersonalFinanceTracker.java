import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PersonalFinanceTracker {

    static class Transaction {
        private String type; // "Income" or "Expense"
        private double amount;
        private String category;
        private String date;

        // Constructor to initialize transaction details
        public Transaction(String type, double amount, String category, String date) {
            this.type = type;
            this.amount = amount;
            this.category = category;
            this.date = date;
        }

        // Getters for transaction details
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public String getCategory() { return category; }
        public String getDate() { return date; }

        @Override
        public String toString() {
            return type + ": ₹" + amount + " (" + category + ") on " + date;
        }
    }

    // Income subclass to differentiate income transactions
    static class Income extends Transaction {
        public Income(double amount, String category, String date) {
            super("Income", amount, category, date);
        }
    }

    // Expense subclass to differentiate expense transactions
    static class Expense extends Transaction {
        public Expense(double amount, String category, String date) {
            super("Expense", amount, category, date);
        }
    }

    // Validate and parse the amount entered by the user
    public static double validateAmount(String input) {
        try {
            double amount = Double.parseDouble(input);
            if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount entered");
        }
    }

    // Validate date format (dd-MM-yyyy) to ensure correct date format
    public static String validateDate(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);  // Ensure strict date parsing
        try {
            dateFormat.parse(input);  // Try to parse the input
            return input;  // If valid, return the date
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format, please enter date in dd-MM-yyyy format.");
        }
    }

    // Save transactions to a file, grouped by date, and also add closing balance
    public static void saveTransactionsToFile(String fileName, List<Transaction> transactions) {
        // Sort transactions by date in ascending order
        Collections.sort(transactions, new Comparator<Transaction>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Custom comparator to compare transaction dates
            @Override
            public int compare(Transaction t1, Transaction t2) {
                try {
                    return dateFormat.parse(t1.getDate()).compareTo(dateFormat.parse(t2.getDate()));
                } catch (ParseException e) {
                    return 0;  // If parsing fails, consider them equal
                }
            }
        });

        // Group transactions by date to handle multiple entries per day
        Map<String, List<Transaction>> groupedByDate = new LinkedHashMap<>();
        for (Transaction transaction : transactions) {
            groupedByDate.computeIfAbsent(transaction.getDate(), k -> new ArrayList<>()).add(transaction);
        }

        // Write transactions to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            double cumulativeBalance = 0; // Variable to keep track of closing balance

            // Iterate over each date group
            for (String date : groupedByDate.keySet()) {
                writer.write("Date: " + date + "\n");  // Write date as header
                writer.write("Serial No | Category | Type | Amount\n");
                writer.write("--------------------------------------\n");

                List<Transaction> dateTransactions = groupedByDate.get(date);
                
                // Write each transaction for the day and update cumulative balance
                for (int i = 0; i < dateTransactions.size(); i++) {
                    Transaction transaction = dateTransactions.get(i);
                    writer.write((i + 1) + " | " + transaction.getCategory() + " | " +
                            transaction.getType() + " | ₹" + transaction.getAmount() + "\n");

                    // Update the cumulative balance
                    if (transaction.getType().equals("Income")) {
                        cumulativeBalance += transaction.getAmount();
                    } else {
                        cumulativeBalance -= transaction.getAmount();
                    }
                }

                // Write the closing balance after processing all transactions for the day
                writer.write("\nClosing Balance: ₹" + cumulativeBalance + "\n");
                writer.write("\n");  // Separate different dates
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    // Load transactions from a file, parsing each line into transactions
    public static List<Transaction> loadTransactionsFromFile(String fileName) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Date: ")) {
                    // Skip the date line and continue reading the transactions for the date
                    String date = line.substring(6);
                    reader.readLine();  // Skip the header line
                    reader.readLine();  // Skip the separator line

                    // Read transactions for the current date
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        String[] parts = line.split(" \\| ");
                        if (parts.length == 4) {
                            String category = parts[1];
                            String type = parts[2];
                            double amount = Double.parseDouble(parts[3].substring(1));  // Remove ₹ symbol
                            // Create corresponding transaction object
                            Transaction transaction = type.equals("Income")
                                    ? new Income(amount, category, date)
                                    : new Expense(amount, category, date);
                            transactions.add(transaction);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Predefined categories for income and expense
    private static final List<String> INCOME_CATEGORIES = Arrays.asList("Salary", "Freelance", "Investments", "Other");
    private static final List<String> EXPENSE_CATEGORIES = Arrays.asList("Food", "Utilities", "Rent", "Entertainment", "Transport", "Other");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Transaction> transactions = new ArrayList<>();
        Map<String, List<Transaction>> categorizedTransactions = new HashMap<>();

        // Load existing transactions from file
        transactions = loadTransactionsFromFile("transactions.txt");

        while (true) {
            System.out.println("\nWelcome to Personal Finance Tracker");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Savings");
            System.out.println("4. View Transactions");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Adding income
                    System.out.print("Enter income amount: ");
                    double incomeAmount = validateAmount(scanner.nextLine());
                    System.out.println("Select income category: ");
                    for (int i = 0; i < INCOME_CATEGORIES.size(); i++) {
                        System.out.println((i + 1) + ". " + INCOME_CATEGORIES.get(i));
                    }
                    System.out.print("Enter category number: ");
                    int incomeCategoryChoice = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    if (incomeCategoryChoice < 0 || incomeCategoryChoice >= INCOME_CATEGORIES.size()) {
                        System.out.println("Invalid category choice.");
                        break;
                    }
                    String incomeCategory = INCOME_CATEGORIES.get(incomeCategoryChoice);
                    System.out.print("Enter date (dd-MM-yyyy): ");
                    String incomeDate = validateDate(scanner.nextLine());
                    transactions.add(new Income(incomeAmount, incomeCategory, incomeDate));
                    categorizedTransactions.computeIfAbsent(incomeCategory, k -> new ArrayList<>())
                            .add(new Income(incomeAmount, incomeCategory, incomeDate));
                    break;

                case 2:
                    // Adding expense
                    System.out.print("Enter expense amount: ");
                    double expenseAmount = validateAmount(scanner.nextLine());
                    System.out.println("Select expense category: ");
                    for (int i = 0; i < EXPENSE_CATEGORIES.size(); i++) {
                        System.out.println((i + 1) + ". " + EXPENSE_CATEGORIES.get(i));
                    }
                    System.out.print("Enter category number: ");
                    int expenseCategoryChoice = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    if (expenseCategoryChoice < 0 || expenseCategoryChoice >= EXPENSE_CATEGORIES.size()) {
                        System.out.println("Invalid category choice.");
                        break;
                    }
                    String expenseCategory = EXPENSE_CATEGORIES.get(expenseCategoryChoice);
                    System.out.print("Enter date (dd-MM-yyyy): ");
                    String expenseDate = validateDate(scanner.nextLine());
                    transactions.add(new Expense(expenseAmount, expenseCategory, expenseDate));
                    categorizedTransactions.computeIfAbsent(expenseCategory, k -> new ArrayList<>())
                            .add(new Expense(expenseAmount, expenseCategory, expenseDate));
                    break;

                case 3:
                    // View total savings
                    double incomeTotal = transactions.stream()
                            .filter(t -> t.getType().equals("Income"))
                            .mapToDouble(Transaction::getAmount).sum();
                    double expenseTotal = transactions.stream()
                            .filter(t -> t.getType().equals("Expense"))
                            .mapToDouble(Transaction::getAmount).sum();
                    double savings = incomeTotal - expenseTotal;
                    System.out.println("Your total savings: ₹" + savings);
                    break;

                case 4:
                    // View all transactions
                    System.out.println("\nTransactions:");
                    transactions.forEach(System.out::println);
                    break;

                case 5:
                    // Save transactions to file and exit
                    saveTransactionsToFile("transactions.txt", transactions);
                    System.out.println("Exiting the program...");
                    scanner.close();
                    return;

                default:
                    // Invalid choice
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
