package dev.kai.commands.impl;

import dev.kai.LoginPlugin;
import dev.kai.commands.BukkitCommand;
import dev.kai.manager.LoginManager;
import dev.kai.manager.SessionManager;
import dev.kai.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * LoginCommand
 *
 * @author Kai
 * @since 8/28/2026
 */
public class LoginCommand extends BukkitCommand {

    private final @NotNull LoginManager loginManager = LoginPlugin.getInstance().getLoginManager();
    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public LoginCommand() {
        super("login", null);
    }

    @Override
    protected void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof final Player player)) return;

        if (args.length == 0) {
            player.sendRichMessage("<red>Wrong Usage! /login <password>");
            player.sendActionBar(ColorUtil.parse("<red>Wrong Usage! /login <password>"));

            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        final var password = args[0];

        loginManager.find(player.getUniqueId())
                .thenAccept(optLogin -> Bukkit.getScheduler().runTask(LoginPlugin.getInstance(), () -> {

                    if (optLogin.isEmpty()) {
                        player.sendRichMessage("<red>You haven't created an account yet");
                        player.sendActionBar(ColorUtil.parse("<red>You haven't created an account yet"));

                        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return;
                    }

                    final var login = optLogin.get();

                    if (sessionManager.isAuthenticated(player)) {
                        player.sendRichMessage("<red>You are already logged in");
                        player.sendActionBar(ColorUtil.parse("<red>You are already logged in"));

                        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return;
                    }

                    if (!login.password().equals(password)) {
                        player.sendRichMessage("<red>The password is incorrect");
                        player.sendActionBar(ColorUtil.parse("<red>The password is incorrect"));

                        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return;
                    }

                    sessionManager.login(player);
                    player.sendRichMessage("<green>You have successfully logged in");
                    player.sendActionBar(ColorUtil.parse("<green>You have successfully logged in"));

                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                }));
    }
}
