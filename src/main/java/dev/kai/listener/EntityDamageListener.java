package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * EntityDamageListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class EntityDamageListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public EntityDamageListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onEntityDamage(final @NotNull EntityDamageEvent event) {
        if (!(event.getEntity() instanceof final Player player)) return;

        if (!sessionManager.isAuthenticated(player)) {
            event.setCancelled(true);
        }
    }
}
