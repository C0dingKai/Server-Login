package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerQuitListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class PlayerQuitListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public PlayerQuitListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        final var player = event.getPlayer();

        if (sessionManager.isAuthenticated(player)) {
            sessionManager.logout(player);
        }
    }
}