package com.hospital.db;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Shared SQLite connection manager for the hospital database.
 * Other teams must obtain connections through {@link #getConnection()} rather
 * than
 * opening their own JDBC connections directly.
 */
public final class DBConnection {

    private static final String DB_PATH = "hospital.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;
    private static final Object LOCK = new Object();

    private static Connection connection;

    private DBConnection() {
    }

    /**
     * Returns the singleton SQLite connection for the project database.
     * The connection is created lazily on the first call and reused afterward.
     */
    public static Connection getConnection() throws SQLException {
        synchronized (LOCK) {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL);
            }
            return connection;
        }
    }

    /**
     * Allows tests and local bootstrap code to use an explicit connection.
     * This is primarily for in-memory SQLite test databases.
     */
    public static void setConnection(Connection newConnection) throws SQLException {
        synchronized (LOCK) {
            if (connection != null && connection != newConnection && !connection.isClosed()) {
                connection.close();
            }
            connection = newConnection;
        }
    }

    /**
     * Initializes the schema from the classpath resource {@code db/schema.sql}.
     * The DDL uses {@code IF NOT EXISTS}, so repeated calls are safe.
     */
    public static void initializeSchema() throws SQLException {
        String sqlScript;
        try (InputStream inputStream = resolveSchemaInputStream()) {
            if (inputStream == null) {
                throw new SQLException("Unable to locate db/schema.sql on the classpath or project resources");
            }
            sqlScript = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SQLException("Unable to read db/schema.sql", e);
        }

        Connection conn = getConnection();
        try (Statement statement = conn.createStatement()) {
            for (String statementText : sqlScript.split(";")) {
                String trimmed = statementText.trim();
                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                }
            }
        }
    }

    private static InputStream resolveSchemaInputStream() throws SQLException {
        ClassLoader classLoader = DBConnection.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("db/schema.sql");
        if (inputStream != null) {
            return inputStream;
        }

        URL resourceUrl = classLoader.getResource("schema.sql");
        if (resourceUrl != null) {
            try {
                return Files.newInputStream(Path.of(resourceUrl.toURI()));
            } catch (IOException | URISyntaxException e) {
                throw new SQLException("Unable to open schema resource from URL", e);
            }
        }

        Path localPath = Path.of("src", "main", "resources", "db", "schema.sql");
        if (Files.exists(localPath)) {
            try {
                return Files.newInputStream(localPath);
            } catch (IOException e) {
                throw new SQLException("Unable to open schema file from project resources", e);
            }
        }

        return null;
    }

    /**
     * Closes the shared connection and clears the singleton reference.
     */
    public static void closeConnection() throws SQLException {
        synchronized (LOCK) {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            connection = null;
        }
    }
}
