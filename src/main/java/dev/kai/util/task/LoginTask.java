package dev.kai.util.task;

import dev.kai.LoginPlugin;
import dev.kai.manager.SessionManager;
import dev.kai.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * LoginTask
 *
 * @author Kai
 * @since 8/28/2026
 */
public class LoginTask extends BukkitRunnable {

    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    @Override
    public void run() {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!sessionManager.isAuthenticated(onlinePlayer)) {
                onlinePlayer.sendActionBar(ColorUtil.parse("<red>You need to be logged in before you can do anything"));
            }
        }
    }
}