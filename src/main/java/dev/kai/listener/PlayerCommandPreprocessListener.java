package dev.kai.listener;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import dev.kai.util.ColorUtil;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerCommandPreprocessListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class PlayerCommandPreprocessListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public PlayerCommandPreprocessListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(final @NotNull PlayerCommandPreprocessEvent event) {
        final var player = event.getPlayer();

        if (sessionManager.isAuthenticated(player)) return;

        final var command = event.getMessage().split(" ")[0].toLowerCase();

        if (!command.equals("/register") && !command.equals("/login")) {
            event.setCancelled(true);
            player.sendRichMessage("<red>You must login or register first to get started");
            player.sendActionBar(ColorUtil.parse("<red>You must login or register first to get started"));

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }



    }


}