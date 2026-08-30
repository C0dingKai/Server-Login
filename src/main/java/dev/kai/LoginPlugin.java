package dev.kai;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kai.commands.impl.LoginCommand;
import dev.kai.commands.impl.RegisterCommand;
import dev.kai.listener.EntityDamageListener;
import dev.kai.listener.EntityPickupItemListener;
import dev.kai.listener.PlayerChatListener;
import dev.kai.listener.PlayerCommandPreprocessListener;
import dev.kai.listener.PlayerDropItemListener;
import dev.kai.listener.PlayerInteractListener;
import dev.kai.listener.PlayerJoinListener;
import dev.kai.listener.PlayerMoveListener;
import dev.kai.listener.PlayerQuitListener;
import dev.kai.manager.LoginManager;
import dev.kai.manager.SessionManager;
import dev.kai.storage.DatabaseManager;
import dev.kai.util.task.LoginTask;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LoginPlugin extends JavaPlugin {

    @Getter
    private static LoginPlugin instance;
    private LoginManager loginManager;
    private SessionManager sessionManager;

    public LoginPlugin() {
        instance = this;
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }


    @Override
    public void onEnable() {

        this.connectDatabase();

        loginManager = new LoginManager();
        sessionManager = new SessionManager();

        new PlayerJoinListener();
        new PlayerQuitListener();
        new PlayerMoveListener();
        new PlayerChatListener();
        new PlayerInteractListener();
        new PlayerDropItemListener();
        new EntityPickupItemListener();
        new EntityDamageListener();
        new PlayerCommandPreprocessListener();

        new LoginCommand();
        new RegisterCommand();

        new LoginTask().runTaskTimer(this, 0, 20);

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        DatabaseManager.getInstance().destroy();
        PacketEvents.getAPI().terminate();
    }

    private void connectDatabase() {
        final String host = System.getenv("MONGODB_HOST");
        final String password = System.getenv("MONGODB_PASSWORD");
        final String username = System.getenv("MONGODB_USERNAME");
        final String database = System.getenv("MONGODB_DATABASE");

        DatabaseManager.getInstance().connect(
                host,
                27017,
                username,
                password,
                database);
    }
}
