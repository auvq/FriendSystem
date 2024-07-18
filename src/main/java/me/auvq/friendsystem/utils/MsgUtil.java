package me.auvq.friendsystem.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MsgUtil {

    public static char HEART = '\u2764';

    public static void sendMessage(String text) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.color(text)));
        MsgUtil.logConsole(text);
    }

    public static void sendAdminMessage(String text){
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.hasPermission("minefruitevents.admin")){
                player.sendMessage(CC.color(text));
            }
        });
    }


    public static void log(CommandSender sender, String text) {
        Command.broadcastCommandMessage(sender, text);
    }

    public static void logConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(CC.color(text));
    }
}
