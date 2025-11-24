package turkishairlines;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.SpinnerDateModel;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.border.TitledBorder;

public class AirlineReservationGUI extends JFrame implements ActionListener {
    private JComboBox<String> fromComboBox, toComboBox;
    private JSpinner dateSpinner;
    private JTextField passengersField;
    private JRadioButton oneWayButton;
    private JRadioButton roundTripButton;
    private HashMap<String, Double> fareMap;
    private JComboBox<String> classComboBox;
    private boolean isLoggedIn = false;

    public AirlineReservationGUI() {
        setTitle("Havayolu Rezervasyon Sistemim");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Bilet Panelim (arka plan resmi)
        JPanel ticketPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("arka.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        ticketPanel.setLayout(new BorderLayout());

        JPanel bedirPanel = new JPanel();
        bedirPanel.setLayout(new BorderLayout());
        bedirPanel.setPreferredSize(new Dimension(1000, 250));
        bedirPanel.setOpaque(false);

        JPanel rightPanel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        String[] cities = {"İstanbul", "Nevsehir", "İzmir", "Paris", "Madrid", "Berlin"};
        fromComboBox = new JComboBox<>(cities);
        toComboBox = new JComboBox<>(cities);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2024);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date initialDate = calendar.getTime();

        SpinnerDateModel dateModel = new SpinnerDateModel(initialDate, null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateEditor.getTextField().setEditable(false);

        String[] classOptions = {"Economy", "Business"};
        classComboBox = new JComboBox<>(classOptions);

        passengersField = new JTextField("Yolcu Sayısı", 15);
        passengersField.setForeground(Color.GRAY);
        passengersField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passengersField.getText().equals("Yolcu Sayısı")) {
                    passengersField.setText("");
                    passengersField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (passengersField.getText().isEmpty()) {
                    passengersField.setForeground(Color.GRAY);
                    passengersField.setText("Yolcu Sayısı");
                }
            }
        });

        JButton searchButton = new JButton("Uçuş Ara");
        searchButton.addActionListener(this);
        searchButton.setBackground(Color.RED);
        searchButton.setForeground(Color.WHITE);

        inputPanel.add(fromComboBox);
        inputPanel.add(toComboBox);
        inputPanel.add(dateSpinner);
        inputPanel.add(passengersField);
        inputPanel.add(classComboBox);
        inputPanel.add(searchButton);

        fromComboBox.setPreferredSize(new Dimension(150, 40));
        toComboBox.setPreferredSize(new Dimension(150, 40));
        dateSpinner.setPreferredSize(new Dimension(150, 40));
        passengersField.setPreferredSize(new Dimension(150, 40));
        classComboBox.setPreferredSize(new Dimension(150, 40));
        searchButton.setPreferredSize(new Dimension(150, 40));

        oneWayButton = new JRadioButton("Tek Yön");
        roundTripButton = new JRadioButton("Gidiş-Dönüş");
        roundTripButton.setEnabled(false);
        oneWayButton.setSelected(true);

        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(roundTripButton);
        radioButtonGroup.add(oneWayButton);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioPanel.add(roundTripButton);
        radioPanel.add(oneWayButton);

        centerPanel.add(radioPanel, BorderLayout.NORTH);
        centerPanel.add(inputPanel, BorderLayout.CENTER);

        inputPanel.setPreferredSize(new Dimension(900, 100));
        radioPanel.setPreferredSize(new Dimension(800, 50));
        inputPanel.setBackground(Color.WHITE);
        radioPanel.setBackground(Color.WHITE);
        Font radioButtonFont = new Font("Arial", Font.PLAIN, 18);
        roundTripButton.setFont(radioButtonFont);
        oneWayButton.setFont(radioButtonFont);
        roundTripButton.setBackground(Color.WHITE);
        oneWayButton.setBackground(Color.WHITE);

        JPanel bottomPanel = new JPanel();

        bedirPanel.add(rightPanel, BorderLayout.EAST);
        bedirPanel.add(leftPanel, BorderLayout.WEST);
        bedirPanel.add(centerPanel, BorderLayout.CENTER);
        bedirPanel.add(bottomPanel, BorderLayout.SOUTH);

        leftPanel.setPreferredSize(new Dimension(350, 200));
        rightPanel.setPreferredSize(new Dimension(350, 200));
        leftPanel.setOpaque(false);
        rightPanel.setOpaque(false);

        ticketPanel.add(bedirPanel, BorderLayout.SOUTH);

        JPanel leftTopPanel = new JPanel();
        JPanel leftBottomPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(leftTopPanel, BorderLayout.NORTH);
        leftPanel.add(leftBottomPanel, BorderLayout.SOUTH);

        JPanel rightTopPanel = new JPanel();
        JPanel rightBottomPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

        rightBottomPanel.setPreferredSize(new Dimension(100, 200));
        leftBottomPanel.setPreferredSize(new Dimension(100, 200));
        leftTopPanel.setOpaque(false);
        rightTopPanel.setOpaque(false);

        centerPanel.setBackground(Color.WHITE);
        leftBottomPanel.setBackground(new Color(242, 242, 242));
        rightBottomPanel.setBackground(new Color(242, 242, 242));
        bottomPanel.setBackground(new Color(242, 242, 242));

        // Menü Paneli
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuPanel.setBackground(new Color(35, 43, 56));

        ImageIcon logoIcon = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel(logoIcon);

        JButton planButton = new JButton("PLANLA & UÇ");
        JButton helpButton = new JButton("YARDIM");
        JButton signUpButton = new JButton("ÜYE OL");
        JButton loginButton = new JButton("GİRİŞ YAP");

        JButton[] menuButtons = {planButton, helpButton, signUpButton, loginButton};
        for (JButton b : menuButtons) {
            b.setBackground(new Color(35, 43, 56));
            b.setForeground(Color.WHITE);
            b.setBorder(null);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setFont(new Font(b.getFont().getName(), Font.BOLD, b.getFont().getSize() + 2));
        }

        // SignUp Listener 
        class SignUpListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame signUpFrame = new JFrame("Üye Ol");
                JPanel signUpPanel = new JPanel(new GridLayout(5, 1, 5, 5));
                JTextField nameField = new JTextField("Adınız", 20);
                JTextField surnameField = new JTextField("Soyadınız", 20);
                JTextField emailField = new JTextField("E-posta", 20);
                JPasswordField passwordField = new JPasswordField(20);

                signUpPanel.add(nameField);
                signUpPanel.add(surnameField);
                signUpPanel.add(emailField);
                signUpPanel.add(passwordField);

                JButton registerButton = new JButton("Kayıt Ol");
                signUpPanel.add(registerButton);

                registerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        String name = nameField.getText().trim();
                        String surname = surnameField.getText().trim();
                        String email = emailField.getText().trim();
                        String password = new String(passwordField.getPassword());

                        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                            JOptionPane.showMessageDialog(signUpFrame, "Tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try {
                            // Basit hash (proje ortamında, production için BCrypt/Argon2 kullan)
                            String hashed = hashPassword(password);

                            UserDAO userDAO = new UserDAO();
                            boolean inserted = userDAO.insertUser(name, surname, email, hashed);
                            if (inserted) {
                                JOptionPane.showMessageDialog(signUpFrame, "Üyelik oluşturuldu!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                                signUpFrame.dispose();
                            } else {
                                JOptionPane.showMessageDialog(signUpFrame, "Kayıt başarısız. Aynı e-posta olabilir.", "Hata", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(signUpFrame, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                        } catch (NoSuchAlgorithmException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(signUpFrame, "Şifreleme hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                signUpFrame.add(signUpPanel);
                signUpFrame.pack();
                signUpFrame.setLocationRelativeTo(null);
                signUpFrame.setVisible(true);
            }
        }
        signUpButton.addActionListener(new SignUpListener());

        // Login Listener
        class LoginListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = new JFrame("Giriş Yap");
                JPanel loginPanel = new JPanel(new GridLayout(3, 1, 5, 5));
                JTextField emailField = new JTextField("E-posta", 20);
                JPasswordField passwordField = new JPasswordField(20);
                loginPanel.add(emailField);
                loginPanel.add(passwordField);

                JButton loginBtn = new JButton("Giriş Yap");
                loginPanel.add(loginBtn);

                loginBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        String email = emailField.getText().trim();
                        String password = new String(passwordField.getPassword());

                        if (email.isEmpty() || password.isEmpty()) {
                            JOptionPane.showMessageDialog(loginFrame, "E-posta ve şifre gerekli.", "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try {
                            String hashed = hashPassword(password);
                            UserDAO userDAO = new UserDAO();
                            boolean ok = userDAO.login(email, hashed);
                            if (ok) {
                                isLoggedIn = true;
                                JOptionPane.showMessageDialog(loginFrame, "Giriş başarılı!");
                                // Basit: menüye hoşgeldin label ekle
                                JLabel welcomeLabel = new JLabel("Hoşgeldiniz, " + email + "!");
                                welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                                welcomeLabel.setForeground(Color.WHITE);
                                menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
                                menuPanel.add(welcomeLabel);
                                menuPanel.revalidate();
                                menuPanel.repaint();

                                // Gizle butonları
                                signUpButton.setVisible(false);
                                loginButton.setVisible(false);
                                planButton.setVisible(false);

                                loginFrame.dispose();
                            } else {
                                JOptionPane.showMessageDialog(loginFrame, "Geçersiz e-posta veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(loginFrame, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                        } catch (NoSuchAlgorithmException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(loginFrame, "Şifreleme hatası.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                loginFrame.add(loginPanel);
                loginFrame.pack();
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
        }
        loginButton.addActionListener(new LoginListener());

        // Help Listener
        class HelpListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame helpFrame = new JFrame("Yardım");
                JPanel helpPanel = new JPanel(new GridLayout(4, 1, 5, 5));
                JLabel helpText = new JLabel("Destek almak için lütfen aşağıdaki formu doldurun:");
                helpPanel.add(helpText);
                JTextField feedbackField = new JTextField("Geri bildiriminizi buraya girin", 20);
                helpPanel.add(feedbackField);
                JButton sendFeedbackButton = new JButton("Geri Bildirim Gönder");
                helpPanel.add(sendFeedbackButton);

                sendFeedbackButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        helpFrame.dispose();
                        JOptionPane.showMessageDialog(null, "Geri bildiriminiz alındı, teşekkürler! Mail hesabınızı kontrol ediniz");
                    }
                });

                helpFrame.add(helpPanel);
                helpFrame.pack();
                helpFrame.setLocationRelativeTo(null);
                helpFrame.setVisible(true);
            }
        }
        helpButton.addActionListener(new HelpListener());

        menuPanel.add(logoLabel);
        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        menuPanel.add(planButton);
        menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        menuPanel.add(helpButton);
        menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        menuPanel.add(signUpButton);
        menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        menuPanel.add(loginButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(ticketPanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, BorderLayout.NORTH);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    // Inner class CityConverter (rota + ücretler). fareMap kullanımı sürdürüldü.
    public class CityConverter {
        private HashMap<String, String> cityPairsMap;

        public CityConverter() {
            cityPairsMap = new HashMap<>();
            fareMap = new HashMap<>();

            cityPairsMap.put("istanbul-nevsehir", "H3450");
            fareMap.put("H3450", 500.0);
            cityPairsMap.put("istanbul-izmir", "H3435");
            fareMap.put("H3435", 400.0);
            cityPairsMap.put("nevsehir-izmir", "H5035");
            fareMap.put("H5035", 600.0);
            cityPairsMap.put("nevsehir-istanbul", "H5034");
            fareMap.put("H5034", 500.0);
            cityPairsMap.put("izmir-istanbul", "H3534");
            fareMap.put("H3534", 400.0);
            cityPairsMap.put("izmir-nevsehir", "H3550");
            fareMap.put("H3550", 600.0);
            cityPairsMap.put("istanbul-berlin", "A3482");
            fareMap.put("A3482", 1000.0);
            cityPairsMap.put("berlin-istanbul", "A8234");
            fareMap.put("A8234", 1000.0);
            cityPairsMap.put("istanbul-paris", "A3483");
            fareMap.put("A3483", 1100.0);
            cityPairsMap.put("paris-istanbul", "A8334");
            fareMap.put("A8334", 1100.0);
            cityPairsMap.put("istanbul-madrid", "A3484");
            fareMap.put("A3484", 1200.0);
            cityPairsMap.put("madrid-istanbul", "A8434");
            fareMap.put("A8434", 1200.0);
        }
        public double getFare(String flywayId, boolean isBusiness) {
            double fare = fareMap.getOrDefault(flywayId, 0.0);
            if (isBusiness) fare *= 2;
            return fare;
        }
        public String convertToId(String from, String to) {
            String key = from.toLowerCase() + "-" + to.toLowerCase();
            return cityPairsMap.getOrDefault(key, "Unknown");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Uçuş Ara".equals(e.getActionCommand())) {
            String from = (String) fromComboBox.getSelectedItem();
            String to = (String) toComboBox.getSelectedItem();
            Date selectedDate = (Date) dateSpinner.getValue();
            String passengersText = passengersField.getText().trim();
            String flightClass = (String) classComboBox.getSelectedItem();

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "Nereden ve nereye aynı şehir olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Yolcu sayısı validasyonu
            int passengers;
            try {
                if (passengersText.isEmpty() || passengersText.equals("Yolcu Sayısı")) {
                    throw new NumberFormatException();
                }
                passengers = Integer.parseInt(passengersText);
                if (passengers <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Geçerli bir yolcu sayısı giriniz (pozitif tam sayı).", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CityConverter converter = new CityConverter();
            String flywayId = converter.convertToId(from, to);

            if (!"Unknown".equals(flywayId)) {
                // Uçuş bulunduğunda
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(selectedDate);
                String combinedValue = flywayId + formattedDate;

                // Database'e ekleme
                FlightDAO flightDAO = new FlightDAO();
                try {
                    boolean inserted = flightDAO.insertFlight(combinedValue, String.valueOf(passengers), flightClass);
                    if (!inserted) {
                        JOptionPane.showMessageDialog(this, "Uçuş veritabanına eklenemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Modern ödeme ekranı
                JFrame resultFrame = new JFrame("Ödeme Ekranı");
                resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultFrame.setSize(900, 520);
                resultFrame.setLayout(new BorderLayout());
                resultFrame.getContentPane().setBackground(new Color(238, 240, 245));

                // ANA PANEL
                JPanel mainPanel = new JPanel(new GridLayout(1, 2, 25, 0));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
                mainPanel.setBackground(new Color(238, 240, 245));

                // Kullanıcı Bilgileri
                JPanel leftCard = new JPanel();
                leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
                leftCard.setBackground(Color.WHITE);
                leftCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));

                JLabel userTitle = new JLabel("Kullanıcı Bilgileri");
                userTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
                userTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                leftCard.add(userTitle);
                leftCard.add(Box.createRigidArea(new Dimension(0, 15)));

                JTextField nameField = new JTextField();
                JTextField surnameField = new JTextField();
                JTextField phoneField = new JTextField();
                JTextField emailField = new JTextField();

                String[][] userFields = {
                        {"Ad:", ""},
                        {"Soyad:", ""},
                        {"Telefon:", ""},
                        {"E-posta:", ""}
                };

                JTextField[] userInputs = {nameField, surnameField, phoneField, emailField};

                for (int i = 0; i < userFields.length; i++) {
                    JPanel fieldPanel = new JPanel(new BorderLayout());
                    fieldPanel.setBackground(Color.WHITE);
                    JLabel lbl = new JLabel(userFields[i][0]);
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                    userInputs[i].setPreferredSize(new Dimension(200, 38));
                    userInputs[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));

                    fieldPanel.add(lbl, BorderLayout.NORTH);
                    fieldPanel.add(userInputs[i], BorderLayout.CENTER);
                    fieldPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                    leftCard.add(fieldPanel);
                }

                mainPanel.add(leftCard);

                // Kart Bilgileri Kartı
                JPanel rightCard = new JPanel();
                rightCard.setLayout(new BoxLayout(rightCard, BoxLayout.Y_AXIS));
                rightCard.setBackground(Color.WHITE);
                rightCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));

                JLabel payTitle = new JLabel("Kart Bilgileri");
                payTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
                payTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                rightCard.add(payTitle);
                rightCard.add(Box.createRigidArea(new Dimension(0, 15)));

                JTextField cardNameField = new JTextField();
                JTextField cardNumberField = new JTextField();
                JTextField cvvField = new JTextField();

                String[] months = new String[12];
                for (int i = 0; i < 12; i++) months[i] = String.format("%02d", i + 1);

                String[] years = new String[12];
                for (int i = 0; i < 12; i++) years[i] = String.valueOf(2024 + i);

                JComboBox<String> monthBox = new JComboBox<>(months);
                JComboBox<String> yearBox = new JComboBox<>(years);

                // Kart sahibinin adı
                JPanel cardNamePanel = new JPanel(new BorderLayout());
                cardNamePanel.setBackground(Color.WHITE);
                cardNamePanel.add(new JLabel("Kart Üzerindeki İsim:"), BorderLayout.NORTH);
                cardNameField.setPreferredSize(new Dimension(200, 38));
                cardNamePanel.add(cardNameField, BorderLayout.CENTER);
                cardNamePanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                rightCard.add(cardNamePanel);

                // Kart numarası
                JPanel cardNumberPanel = new JPanel(new BorderLayout());
                cardNumberPanel.setBackground(Color.WHITE);
                cardNumberPanel.add(new JLabel("Kart Numarası:"), BorderLayout.NORTH);
                cardNumberField.setPreferredSize(new Dimension(200, 38));
                cardNumberPanel.add(cardNumberField, BorderLayout.CENTER);
                cardNumberPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                rightCard.add(cardNumberPanel);

                // Tarih ve CVV alanı
                JPanel dateCvvPanel = new JPanel(new GridLayout(1, 3, 10, 0));
                dateCvvPanel.setBackground(Color.WHITE);

                JPanel datePanel = new JPanel(new GridLayout(2, 1));
                datePanel.setBackground(Color.WHITE);
                datePanel.add(new JLabel("Ay"));
                datePanel.add(monthBox);

                JPanel yearPanel = new JPanel(new GridLayout(2, 1));
                yearPanel.setBackground(Color.WHITE);
                yearPanel.add(new JLabel("Yıl"));
                yearPanel.add(yearBox);

                JPanel cvvPanel = new JPanel(new GridLayout(2, 1));
                cvvPanel.setBackground(Color.WHITE);
                cvvPanel.add(new JLabel("CVV"));
                cvvField.setPreferredSize(new Dimension(60, 38));
                cvvPanel.add(cvvField);

                dateCvvPanel.add(datePanel);
                dateCvvPanel.add(yearPanel);
                dateCvvPanel.add(cvvPanel);

                rightCard.add(dateCvvPanel);

                // Ödeme butonu
                JButton paymentButton = new JButton("Ödeme Yap");
                paymentButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
                paymentButton.setForeground(Color.WHITE);
                paymentButton.setBackground(new Color(52, 120, 246));
                paymentButton.setFocusPainted(false);
                paymentButton.setPreferredSize(new Dimension(180, 45));
                paymentButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                paymentButton.addActionListener(ev -> {
                    if (!isLoggedIn) {
                        JOptionPane.showMessageDialog(null, "Lütfen giriş yapın!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    double fare = fareMap.getOrDefault(flywayId, 0.0);
                    JOptionPane.showMessageDialog(resultFrame, "Ödeme başarılı! Ücret: " + fare + " TL");
                    resultFrame.dispose();
                });

                rightCard.add(Box.createRigidArea(new Dimension(0, 20)));
                rightCard.add(paymentButton);

                mainPanel.add(rightCard);
                resultFrame.add(mainPanel);
                resultFrame.setLocationRelativeTo(null);
                resultFrame.setVisible(true);
            }
             } 
    }
        
            
            


         
          private static String hashPassword(String password) throws NoSuchAlgorithmException {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         byte[] digest = md.digest(password.getBytes());
         StringBuilder sb = new StringBuilder();

         for (byte b : digest) {
             sb.append(String.format("%02x", b));
         }

         return sb.toString();
     }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirlineReservationGUI gui = new AirlineReservationGUI();
            gui.setVisible(true);
        });
    }
}
