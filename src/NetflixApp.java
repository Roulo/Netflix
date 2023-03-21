import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class NetflixApp extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/netflix?user=root&password=zhe5168ng";
    private static final String SELECT_USER = "SELECT * FROM utilisateurs WHERE nom_utilisateur=? AND mot_de_passe=?";
    private static final String INSERT_USER = "INSERT INTO utilisateurs(nom_utilisateur, mot_de_passe) VALUES (?, ?)";
    private static final String SELECT_VIDEOS = "SELECT id,titre, resume, teaser, duree, annee, realisateur, acteurs, categorie, est_vue, note FROM videos";

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
        JPanel videosPanel = new JPanel(new GridLayout(2, 3, 10, 10));

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

                JPanel videoPanel = new JPanel(new BorderLayout(10, 10));
                videoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                // Add image
                ImageIcon image = new ImageIcon("src/images/" + rs.getInt("id") + ".jpg");
                JLabel imageLabel = new JLabel(image);
                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Show video details in new frame
                        JFrame detailsFrame = new JFrame("Video Details");
                        detailsFrame.setLayout(new BorderLayout());

                        // Add image and description
                        JPanel detailsPanel = new JPanel(new BorderLayout());
                        JLabel detailsImageLabel = new JLabel(image);
                        JTextArea detailsTextArea = new JTextArea();
                        detailsTextArea.setLineWrap(true);
                        detailsTextArea.setWrapStyleWord(true);
                        detailsTextArea.setText(titre + "\n" + realisateur + ", " + categorie);
                        detailsPanel.add(detailsImageLabel, BorderLayout.CENTER);
                        detailsPanel.add(detailsTextArea, BorderLayout.SOUTH);

                        // Add watch and note buttons
                        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        JButton watchButton = new JButton("Watch");
                        JButton noteButton = new JButton("Note");
                        buttonsPanel.add(watchButton);
                        buttonsPanel.add(noteButton);

                        detailsFrame.add(detailsPanel, BorderLayout.CENTER);
                        detailsFrame.add(buttonsPanel, BorderLayout.SOUTH);
                        detailsFrame.pack();
                        detailsFrame.setLocationRelativeTo(null);
                        detailsFrame.setVisible(true);
                    }
                });
                videoPanel.add(imageLabel, BorderLayout.NORTH);

                // Add video info
                JPanel infoPanel = new JPanel(new GridLayout(4, 1));
                JLabel titleLabel = new JLabel(titre);
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                JLabel realisateurLabel = new JLabel(realisateur);
                realisateurLabel.setHorizontalAlignment(SwingConstants.CENTER);
                JLabel categorieLabel = new JLabel(categorie);
                categorieLabel.setHorizontalAlignment(SwingConstants.CENTER);
                JLabel anneeLabel = new JLabel(Integer.toString(annee));
                anneeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                infoPanel.add(titleLabel);
                infoPanel.add(realisateurLabel);
                infoPanel.add(categorieLabel);
                infoPanel.add(anneeLabel);
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