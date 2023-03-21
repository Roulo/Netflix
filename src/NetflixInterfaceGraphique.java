import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NetflixInterfaceGraphique extends JFrame {
    private JLabel labelNomUtilisateur;
    private JTextField champNomUtilisateur;
    private JLabel labelMotDePasse;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion;
    private JButton boutonCreationCompte;

    private JLabel labelTitre;
    private JLabel labelTeaser;
    private JLabel labelDescription;
    private JLabel labelDateSortie;
    private JLabel labelDuree;
    private JLabel labelGenre;
    private JLabel labelRealisateur;
    private JLabel labelActeurs;
    private JLabel labelNote;

    private Connection connexion;

    public NetflixInterfaceGraphique() {
        labelNomUtilisateur = new JLabel("Nom d'utilisateur:");
        champNomUtilisateur = new JTextField(20);
        labelMotDePasse = new JLabel("Mot de passe:");
        champMotDePasse = new JPasswordField(20);
        boutonConnexion = new JButton("Connexion");
        boutonCreationCompte = new JButton("Créer un compte");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/netflix?user=root&password=zhe5168ng");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boutonCreationCompte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nomUtilisateur = champNomUtilisateur.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                ajouterCompte(nomUtilisateur, motDePasse);
            }
        });

        boutonConnexion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nomUtilisateur = champNomUtilisateur.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                if (verifierCompte(nomUtilisateur, motDePasse)) {
                    // code pour connecter l'utilisateur

                } else {
                    // code pour afficher un message d'erreur
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(labelNomUtilisateur);
        panel.add(champNomUtilisateur);
        panel.add(labelMotDePasse);
        panel.add(champMotDePasse);
        panel.add(boutonConnexion);
        panel.add(boutonCreationCompte);
        this.add(panel);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void ajouterCompte(String nomUtilisateur, String motDePasse) {
        try {
            PreparedStatement statement = connexion.prepareStatement("INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe) VALUES (?, ?)");
            statement.setString(1, nomUtilisateur);
            statement.setString(2, motDePasse);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Le compte a été créé avec succès!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue lors de la création du compte.");
            e.printStackTrace();
        }
    }

    private boolean verifierCompte(String nomUtilisateur, String motDePasse) {
        try {
            PreparedStatement statement = connexion.prepareStatement("SELECT COUNT(*) AS count FROM utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?");
            statement.setString(1, nomUtilisateur);
            statement.setString(2, motDePasse);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt("count");
            return count == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    //affiche tout les films de la base de donnée, videos (titre, resume, teaser, duree, annee, realisateur, acteurs, categorie, est_vue, note)
    void afficherFilms() {

        try {
            PreparedStatement statement = connexion.prepareStatement("SELECT * FROM videos");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String titre = resultSet.getString("titre");
                String resume = resultSet.getString("resume");
                String teaser = resultSet.getString("teaser");
                int duree = resultSet.getInt("duree");
                int annee = resultSet.getInt("annee");
                String realisateur = resultSet.getString("realisateur");
                String acteurs = resultSet.getString("acteurs");
                String categorie = resultSet.getString("categorie");
                boolean estVue = resultSet.getBoolean("est_vue");
                int note = resultSet.getInt("note");

                //saute une ligne dans le jframe pour chaque film
                JLabel label = new JLabel(" ");
                this.add(label);
                labelTitre = new JLabel(titre);
                labelTeaser = new JLabel(teaser);
                labelDescription = new JLabel(resume);
                labelDateSortie = new JLabel(Integer.toString(annee));
                labelDuree = new JLabel(Integer.toString(duree));
                labelGenre = new JLabel(categorie);
                labelRealisateur = new JLabel(realisateur);
                labelActeurs = new JLabel(acteurs);
                labelNote = new JLabel(Integer.toString(note));

                JPanel panel = new JPanel();
                panel.add(labelTitre);
                panel.add(labelTeaser);
                panel.add(labelDescription);
                panel.add(labelDateSortie);
                panel.add(labelDuree);
                panel.add(labelGenre);
                panel.add(labelRealisateur);
                panel.add(labelActeurs);
                panel.add(labelNote);
                this.add(panel);
                this.pack();
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
