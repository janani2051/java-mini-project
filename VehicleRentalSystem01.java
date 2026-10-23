import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.Timer;
// ==================== PACKAGE STRUCTURE ====================
// Package: com.vehiclerental
// Classes: Vehicle, Customer, Rental, FileHandler,
//          LoginFrame, DashboardFrame, VehiclePanel,
//          RentalPanel, ReturnPanel

// ==================== Vehicle Class ====================
class Vehicle implements Serializable {
    private int vehicleId;
    private String vehicleName;
    private String type;       // Car / Bike / Van
    private String brand;
    private double ratePerDay;
    private boolean isAvailable;

    // Constructor
    public Vehicle(int vehicleId, String vehicleName, String type, String brand, double ratePerDay) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.type = type;
        this.brand = brand;
        this.ratePerDay = ratePerDay;
        this.isAvailable = true;
    }

    // Getters and Setters (Encapsulation)
    public int getVehicleId() { return vehicleId; }
    public String getVehicleName() { return vehicleName; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public double getRatePerDay() { return ratePerDay; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setRatePerDay(double ratePerDay) { this.ratePerDay = ratePerDay; }

    @Override
    public String toString() {
        return vehicleId + "," + vehicleName + "," + type + "," + brand + "," + ratePerDay + "," + isAvailable;
    }
}

// ==================== Customer Class ====================
class Customer {
    private int customerId;
    private String customerName;
    private String phone;
    private String address;

    public Customer(int customerId, String customerName, String phone, String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
    }

    public int getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return customerId + "," + customerName + "," + phone + "," + address;
    }
}

// ==================== Rental Class ====================
class Rental {
    private int rentalId;
    private int vehicleId;
    private int customerId;
    private String customerName;
    private String vehicleName;
    private String rentDate;
    private String returnDate;
    private int days;
    private double totalAmount;
    private double paidAmount;       // NEW - amount paid by customer
    private String paymentMode;      // NEW - Cash / Online
    private String paymentStatus;    // NEW - Full Paid / Partial
    private boolean isReturned;

    public Rental(int rentalId, int vehicleId, int customerId, String customerName,
                  String vehicleName, String rentDate, int days, double totalAmount,
                  double paidAmount, String paymentMode, String paymentStatus) {
        this.rentalId = rentalId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.vehicleName = vehicleName;
        this.rentDate = rentDate;
        this.days = days;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.paymentMode = paymentMode;
        this.paymentStatus = paymentStatus;
        this.isReturned = false;
    }

    public int getRentalId() { return rentalId; }
    public int getVehicleId() { return vehicleId; }
    public int getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getVehicleName() { return vehicleName; }
    public String getRentDate() { return rentDate; }
    public String getReturnDate() { return returnDate; }
    public int getDays() { return days; }
    public double getTotalAmount() { return totalAmount; }
    public double getPaidAmount() { return paidAmount; }
    public String getPaymentMode() { return paymentMode; }
    public String getPaymentStatus() { return paymentStatus; }
    public double getBalanceAmount() { return totalAmount - paidAmount; }
    public boolean isReturned() { return isReturned; }
    public void setReturned(boolean returned) { isReturned = returned; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    @Override
    public String toString() {
        return rentalId + "," + vehicleId + "," + customerId + "," + customerName + ","
                + vehicleName + "," + rentDate + "," + (returnDate != null ? returnDate : "N/A")
                + "," + days + "," + totalAmount + "," + paidAmount + ","
                + paymentMode + "," + paymentStatus + "," + isReturned;
    }
}

// ==================== FileHandler Class ====================
class FileHandler {
    private static final String VEHICLE_FILE = "vehicles.txt";
    private static final String RENTAL_FILE  = "rentals.txt";

