package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerMoveListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class PlayerMoveListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public PlayerMoveListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerMove(final @NotNull PlayerMoveEvent event) {
        final var player = event.getPlayer();

        if (!sessionManager.isAuthenticated(player)) {
            event.setCancelled(true);
        }
    }
}