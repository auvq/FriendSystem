package me.auvq.friendsystem.handlers;

import com.google.common.collect.Maps;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import me.auvq.friendsystem.Main;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHandler {

    @Getter
    private static final Map<Player, PlayerHandler> playerData = Maps.newHashMap();

    private final Player player;

    private final Main plugin = Main.getInstance();

    private final Map<String, Object> values = Maps.newHashMap();

    @Getter
    public static HashMap<Player, Integer> wins = new HashMap<>();

    public PlayerHandler(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        this.player = player;
    }

    public static PlayerHandler get(Player player){
        if(playerData.containsKey(player)){
            return playerData.get(player);
        }

        PlayerHandler data = new PlayerHandler(player);
        data.loadData();

        playerData.put(player, data);
        return data;
    }

    private void loadData(){
        Document document = Main.getInstance().getCollection().find(
                        Filters.eq("UUID", this.player.getUniqueId().toString()))
                .first();

        if(document == null){
            putDefaultStats();
            plugin.getCollection().insertOne(toDocument());
            return;
        }

        this.values.putAll(document);
        wins.put(player, document.getInteger("wins"));
    }

    private void putDefaultStats(){
        this.values.put("friends", new ArrayList<String>());
        this.values.put("requests_enabled:", true);
    }

    public void setRequestsEnabled(boolean requestsEnabled){
        this.values.put("requests_enabled", requestsEnabled);
        Main.getInstance().getCollection().updateOne(
                Filters.eq("UUID",
                this.player.getUniqueId().toString()),
                new Document("$set", new Document("requests_enabled", requestsEnabled))
        );
    }

    public boolean isRequestEnabled(){
        return Boolean.getBoolean(String.valueOf(this.values.get("requests_enabled")));
    }

    public ArrayList<String> getFriends(){
        return (ArrayList<String>) this.values.get("friends");
    }

    public void addFriend(UUID friend){
        this.values.put("wins", getFriends().add(friend.toString()));
        Main.getInstance().getCollection().updateOne(
                Filters.eq("UUID",
                this.player.getUniqueId().toString()),
                new Document("$set", new Document("wins", getFriends().add(friend.toString())))
        );
    }

    public void removeFriend(UUID friend){
        this.values.put("wins", getFriends().remove(friend.toString()));
        Main.getInstance().getCollection().updateOne(
                Filters.eq("UUID",
                this.player.getUniqueId().toString()),
                new Document("$set", new Document("wins", getFriends().remove(friend.toString())))
        );
    }

    public Document toDocument(){
        return new Document("UUID", this.player.getUniqueId().toString())
                .append("friends", getFriends())
                .append("requests_enabled", isRequestEnabled()
                );
    }

    public void update(){
        Main.getInstance().getCollection()
                .replaceOne(
                        Filters.eq("UUID", this.player.getUniqueId().toString()),
                        toDocument()
                );
    }
}