    // Save all vehicles to file
    public static void saveVehicles(ArrayList<Vehicle> vehicles) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VEHICLE_FILE))) {
            for (Vehicle v : vehicles) pw.println(v.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving vehicles: " + e.getMessage());
        }
    }

    // Load vehicles from file
    public static ArrayList<Vehicle> loadVehicles() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        File f = new File(VEHICLE_FILE);
        if (!f.exists()) return vehicles;
        try (BufferedReader br = new BufferedReader(new FileReader(VEHICLE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Vehicle v = new Vehicle(Integer.parseInt(parts[0]), parts[1], parts[2],
                            parts[3], Double.parseDouble(parts[4]));
                    v.setAvailable(Boolean.parseBoolean(parts[5]));
                    vehicles.add(v);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    // Save all rentals to file
    public static void saveRentals(ArrayList<Rental> rentals) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RENTAL_FILE))) {
            for (Rental r : rentals) pw.println(r.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving rentals: " + e.getMessage());
        }
    }

    // Load rentals from file
    public static ArrayList<Rental> loadRentals() {
        ArrayList<Rental> rentals = new ArrayList<>();
        File f = new File(RENTAL_FILE);
        if (!f.exists()) return rentals;
        try (BufferedReader br = new BufferedReader(new FileReader(RENTAL_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // New format has 13 fields, old format had 10 — handle both
                if (parts.length >= 10) {
                    double paidAmt   = parts.length >= 13 ? Double.parseDouble(parts[9])  : Double.parseDouble(parts[8]);
                    String payMode   = parts.length >= 13 ? parts[10] : "Cash";
                    String payStat   = parts.length >= 13 ? parts[11] : "Full Paid";
                    boolean returned = parts.length >= 13 ? Boolean.parseBoolean(parts[12]) : Boolean.parseBoolean(parts[9]);
                    Rental r = new Rental(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]), parts[3], parts[4], parts[5],
                            Integer.parseInt(parts[7]), Double.parseDouble(parts[8]),
                            paidAmt, payMode, payStat);
                    r.setReturnDate(parts[6].equals("N/A") ? null : parts[6]);
                    r.setReturned(returned);
                    rentals.add(r);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading rentals: " + e.getMessage());
        }
        return rentals;
    }
}

// ==================== LoginFrame Class ====================
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    // Valid credentials
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    public LoginFrame() {
        setTitle("Vehicle Rental System - Login");
        setSize(450, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 32, 65),
                        0, getHeight(), new Color(26, 58, 110));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        // Title
        JLabel titleLabel = new JLabel("🚗 Vehicle Rental System", SwingConstants.CENTER);
        titleLabel.setBounds(50, 30, 350, 40);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 200, 50));

        JLabel subTitle = new JLabel("Admin Login", SwingConstants.CENTER);
        subTitle.setBounds(50, 70, 350, 25);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(new Color(180, 200, 255));

        // White card panel
        JPanel card = new JPanel(null);
        card.setBounds(50, 110, 350, 200);
        card.setBackground(new Color(255, 255, 255, 230));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230), 1));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 20, 100, 25);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(new Color(30, 50, 90));

        usernameField = new JTextField();
        usernameField.setBounds(130, 20, 190, 30);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 140, 200)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 100, 25);
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setForeground(new Color(30, 50, 90));

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 70, 190, 30);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 140, 200)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        loginButton = new JButton("LOGIN");
        loginButton.setBounds(100, 120, 150, 40);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(26, 82, 188));
        loginButton.setForeground(Color.black);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(userLabel); card.add(usernameField);
        card.add(passLabel); card.add(passwordField);
        card.add(loginButton);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setBounds(50, 320, 350, 25);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(255, 100, 100));

        JLabel hint = new JLabel("Hint: admin / admin123", SwingConstants.CENTER);
        hint.setBounds(50, 345, 350, 20);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(150, 170, 210));

        mainPanel.add(titleLabel); mainPanel.add(subTitle);
        mainPanel.add(card); mainPanel.add(statusLabel); mainPanel.add(hint);
        add(mainPanel);

        // Login action
        loginButton.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { loginButton.setBackground(new Color(15, 60, 150)); }
            public void mouseExited(MouseEvent e)  { loginButton.setBackground(new Color(26, 82, 188)); }
        });
    }

    private void performLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        try {
            if (user.isEmpty() || pass.isEmpty()) throw new Exception("Fields cannot be empty!");
            if (user.equals(USERNAME) && pass.equals(PASSWORD)) {
                statusLabel.setForeground(new Color(50, 200, 100));
                statusLabel.setText("Login successful! Loading...");
                Timer t = new Timer(800, ev -> {
                    dispose();
                    new DashboardFrame().setVisible(true);
                });
                t.setRepeats(false); t.start();
            } else {
                throw new Exception("Invalid username or password!");
            }
        } catch (Exception ex) {
            statusLabel.setForeground(new Color(255, 80, 80));
            statusLabel.setText(ex.getMessage());
        }
    }
}

