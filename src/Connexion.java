import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Charger le pilote JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Se connecter à la base de données "netflix"
            String url = "jdbc:mysql://localhost:3306/netflix";
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);

            System.out.println("Connexion à la base de données réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Pilote JDBC introuvable : " + e.getMessage());
        } finally {
            // Fermer la connexion à la base de données
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("Connexion à la base de données fermée !");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion à la base de données : " + e.getMessage());
            }
        }
    }
}
