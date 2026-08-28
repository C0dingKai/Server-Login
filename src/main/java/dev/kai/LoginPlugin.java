package dev.kai;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kai.commands.impl.LoginCommand;
import dev.kai.commands.impl.RegisterCommand;
import dev.kai.listener.PlayerCommandPreprocessListener;
import dev.kai.listener.PlayerJoinListener;
import dev.kai.listener.PlayerMoveListener;
import dev.kai.listener.PlayerQuitListener;
import dev.kai.manager.LoginManager;
import dev.kai.manager.SessionManager;
import dev.kai.storage.DatabaseManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LoginPlugin extends JavaPlugin {

    @Getter
    private static LoginPlugin instance;
    private DatabaseManager databaseManager;
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

        databaseManager = new DatabaseManager(getDataFolder());
        databaseManager.connect();

        loginManager = new LoginManager();
        sessionManager = new SessionManager();

        new PlayerJoinListener();
        new PlayerQuitListener();
        new PlayerMoveListener();
        new PlayerCommandPreprocessListener();

        new LoginCommand();
        new RegisterCommand();


    }

    @Override
    public void onDisable() {
        if (databaseManager != null) databaseManager.shutdown();
        PacketEvents.getAPI().terminate();
    }
}
