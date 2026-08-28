package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerChatListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class PlayerChatListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public PlayerChatListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerChat(final @NotNull AsyncPlayerChatEvent event) {
        final var player = event.getPlayer();

        if (!sessionManager.isAuthenticated(player)) {
            event.setCancelled(true);
        }
    }
}