// ==================== DashboardFrame (Main Window) ====================
class DashboardFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Rental>  rentals;

    public DashboardFrame() {
        vehicles = FileHandler.loadVehicles();
        rentals  = FileHandler.loadRentals();

        // Add sample vehicles if file is empty
        if (vehicles.isEmpty()) loadSampleVehicles();

        setTitle("Vehicle Rental System - Dashboard");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void loadSampleVehicles() {
        vehicles.add(new Vehicle(1, "Swift Dzire", "Car",  "Maruti",  1500.0));
        vehicles.add(new Vehicle(2, "Honda Activa","Bike", "Honda",    500.0));
        vehicles.add(new Vehicle(3, "Innova Crysta","Van", "Toyota",  2500.0));
        vehicles.add(new Vehicle(4, "Pulsar 150",  "Bike", "Bajaj",    600.0));
        vehicles.add(new Vehicle(5, "Creta",       "Car",  "Hyundai", 2000.0));
        FileHandler.saveVehicles(vehicles);
    }

    private void initComponents() {
        // Header
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 32, 65),
                        getWidth(), 0, new Color(26, 82, 188));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(950, 60));
        header.setLayout(new BorderLayout(10, 0));

        JLabel logo = new JLabel("  🚗  Vehicle Rental System");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(new Color(255, 200, 50));
        header.add(logo, BorderLayout.WEST);

        JLabel userInfo = new JLabel("Admin  |  ");
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userInfo.setForeground(new Color(180, 210, 255));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(200, 50, 50));
        logoutBtn.setForeground(Color.black);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(userInfo); rightPanel.add(logoutBtn);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(new Color(240, 245, 255));

        tabbedPane.addTab("🏠 Dashboard",  createDashboardPanel());
        tabbedPane.addTab("🚘 Vehicles",   new VehiclePanel(vehicles));
        tabbedPane.addTab("📋 Rent Vehicle", new RentalPanel(vehicles, rentals, this));
        tabbedPane.addTab("🔄 Return Vehicle", new ReturnPanel(vehicles, rentals, this));

        add(tabbedPane, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("  Vehicle Rental System © 2025 | Data saved to vehicles.txt & rentals.txt",
                SwingConstants.LEFT);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footer.setForeground(new Color(100, 120, 160));
        footer.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 245, 255));

        JLabel heading = new JLabel("Welcome, Admin!", SwingConstants.LEFT);
        heading.setBounds(30, 20, 400, 35);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(new Color(15, 40, 90));
        panel.add(heading);

        JLabel sub = new JLabel("Here's a quick overview of your rental system.", SwingConstants.LEFT);
        sub.setBounds(30, 55, 500, 22);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 120, 160));
        panel.add(sub);

        // Summary cards
        long totalVehicles   = vehicles.size();
        long available       = vehicles.stream().filter(Vehicle::isAvailable).count();
        long rented          = vehicles.stream().filter(v -> !v.isAvailable()).count();
        long activeRentals   = rentals.stream().filter(r -> !r.isReturned()).count();

        addSummaryCard(panel,  30, 100, "Total Vehicles",  String.valueOf(totalVehicles),
                new Color(26, 82, 188),  "🚗");
        addSummaryCard(panel, 230, 100, "Available",       String.valueOf(available),
                new Color(30, 160, 90),  "✅");
        addSummaryCard(panel, 430, 100, "Currently Rented",String.valueOf(rented),
                new Color(200, 120, 20), "🔑");
        addSummaryCard(panel, 630, 100, "Active Rentals",  String.valueOf(activeRentals),
                new Color(160, 30, 100), "📋");

        // Recent rentals table
        JLabel tableTitle = new JLabel("Recent Rental Records");
        tableTitle.setBounds(30, 280, 300, 28);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableTitle.setForeground(new Color(15, 40, 90));
        panel.add(tableTitle);

        String[] cols = {"Rental ID", "Customer", "Vehicle", "Date", "Days", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Rental r : rentals) {
            model.addRow(new Object[]{
                    r.getRentalId(), r.getCustomerName(), r.getVehicleName(),
                    r.getRentDate(), r.getDays(),
                    "₹" + String.format("%.0f", r.getTotalAmount()),
                    r.isReturned() ? "Returned" : "Active"
            });
        }

        JTable table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(30, 315, 870, 230);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));
        panel.add(scroll);

        return panel;
    }

    private void addSummaryCard(JPanel parent, int x, int y, String title, String value,
                                Color color, String icon) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, color,
                        getWidth(), getHeight(), color.darker());
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
        };
        card.setBounds(x, y, 180, 110);
        card.setOpaque(false);

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setBounds(0, 10, 180, 35);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setBounds(0, 45, 180, 32);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLabel.setForeground(Color.black);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setBounds(0, 78, 180, 22);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(220, 235, 255));

        card.add(iconLabel); card.add(valLabel); card.add(titleLabel);
        parent.add(card);
    }

    static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(26, 82, 188));
        table.getTableHeader().setForeground(Color.black);
        table.setSelectionBackground(new Color(180, 210, 255));
        table.setGridColor(new Color(210, 220, 240));
        table.setShowGrid(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 255));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    public void refreshDashboard() {
        tabbedPane.setComponentAt(0, createDashboardPanel());
    }

    public ArrayList<Vehicle> getVehicles() { return vehicles; }
    public ArrayList<Rental>  getRentals()  { return rentals; }
}

