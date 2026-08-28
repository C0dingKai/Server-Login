package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.jetbrains.annotations.NotNull;

/**
 * EntityPickupItemListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class EntityPickupItemListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public EntityPickupItemListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onEntityPickupItem(final @NotNull EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof final Player player)) return;

        if (!sessionManager.isAuthenticated(player)) {
            event.setCancelled(true);
        }
    }
}
