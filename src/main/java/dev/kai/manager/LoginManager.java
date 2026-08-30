package dev.kai.manager;

import dev.kai.model.LoginHolder;
import dev.kai.storage.DatabaseManager;
import dev.kai.storage.provider.LoginDatabaseProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * LoginManager
 *
 * @author Kai
 * @since 8/28/2026
 */
public final class LoginManager {

    private final @NotNull LoginDatabaseProvider loginDatabaseProvider;

    public LoginManager() {
        this.loginDatabaseProvider = (LoginDatabaseProvider) DatabaseManager.getInstance().getProvider(LoginHolder.class);
    }

    public CompletableFuture<Void> register(final @NotNull LoginHolder loginHolder) {
        return CompletableFuture.runAsync(() -> loginDatabaseProvider.save(loginHolder));
    }

    public CompletableFuture<Optional<LoginHolder>> find(final @NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> loginDatabaseProvider.get(uuid.toString()));
    }

    public CompletableFuture<Boolean> exist(final @NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> loginDatabaseProvider.get(uuid.toString()).isPresent());
    }

    public CompletableFuture<Void> delete(final @NotNull UUID uuid) {
        return CompletableFuture.runAsync(() -> loginDatabaseProvider.delete(uuid.toString()));
    }
}
