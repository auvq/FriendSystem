package me.auvq.friendsystem;

import com.mongodb.DocumentToDBRefTransformer;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import lombok.Getter;
import me.auvq.friendsystem.commands.Command;
import me.auvq.friendsystem.utils.CC;
import me.auvq.friendsystem.utils.Constants;
import me.clip.placeholderapi.PlaceholderHook;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private LiteCommands<CommandSender> liteCommands;;

    private MongoClient mongoClient;

    private MongoCollection<Document> collection;

    @Override
    public void onEnable() {
        instance = this;

        this.liteCommands = LiteCommandsBukkit.builder()
                .commands(LiteCommandsAnnotations.of(new Command()))
                .build();

        getServer().getConsoleSender().sendMessage(CC.color("&aFriends has been enabled."));

        assert Constants.Database.URI != null;
        mongoClient = MongoClients.create(Constants.Database.URI);

        assert Constants.Database.DATABASE_NAME != null;
        assert Constants.Database.COLLECTION_NAME != null;
        collection = mongoClient.getDatabase(Constants.Database.DATABASE_NAME).getCollection(Constants.Database.COLLECTION_NAME);

        getServer().getConsoleSender().sendMessage(CC.color("&aMongoDB successfully setup!"));

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

            getServer().getConsoleSender().sendMessage(CC.color("&aPlaceholderAPI registered hookups!"));
            getLogger().info("Registered PlaceholderAPI expansions for your plugin.");
        } else {
            getLogger().warning("PlaceholderAPI is not installed, PlaceholderAPI expansions will not be available.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getConsoleSender().sendMessage(CC.color("&aThe config setup correctly!"));
    }
}
