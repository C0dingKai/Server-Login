package dev.kai.manager;

import dev.kai.LoginPlugin;
import dev.kai.model.Login;
import dev.kai.storage.dao.LoginDao;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * LoginManager
 *
 * @author Kai
 * @since 8/28/2026
 */
public final class LoginManager {

    private final @NotNull LoginDao loginDao = LoginPlugin.getInstance().getDatabaseManager().loginDao();

    public CompletableFuture<Void> register(final @NotNull Login login) {
        return loginDao.create(login);
    }

    public CompletableFuture<Login> find(final @NotNull UUID uuid) {
        return loginDao.find(uuid);
    }

    public CompletableFuture<Boolean> exist(final @NotNull UUID uuid) {
        return loginDao.find(uuid)
                .thenApply(login -> login != null);
    }

    public CompletableFuture<Void> delete(final UUID uuid) {
        return loginDao.delete(uuid);
    }
}