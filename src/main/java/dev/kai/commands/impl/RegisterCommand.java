package dev.kai.commands.impl;

import dev.kai.LoginPlugin;
import dev.kai.commands.BukkitCommand;
import dev.kai.manager.LoginManager;
import dev.kai.manager.SessionManager;
import dev.kai.model.Login;
import dev.kai.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * RegisterCommand
 *
 * @author Kai
 * @since 8/28/2026
 */
public class RegisterCommand extends BukkitCommand {

    private final @NotNull LoginManager loginManager = LoginPlugin.getInstance().getLoginManager();
    private final @NotNull SessionManager sessionManager = LoginPlugin.getInstance().getSessionManager();

    public RegisterCommand() {
        super("register", null);
    }

    @Override
    protected void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof final Player player)) return;

        if (args.length == 0) {
            player.sendRichMessage("<red>Wrong Usage! /register <password>");
            player.sendActionBar(ColorUtil.parse("<red>Wrong Usage! /register <password>"));

            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        loginManager.exist(player.getUniqueId()).thenAccept(exists -> Bukkit.getScheduler().runTask(LoginPlugin.getInstance(), () -> {
            if (exists) {
                player.sendRichMessage("<red>You already have registered");
                player.sendActionBar(ColorUtil.parse("<red>You already have registered"));

                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            final Login login = new Login(player.getUniqueId(), args[0]);
            loginManager.register(login).thenRun(() -> Bukkit.getScheduler().runTask(LoginPlugin.getInstance(), () -> {
                sessionManager.login(player);
                player.sendRichMessage("<green>You have successfully registered");
                player.sendActionBar(ColorUtil.parse("<green>You have successfully registered"));

                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }));
        }));
    }
}