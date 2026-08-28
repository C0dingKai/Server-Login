package dev.kai.storage.dao;

import dev.kai.model.Login;
import dev.kai.storage.DatabaseService;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * LoginDao
 *
 * @author Kai
 * @since 8/28/2026
 */
public class LoginDao extends DatabaseService {

    public LoginDao(final @NotNull String databasePath) {
        super(databasePath);
    }

    @Override
    protected void initialize() throws SQLException {
        try (final Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS login (
                    uuid VARCHAR(36) PRIMARY KEY,
                    password VARCHAR(255) NOT NULL
                )
            """);
        }
    }

    public CompletableFuture<Void> create(final @NotNull Login login) {
        return executeAsync(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO login (uuid, password)
                VALUES (?, ?)
                """)) {

                stmt.setString(1, login.uuid().toString());
                stmt.setString(2, login.password());

                stmt.executeUpdate();
            }

            return null;
        });
    }

    public CompletableFuture<Login> find(final @NotNull UUID uuid) {
        return executeAsync(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement("""
                SELECT uuid, password
                FROM login
                WHERE uuid = ?
                """)) {

                stmt.setString(1, uuid.toString());

                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    return new Login(
                            UUID.fromString(resultSet.getString("uuid")),
                            resultSet.getString("password")
                    );
                }
            }
        });
    }

    public CompletableFuture<Void> delete(final @NotNull UUID uuid) {
        return executeAsync(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement("""
                DELETE FROM login
                WHERE uuid = ?
                """)) {

                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
            }

            return null;
        });
    }
}