package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerDropItemListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class PlayerDropItemListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public PlayerDropItemListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        final var player = event.getPlayer();

        if (!sessionManager.isAuthenticated(player)) {
            event.setCancelled(true);
        }
    }
}
