package dev.kai.listener;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import dev.kai.LoginPlugin;
import dev.kai.manager.LoginManager;
import dev.kai.manager.SessionManager;
import dev.kai.util.ColorUtil;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerJoinListener
 *
 * @author Kai
 * @since 8/28/2026
 */
public class PlayerJoinListener implements Listener {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();
    private final @NotNull LoginManager loginManager = LoginPlugin.getInstance().getLoginManager();


    public PlayerJoinListener() {
        LoginPlugin.getInstance().getServer().getPluginManager().registerEvents(this, LoginPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        final var player = event.getPlayer();

        if (sessionManager.isAuthenticated(player)) return;

        final var user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        user.sendPacket(new WrapperPlayServerEntityStatus(player.getEntityId(), 22));

        loginManager.exist(player.getUniqueId()).thenAccept(exists -> {
            if (exists) {
                player.sendRichMessage("<red>You need to be logged in before you can do anything");
                player.sendActionBar(ColorUtil.parse("<red>You need to be logged in before you can do anything"));
            } else {
                player.sendRichMessage("<red>You are not registered. Use /register <password>");
            }
        });
    }
}