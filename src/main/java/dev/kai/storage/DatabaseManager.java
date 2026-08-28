package dev.kai.storage;

import dev.kai.storage.dao.LoginDao;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.CompletionException;

public record DatabaseManager(@NotNull LoginDao loginDao) {

    public DatabaseManager(final File dataFolder) {
        this(
                new LoginDao(new File(storage(dataFolder), "login.db").getAbsolutePath())
        );
    }

    public void connect() {
        try {
            loginDao.connect();
        } catch (final SQLException e) {
            throw new CompletionException(e);
        }
    }

    public void shutdown() {
        try {
            loginDao.disconnect();
        } catch (final SQLException e) {
            throw new CompletionException(e);
        }
    }

    private static @NotNull File storage(final @NotNull File dataFolder) {
        final File storage = new File(dataFolder, "storage");

        if (!storage.exists() && !storage.mkdirs()) {
            throw new IllegalStateException();
        }

        return storage;
    }
}