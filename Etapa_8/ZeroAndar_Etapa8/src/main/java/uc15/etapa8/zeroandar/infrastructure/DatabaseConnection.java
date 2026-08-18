/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 */
package uc15.etapa8.zeroandar.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fornece conexoes JDBC sem acoplar os DAOs a credenciais fixas.
 */
public final class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/zandarDB?useSSL=false&serverTimezone=America/Sao_Paulo";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(value("zeroandar.db.url", "ZEROANDAR_DB_URL", DEFAULT_URL), value("zeroandar.db.user", "ZEROANDAR_DB_USER", DEFAULT_USER), value("zeroandar.db.password", "ZEROANDAR_DB_PASSWORD", DEFAULT_PASSWORD));
    }

    public boolean testConnection() {
        try (Connection c = getConnection()) {
            return c.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    private static String value(String p, String e, String d) {
        String v = System.getProperty(p);
        if (v == null || v.isBlank()) {
            v = System.getenv(e);
        }
        return v == null ? d : v;
    }

    private static class Holder {

        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }
}
