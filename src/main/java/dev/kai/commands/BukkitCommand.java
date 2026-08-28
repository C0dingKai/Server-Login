package dev.kai.commands;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;

import dev.kai.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class BukkitCommand extends Command {

    private final @Nullable String permission;
    private boolean playerOnly;

    protected BukkitCommand(@NotNull String name, @Nullable String permission, @NotNull String @NotNull ... aliases) {
        super(name, name + " command", "/" + name, aliases(name, aliases));
        this.permission = permission;

        register();
    }

    protected abstract void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args);

    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        return suggest(args.length == 0 ? "" : args[args.length - 1], onlinePlayers());
    }

    protected final @NotNull BukkitCommand playerOnly() {
        this.playerOnly = true;
        return this;
    }

    @Override
    public final boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (playerOnly && !(sender instanceof Player)) {
            deny(sender, "<red>This command can only be used by players.");
            return true;
        }

        if (!allowed(sender)) {
            deny(sender, "<red>You don't have permission to use this command.");
            return true;
        }

        try {
            onCommand(sender, label, args);
        } catch (final Exception exception) {
            deny(sender, "<red>An error occurred while executing this command.");

            Bukkit.getLogger().log(Level.SEVERE, "Failed to execute command '" + getName() + "'", exception);
        }
        return true;
    }

    @Override
    public final @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (!allowed(sender) || (playerOnly && !(sender instanceof Player))) return List.of();

        return Objects.requireNonNullElseGet(onTabComplete(sender, alias, args), List::of);
    }

    protected static @NotNull List<String> suggest(@NotNull String input, @NotNull Collection<String> options) {
        final String lower = input.toLowerCase(Locale.ROOT);

        return options.stream()
                .filter(option -> option.toLowerCase(Locale.ROOT).startsWith(lower))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    protected static @NotNull List<String> suggest(@NotNull String input, @NotNull String @NotNull ... options) {
        return suggest(input, Arrays.asList(options));
    }

    protected static @NotNull List<String> onlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).map(name -> (String) name).toList();
    }

    protected static void deny(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendRichMessage(message);

        if (sender instanceof final Player player) {
            player.sendActionBar(ColorUtil.parse(message));
            player.playSound(player, Sound.ENTITY_VILLAGER_HURT, 1, 1);
        }
    }

    private boolean allowed(@NotNull CommandSender sender) {
        return permission == null || sender.hasPermission(permission);
    }

    private static @NotNull List<String> aliases(@NotNull String name, @NotNull String @NotNull [] aliases) {
        return Arrays.stream(aliases)
                .filter(alias -> alias != null && !alias.isBlank())
                .map(alias -> alias.toLowerCase(Locale.ROOT))
                .filter(alias -> !alias.equalsIgnoreCase(name))
                .distinct()
                .toList();
    }

    private void register() {
        commandMap().register("StablePvP", this);
    }

    public void unregister() {
        unregister(commandMap());
    }

    private static @NotNull CommandMap commandMap() {
        try {
            return Bukkit.getCommandMap();
        } catch (final NoSuchMethodError ignored) {
            try {
                final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);

                return (CommandMap) field.get(Bukkit.getServer());
            } catch (final NoSuchFieldException | IllegalAccessException exception) {
                throw new IllegalStateException("Cannot access the command map", exception);
            }
        }
    }
}