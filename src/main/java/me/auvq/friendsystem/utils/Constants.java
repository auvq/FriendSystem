package me.auvq.friendsystem.utils;

import me.auvq.friendsystem.Main;

public class Constants {

    private static Main plugin = Main.getInstance();

    public static class Database {
        public static final String URI = plugin.getConfig().getString("database.uri");
        public static final String DATABASE_NAME = plugin.getConfig().getString("database.db");
        public static final String COLLECTION_NAME = plugin.getConfig().getString("database.collection");
    }

}
