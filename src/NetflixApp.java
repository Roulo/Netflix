import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NetflixApp extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/netflix?user=root&password=zhe5168ng";
    private static final String SELECT_USER = "SELECT * FROM utilisateurs WHERE nom_utilisateur=? AND mot_de_passe=?";
    private static final String INSERT_USER = "INSERT INTO utilisateurs(nom_utilisateur, mot_de_passe) VALUES (?, ?)";
    private static final String SELECT_VIDEOS = "SELECT titre, resume, teaser, duree, annee, realisateur, acteurs, categorie, est_vue, note FROM videos";

    private Connection conn;
    private PreparedStatement selectUserStmt;
    private PreparedStatement insertUserStmt;
    private PreparedStatement selectVideosStmt;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton connectButton;
    private JButton createButton;

    private JFrame mainFrame;
    private JLabel usernameLabel;
    private JButton disconnectButton;
    private JList<String> videosList;

    public NetflixApp() {
        super("Netflix App");

        try {
            conn = DriverManager.getConnection(DB_URL);
            selectUserStmt = conn.prepareStatement(SELECT_USER);
            insertUserStmt = conn.prepareStatement(INSERT_USER);
            selectVideosStmt = conn.prepareStatement(SELECT_VIDEOS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        connectButton = new JButton("Connect");
        createButton = new JButton("Create");

        connectButton.addActionListener(e -> {
            String nom_utilisateur = usernameField.getText();
            String mot_de_passe = new String(passwordField.getPassword());
            try {
                selectUserStmt.setString(1, nom_utilisateur);
                selectUserStmt.setString(2, mot_de_passe);
                ResultSet rs = selectUserStmt.executeQuery();
                if (rs.next()) {
                    showMainScreen(nom_utilisateur);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid nom_utilisateur or mot_de_passe");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        createButton.addActionListener(e -> {
            String nom_utilisateur = usernameField.getText();
            String mot_de_passe = new String(passwordField.getPassword());
            try {
                insertUserStmt.setString(1, nom_utilisateur);
                insertUserStmt.setString(2, mot_de_passe);
                insertUserStmt.executeUpdate();
                showMainScreen(nom_utilisateur);
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("username:"));
        panel.add(usernameField);
        panel.add(new JLabel("mdp:"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(connectButton);
        panel.add(new JLabel(""));
        panel.add(createButton);

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showMainScreen(String nom_utilisateur) {
        mainFrame = new JFrame("Netflix App - " + nom_utilisateur);
        usernameLabel = new JLabel(nom_utilisateur, SwingConstants.LEFT);
        disconnectButton = new JButton("Disconnect");
        JPanel videosPanel = new JPanel(new GridLayout(0, 3, 10, 10));

        disconnectButton.addActionListener(e -> {
            mainFrame.dispose();
            new NetflixApp();
        });

        try {
            ResultSet rs = selectVideosStmt.executeQuery();
            while (rs.next()) {
                String titre = rs.getString("titre");
                String resume = rs.getString("resume");
                String teaser = rs.getString("teaser");
                int duree = rs.getInt("duree");
                int annee = rs.getInt("annee");
                String realisateur = rs.getString("realisateur");
                String acteurs = rs.getString("acteurs");
                String categorie = rs.getString("categorie");
                boolean estVue = rs.getBoolean("est_vue");
                int note = rs.getInt("note");

                // Create a panel for the video item
                JPanel videoPanel = new JPanel(new BorderLayout(10, 10));

                // Add the image to the WEST position
                ImageIcon image = new ImageIcon("src/images/" + (rs.getRow() % 6 + 1) + ".jpg");
                JLabel imageLabel = new JLabel(image);
                videoPanel.add(imageLabel, BorderLayout.WEST);

                // Add the video information to the CENTER position
                JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
                infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
                JLabel titleLabel = new JLabel(titre, SwingConstants.CENTER);
                JLabel yearDirectorLabel = new JLabel(annee + " - " + realisateur, SwingConstants.CENTER);
                JLabel categoryLabel = new JLabel(categorie, SwingConstants.CENTER);
                infoPanel.add(titleLabel);
                infoPanel.add(yearDirectorLabel);
                infoPanel.add(categoryLabel);
                videoPanel.add(infoPanel, BorderLayout.CENTER);

                videosPanel.add(videoPanel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(usernameLabel);
        topPanel.add(disconnectButton);

        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(new JScrollPane(videosPanel), BorderLayout.CENTER);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}

