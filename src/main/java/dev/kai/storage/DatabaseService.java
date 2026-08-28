package dev.kai.storage;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public abstract class DatabaseService {

    @Getter
    private final @NotNull String databasePath;
    private final @NotNull ExecutorService executorService;
    private Connection connection;

    public DatabaseService(final @NotNull String databasePath) {
        this.databasePath = databasePath;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) return;
        connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
        initialize();
    }

    public void disconnect() throws SQLException {
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (final @NotNull InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    protected abstract void initialize() throws SQLException;

    public final @NotNull <T> CompletableFuture<T> executeAsync(final @NotNull DatabaseTask<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.execute(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public final void executeAsync(final @NotNull DatabaseRunnable task) {
        CompletableFuture.runAsync(() -> {
            try {
                task.run(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    public interface DatabaseTask<T> {
        T execute(final @NotNull Connection connection) throws SQLException;
    }

    public interface DatabaseRunnable {
        void run(final @NotNull Connection connection) throws SQLException;
    }
}