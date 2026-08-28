package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerInteractListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class PlayerInteractListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public PlayerInteractListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        final var player = event.getPlayer();

        if (!sessionManager.isAuthenticated(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(final @NotNull PlayerInteractEntityEvent event) {
        final var player = event.getPlayer();

        if (!sessionManager.isAuthenticated(player)) {
            event.setCancelled(true);
        }
    }
}
