package dev.kai.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SessionManager
 *
 * @author Kai
 * @since 8/28/2026
 */
public class SessionManager {

    private final Set<UUID> authenticated = ConcurrentHashMap.newKeySet();

    public void login(final @NotNull Player player) {
        authenticated.add(player.getUniqueId());

        final var user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        user.sendPacket(new WrapperPlayServerEntityStatus(player.getEntityId(), 23));
    }

    public void logout(final @NotNull Player player) {
        authenticated.remove(player.getUniqueId());
    }

    public boolean isAuthenticated(final @NotNull Player player) {
        return authenticated.contains(player.getUniqueId());
    }
}