// ==================== VehiclePanel Class ====================
class VehiclePanel extends JPanel {
    private ArrayList<Vehicle> vehicles;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, brandField, rateField;
    private JComboBox<String> typeCombo;

    public VehiclePanel(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        loadTable();
    }

    private void initComponents() {
        // Form Panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.white);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 188), 1),
                "  Add / Update Vehicle  ",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(26, 82, 188)));
        formPanel.setPreferredSize(new Dimension(0, 160));

        addFormLabel(formPanel, "Vehicle ID:",   10,  25);
        addFormLabel(formPanel, "Name:",        200,  25);
        addFormLabel(formPanel, "Type:",        400,  25);
        addFormLabel(formPanel, "Brand:",       580,  25);
        addFormLabel(formPanel, "Rate/Day (₹):",10,  85);

        idField   = addFormField(formPanel,  90,  20, 90,  30);
        nameField = addFormField(formPanel, 245,  20, 135, 30);
        typeCombo = new JComboBox<>(new String[]{"Car", "Bike", "Van", "Truck"});
        typeCombo.setBounds(445, 20, 120, 30);
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        brandField = addFormField(formPanel, 635,  20, 130, 30);
        rateField  = addFormField(formPanel, 120,  80, 130, 30);
        formPanel.add(typeCombo);

        JButton addBtn    = createButton("➕ Add",    new Color(26, 82, 188));
        JButton updateBtn = createButton("✏️ Update",  new Color(50, 150, 80));
        JButton deleteBtn = createButton("🗑 Delete",  new Color(190, 50, 50));
        JButton clearBtn  = createButton("🧹 Clear",   new Color(120, 120, 120));

        addBtn.setBounds(300, 70, 110, 35);
        updateBtn.setBounds(420, 70, 120, 35);
        deleteBtn.setBounds(550, 70, 120, 35);
        clearBtn.setBounds(680, 70, 100, 35);

        formPanel.add(addBtn); formPanel.add(updateBtn);
        formPanel.add(deleteBtn); formPanel.add(clearBtn);

        // Table
        String[] cols = {"ID", "Vehicle Name", "Type", "Brand", "Rate/Day (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        DashboardFrame.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        JLabel title = new JLabel("Vehicle Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(15, 40, 90));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        add(title, BorderLayout.NORTH);
        add(formPanel, BorderLayout.NORTH);  // layered - use BoxLayout instead
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(formPanel, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Actions
        addBtn.addActionListener(e -> addVehicle());
        updateBtn.addActionListener(e -> updateVehicle());
        deleteBtn.addActionListener(e -> deleteVehicle());
        clearBtn.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                typeCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                brandField.setText(tableModel.getValueAt(row, 3).toString());
                rateField.setText(tableModel.getValueAt(row, 4).toString().replace("₹", "").trim());
            }
        });
    }

    private JLabel addFormLabel(JPanel p, String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 100, 25);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(40, 60, 100));
        p.add(l); return l;
    }

    private JTextField addFormField(JPanel p, int x, int y, int w, int h) {
        JTextField f = new JTextField();
        f.setBounds(x, y, w, h);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        p.add(f); return f;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Vehicle v : vehicles) {
            tableModel.addRow(new Object[]{
                    v.getVehicleId(), v.getVehicleName(), v.getType(),
                    v.getBrand(), "₹" + v.getRatePerDay(),
                    v.isAvailable() ? "Available" : "Rented"
            });
        }
    }

    private void addVehicle() {
        try {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() ||
                brandField.getText().isEmpty() || rateField.getText().isEmpty())
                throw new Exception("All fields are required!");

            int id = Integer.parseInt(idField.getText().trim());
            for (Vehicle v : vehicles) {
                if (v.getVehicleId() == id) throw new Exception("Vehicle ID already exists!");
            }
            double rate = Double.parseDouble(rateField.getText().trim());
            if (rate <= 0) throw new Exception("Rate must be positive!");

            Vehicle v = new Vehicle(id, nameField.getText().trim(),
                    typeCombo.getSelectedItem().toString(),
                    brandField.getText().trim(), rate);
            vehicles.add(v);
            FileHandler.saveVehicles(vehicles);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Vehicle added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Rate must be numbers!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        try {
            if (idField.getText().isEmpty()) throw new Exception("Select a vehicle to update!");
            int id = Integer.parseInt(idField.getText().trim());
            for (Vehicle v : vehicles) {
                if (v.getVehicleId() == id) {
                    v.setVehicleName(nameField.getText().trim());
                    v.setBrand(brandField.getText().trim());
                    v.setRatePerDay(Double.parseDouble(rateField.getText().trim()));
                    FileHandler.saveVehicles(vehicles);
                    loadTable(); clearFields();
                    JOptionPane.showMessageDialog(this, "Vehicle updated!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            throw new Exception("Vehicle not found!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        try {
            if (idField.getText().isEmpty()) throw new Exception("Select a vehicle to delete!");
            int id = Integer.parseInt(idField.getText().trim());
            vehicles.removeIf(v -> v.getVehicleId() == id);
            FileHandler.saveVehicles(vehicles);
            loadTable(); clearFields();
            JOptionPane.showMessageDialog(this, "Vehicle deleted!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText(""); nameField.setText("");
        brandField.setText(""); rateField.setText("");
        typeCombo.setSelectedIndex(0);
        table.clearSelection();
    }
}

// ==================== RentalPanel Class ====================
class RentalPanel extends JPanel {
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Rental>  rentals;
    private DashboardFrame     dashboard;

    private JTextField custNameField, custPhoneField, custAddrField, daysField, paidAmountField;
    private JComboBox<String> vehicleCombo, paymentModeCombo, paymentStatusCombo;
    private JLabel  rateLabel, totalLabel, balanceLabel;
    private DefaultTableModel tableModel;

    public RentalPanel(ArrayList<Vehicle> vehicles, ArrayList<Rental> rentals,
                       DashboardFrame dashboard) {
        this.vehicles  = vehicles;
        this.rentals   = rentals;
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        loadRentalTable();
    }

    private void initComponents() {
        JLabel title = new JLabel("Rent a Vehicle");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(15, 40, 90));

        // Form
        JPanel form = new JPanel(null);
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 188)),
                "  Rental Details  ", 0, 0,
                new Font("Segoe UI", Font.BOLD, 13), new Color(26, 82, 188)));
        form.setPreferredSize(new Dimension(0, 260));

        // Row 1
        addLabel(form, "Customer Name:", 10, 28);
        custNameField = addField(form, 140, 25, 160, 30);

        addLabel(form, "Phone:", 320, 28);
        custPhoneField = addField(form, 380, 25, 130, 30);

        addLabel(form, "Address:", 530, 28);
        custAddrField = addField(form, 600, 25, 170, 30);

        // Row 2
        addLabel(form, "Select Vehicle:", 10, 90);
        vehicleCombo = new JComboBox<>();
        vehicleCombo.setBounds(130, 85, 220, 32);
        vehicleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        refreshVehicleCombo();
        form.add(vehicleCombo);

        addLabel(form, "Rate/Day:", 370, 90);
        rateLabel = new JLabel("₹0.00");
        rateLabel.setBounds(450, 85, 100, 30);
        rateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rateLabel.setForeground(new Color(26, 82, 188));
        form.add(rateLabel);

        addLabel(form, "Number of Days:", 570, 90);
        daysField = addField(form, 700, 85, 70, 30);

        // Row 3 - Payment Details
        addLabel(form, "Payment Mode:", 10, 150);
        paymentModeCombo = new JComboBox<>(new String[]{"Cash", "Online (UPI)", "Online (Card)", "Bank Transfer"});
        paymentModeCombo.setBounds(130, 147, 160, 30);
        paymentModeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(paymentModeCombo);

        addLabel(form, "Paid Amount (₹):", 310, 150);
        paidAmountField = addField(form, 450, 147, 120, 30);

        addLabel(form, "Payment Status:", 590, 150);
        paymentStatusCombo = new JComboBox<>(new String[]{"Full Paid", "Partial Payment"});
        paymentStatusCombo.setBounds(710, 147, 150, 30);
        paymentStatusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(paymentStatusCombo);

        // Row 4 - Total / Balance / Buttons
        addLabel(form, "Total Amount:", 10, 210);
        totalLabel = new JLabel("₹0.00");
        totalLabel.setBounds(130, 205, 130, 30);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(30, 150, 80));
        form.add(totalLabel);

        addLabel(form, "Balance Due:", 270, 210);
        balanceLabel = new JLabel("₹0.00");
        balanceLabel.setBounds(370, 205, 130, 30);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(200, 60, 60));
        form.add(balanceLabel);

        JButton calcBtn = createBtn("Calculate", new Color(100, 100, 200));
        calcBtn.setBounds(520, 205, 120, 35);
        JButton rentBtn = createBtn("✅ Confirm Rental", new Color(26, 82, 188));
        rentBtn.setBounds(650, 205, 160, 35);
        JButton clearBtn = createBtn("Clear", new Color(120, 120, 120));
        clearBtn.setBounds(820, 205, 90, 35);
        form.add(calcBtn); form.add(rentBtn); form.add(clearBtn);

        // Table
        String[] cols = {"Rental ID", "Customer", "Phone", "Vehicle", "Date", "Days", "Total", "Paid", "Balance", "Mode", "Pay Status", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        DashboardFrame.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Action listeners
        vehicleCombo.addActionListener(e -> updateRate());
        calcBtn.addActionListener(e -> calculateTotal());
        rentBtn.addActionListener(e -> confirmRental());
        clearBtn.addActionListener(e -> clearForm());
        // Auto update balance when paid amount changes
        paidAmountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updateBalance(); }
        });
        // Auto set payment status based on paid vs total
        paidAmountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                updateBalance();
                try {
                    double paid  = Double.parseDouble(paidAmountField.getText().trim());
                    double total = Double.parseDouble(totalLabel.getText().replace("₹","").trim());
                    paymentStatusCombo.setSelectedItem(paid >= total ? "Full Paid" : "Partial Payment");
                } catch (Exception ex) { /* ignore */ }
            }
        });
    }

    private void refreshVehicleCombo() {
        vehicleCombo.removeAllItems();
        for (Vehicle v : vehicles) {
            if (v.isAvailable()) {
                vehicleCombo.addItem(v.getVehicleId() + " - " + v.getVehicleName()
                        + " (" + v.getType() + ")");
            }
        }
    }

    private void updateRate() {
        Vehicle v = getSelectedVehicle();
        if (v != null) rateLabel.setText("₹" + v.getRatePerDay());
    }

    private void calculateTotal() {
        try {
            Vehicle v = getSelectedVehicle();
            if (v == null) throw new Exception("No vehicle selected!");
            int days = Integer.parseInt(daysField.getText().trim());
            if (days <= 0) throw new Exception("Days must be greater than 0!");
            double total = v.getRatePerDay() * days;
            totalLabel.setText("₹" + String.format("%.2f", total));
            // Auto fill paid amount with total if empty
            if (paidAmountField.getText().isEmpty()) {
                paidAmountField.setText(String.format("%.2f", total));
                paymentStatusCombo.setSelectedItem("Full Paid");
            }
            updateBalance();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid number of days!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBalance() {
        try {
            double total = Double.parseDouble(totalLabel.getText().replace("₹","").trim());
            double paid  = Double.parseDouble(paidAmountField.getText().trim());
            double bal   = total - paid;
            balanceLabel.setText("₹" + String.format("%.2f", bal));
            balanceLabel.setForeground(bal > 0 ? new Color(200, 60, 60) : new Color(30, 150, 80));
        } catch (Exception ex) {
            balanceLabel.setText("₹0.00");
        }
    }

    private void confirmRental() {
        try {
            if (custNameField.getText().isEmpty() || custPhoneField.getText().isEmpty()
                    || daysField.getText().isEmpty() || paidAmountField.getText().isEmpty())
                throw new Exception("All fields are required!");
            Vehicle v = getSelectedVehicle();
            if (v == null) throw new Exception("No available vehicle selected!");
            int days = Integer.parseInt(daysField.getText().trim());
            if (days <= 0) throw new Exception("Days must be greater than 0!");
            double total    = v.getRatePerDay() * days;
            double paid     = Double.parseDouble(paidAmountField.getText().trim());
            if (paid < 0)   throw new Exception("Paid amount cannot be negative!");
            if (paid > total) throw new Exception("Paid amount cannot exceed total amount!");
            String payMode  = paymentModeCombo.getSelectedItem().toString();
            String payStat  = paymentStatusCombo.getSelectedItem().toString();
            double balance  = total - paid;

            int customerId = rentals.size() + 100;
            int rentalId   = rentals.size() + 1;
            String date    = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            Rental rental = new Rental(rentalId, v.getVehicleId(), customerId,
                    custNameField.getText().trim() + "|" + custPhoneField.getText().trim(),
                    v.getVehicleName(), date, days, total, paid, payMode, payStat);
            rentals.add(rental);
            v.setAvailable(false);
            FileHandler.saveVehicles(vehicles);
            FileHandler.saveRentals(rentals);

            // Show receipt
            String receipt = "===== RENTAL RECEIPT =====\n"
                    + "Rental ID      : " + rentalId + "\n"
                    + "Customer       : " + custNameField.getText() + "\n"
                    + "Phone          : " + custPhoneField.getText() + "\n"
                    + "Vehicle        : " + v.getVehicleName() + " (" + v.getType() + ")\n"
                    + "Rate/Day       : ₹" + v.getRatePerDay() + "\n"
                    + "Days           : " + days + "\n"
                    + "Date           : " + date + "\n"
                    + "--------------------------\n"
                    + "Total Amount   : ₹" + String.format("%.2f", total) + "\n"
                    + "Payment Mode   : " + payMode + "\n"
                    + "Amount Paid    : ₹" + String.format("%.2f", paid) + "\n"
                    + "Balance Due    : ₹" + String.format("%.2f", balance) + "\n"
                    + "Payment Status : " + payStat + "\n"
                    + "==========================";
            JOptionPane.showMessageDialog(this, receipt, "Rental Confirmed ✅",
                    JOptionPane.INFORMATION_MESSAGE);

            loadRentalTable(); refreshVehicleCombo();
            clearForm();
            dashboard.refreshDashboard();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers in amount fields!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRentalTable() {
        tableModel.setRowCount(0);
        for (Rental r : rentals) {
            String[] nameParts = r.getCustomerName().split("\\|");
            String name  = nameParts[0];
            String phone = nameParts.length > 1 ? nameParts[1] : "N/A";
            tableModel.addRow(new Object[]{
                    r.getRentalId(), name, phone, r.getVehicleName(),
                    r.getRentDate(), r.getDays(),
                    "₹" + String.format("%.2f", r.getTotalAmount()),
                    "₹" + String.format("%.2f", r.getPaidAmount()),
                    "₹" + String.format("%.2f", r.getBalanceAmount()),
                    r.getPaymentMode(), r.getPaymentStatus(),
                    r.isReturned() ? "Returned" : "Active"
            });
        }
    }

    private void clearForm() {
        custNameField.setText(""); custPhoneField.setText("");
        custAddrField.setText(""); daysField.setText("");
        paidAmountField.setText("");
        totalLabel.setText("₹0.00"); rateLabel.setText("₹0.00");
        balanceLabel.setText("₹0.00");
        paymentModeCombo.setSelectedIndex(0);
        paymentStatusCombo.setSelectedIndex(0);
    }

    private Vehicle getSelectedVehicle() {
        if (vehicleCombo.getSelectedItem() == null) return null;
        String sel = vehicleCombo.getSelectedItem().toString();
        int id = Integer.parseInt(sel.split(" - ")[0].trim());
        for (Vehicle v : vehicles) if (v.getVehicleId() == id) return v;
        return null;
    }

    private void addLabel(JPanel p, String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 130, 25);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(40, 60, 100));
        p.add(l);
    }

    private JTextField addField(JPanel p, int x, int y, int w, int h) {
        JTextField f = new JTextField();
        f.setBounds(x, y, w, h);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        p.add(f); return f;
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

// ==================== ReturnPanel Class ====================
class ReturnPanel extends JPanel {
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Rental>  rentals;
    private DashboardFrame     dashboard;
    private DefaultTableModel  tableModel;
    private JTextField         rentalIdField;
    private JLabel             detailLabel;

    public ReturnPanel(ArrayList<Vehicle> vehicles, ArrayList<Rental> rentals,
                       DashboardFrame dashboard) {
        this.vehicles  = vehicles;
        this.rentals   = rentals;
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        loadReturnTable();
    }

    private void initComponents() {
        JLabel title = new JLabel("Return Vehicle");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(15, 40, 90));

        JPanel form = new JPanel(null);
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 188)),
                "  Process Return  ", 0, 0,
                new Font("Segoe UI", Font.BOLD, 13), new Color(26, 82, 188)));
        form.setPreferredSize(new Dimension(0, 130));

        JLabel idLabel = new JLabel("Rental ID:");
        idLabel.setBounds(20, 30, 90, 25);
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(new Color(40, 60, 100));
        form.add(idLabel);

        rentalIdField = new JTextField();
        rentalIdField.setBounds(115, 27, 120, 32);
        rentalIdField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rentalIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        form.add(rentalIdField);

        JButton searchBtn  = createBtn("🔍 Search",      new Color(80, 100, 180));
        JButton returnBtn  = createBtn("✅ Process Return", new Color(30, 150, 80));
        searchBtn.setBounds(255, 27, 130, 35);
        returnBtn.setBounds(400, 27, 170, 35);
        form.add(searchBtn); form.add(returnBtn);

        detailLabel = new JLabel("Enter Rental ID and click Search to view details.");
        detailLabel.setBounds(20, 75, 750, 40);
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailLabel.setForeground(new Color(80, 100, 140));
        form.add(detailLabel);

        // Table - active rentals
        String[] cols = {"Rental ID", "Customer", "Vehicle", "Rent Date", "Days", "Total", "Due Return", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        DashboardFrame.styleTable(table);

        // Click table to auto-fill
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                rentalIdField.setText(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
                searchBtn.doClick();
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        JLabel tableTitle = new JLabel("Active Rentals (click a row to select)");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableTitle.setForeground(new Color(15, 40, 90));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(tableTitle, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> searchRental());
        returnBtn.addActionListener(e -> processReturn());
    }

    private void searchRental() {
        try {
            int id = Integer.parseInt(rentalIdField.getText().trim());
            for (Rental r : rentals) {
                if (r.getRentalId() == id) {
                    if (r.isReturned()) {
                        detailLabel.setForeground(new Color(180, 60, 60));
                        detailLabel.setText("Rental #" + id + " has already been returned.");
                    } else {
                        detailLabel.setForeground(new Color(30, 130, 70));
                        detailLabel.setText("✅ Found | Customer: " + r.getCustomerName().split("\\|")[0]
                                + "  | Vehicle: " + r.getVehicleName()
                                + "  | Rented: " + r.getRentDate()
                                + "  | Days: " + r.getDays()
                                + "  | Total: ₹" + String.format("%.2f", r.getTotalAmount())
                                + "  | Paid: ₹" + String.format("%.2f", r.getPaidAmount())
                                + "  | Balance: ₹" + String.format("%.2f", r.getBalanceAmount())
                                + "  | Mode: " + r.getPaymentMode());
                    }
                    return;
                }
            }
            detailLabel.setForeground(new Color(180, 60, 60));
            detailLabel.setText("No rental found with ID: " + id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid Rental ID!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processReturn() {
        try {
            int id = Integer.parseInt(rentalIdField.getText().trim());
            for (Rental r : rentals) {
                if (r.getRentalId() == id) {
                    if (r.isReturned()) {
                        JOptionPane.showMessageDialog(this, "Already returned!", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    String returnDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    r.setReturnDate(returnDate);
                    r.setReturned(true);
                    // Free the vehicle
                    for (Vehicle v : vehicles) {
                        if (v.getVehicleId() == r.getVehicleId()) { v.setAvailable(true); break; }
                    }
                    FileHandler.saveRentals(rentals);
                    FileHandler.saveVehicles(vehicles);

                    String msg = "===== RETURN RECEIPT =====\n"
                            + "Rental ID      : " + r.getRentalId() + "\n"
                            + "Customer       : " + r.getCustomerName().split("\\|")[0] + "\n"
                            + "Vehicle        : " + r.getVehicleName() + "\n"
                            + "Rent Date      : " + r.getRentDate() + "\n"
                            + "Return Date    : " + returnDate + "\n"
                            + "Days           : " + r.getDays() + "\n"
                            + "--------------------------\n"
                            + "Total Amount   : ₹" + String.format("%.2f", r.getTotalAmount()) + "\n"
                            + "Amount Paid    : ₹" + String.format("%.2f", r.getPaidAmount()) + "\n"
                            + "Balance Cleared: ₹" + String.format("%.2f", r.getBalanceAmount()) + "\n"
                            + "Payment Mode   : " + r.getPaymentMode() + "\n"
                            + "==========================\nThank you!";
                    JOptionPane.showMessageDialog(this, msg, "Return Successful ✅",
                            JOptionPane.INFORMATION_MESSAGE);

                    loadReturnTable();
                    rentalIdField.setText("");
                    detailLabel.setText("Return processed successfully!");
                    detailLabel.setForeground(new Color(30, 130, 70));
                    dashboard.refreshDashboard();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Rental ID not found!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid Rental ID!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReturnTable() {
        tableModel.setRowCount(0);
        for (Rental r : rentals) {
            if (!r.isReturned()) {
                tableModel.addRow(new Object[]{
                        r.getRentalId(), r.getCustomerName(), r.getVehicleName(),
                        r.getRentDate(), r.getDays(),
                        "₹" + String.format("%.2f", r.getTotalAmount()),
                        "After " + r.getDays() + " days", "Active 🔑"
                });
            }
        }
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

// ==================== Main Entry Point ====================
public class VehicleRentalSystem01{
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TabbedPane.selected",       new Color(26, 82, 188));
            UIManager.put("TabbedPane.foreground",     new Color(30, 50, 90));
            UIManager.put("TabbedPane.background",     new Color(240, 245, 255));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
