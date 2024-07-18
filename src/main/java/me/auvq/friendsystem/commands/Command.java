package me.auvq.friendsystem.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import me.auvq.friendsystem.handlers.PlayerHandler;
import me.auvq.friendsystem.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@dev.rollczi.litecommands.annotations.command.Command(name = "friends")
public class Command {


    @Execute(name = "help")
    public void help(@Context CommandSender sender) {
        List<String> helpMessage = new ArrayList<>();

        helpMessage.add(CC.color("&7&m-----------------------------"));
        helpMessage.add(CC.color("&&eFriends - Created by &6auvq"));
        helpMessage.add(CC.color("&7&m-----------------------------"));
        helpMessage.add(CC.color("&e/friends help §7- §fShow this message"));
        helpMessage.add(CC.color("&e/friends add <player> §7- §fAdd a friend"));
        helpMessage.add(CC.color("&e/friends remove <player> §7- §fRemove a friend"));
        helpMessage.add(CC.color("&e/friends list §7- §fList your friends"));
        helpMessage.add(CC.color("&e/friends toggle §7- §fToggle friend requests"));
        helpMessage.add(CC.color("&7&m-----------------------------"));

        helpMessage.forEach(sender::sendMessage);
    }

    @Execute(name = "toggle")
    public void toggleRequests(@Context CommandSender sender) {
        if(!(sender instanceof Player)){
            sender.sendMessage(CC.color("&cYou must be a player to execute this command!"));
            return;
        }

        Player player = (Player) sender;
        PlayerHandler playerHandler = PlayerHandler.get(player);

        playerHandler.setRequestsEnabled(!playerHandler.isRequestEnabled());

        player.sendMessage(CC.color("&eFriend requests are now " + (playerHandler.isRequestEnabled() ? "&aenabled" : "&cdisabled") + "&e!"));
    }

    @Execute(name = "add")
    public void addFriend(@Context CommandSender sender, @Arg String target) {
        if(!(sender instanceof Player)){
            sender.sendMessage(CC.color("&cYou must be a player to execute this command!"));
            return;
        }

        Player player = (Player) sender;
        Player friend = player.getServer().getPlayer(target);

        if(friend == null){
            player.sendMessage(CC.color("&cThat player is not online!"));
            return;
        }

        if(player == friend){
            player.sendMessage(CC.color("&cYou can't add yourself as a friend!"));
            return;
        }

        if(!PlayerHandler.get(friend).isRequestEnabled()){
            player.sendMessage(CC.color("&cThat player has disabled friend requests!"));
            return;
        }

        PlayerHandler playerHandler = PlayerHandler.get(player);

        playerHandler.addFriend(friend.getUniqueId());
        player.sendMessage(CC.color("&eYou have added &6" + friend.getName() + " &eto your friends list!"));
    }

    @Execute(name = "remove")
    public void removeFriend(@Context CommandSender sender, @Arg String target) {
        if(!(sender instanceof Player)){
            sender.sendMessage(CC.color("&cYou must be a player to execute this command!"));
            return;
        }

        Player player = (Player) sender;
        Player friend = player.getServer().getPlayer(target);

        if(friend == null){
            player.sendMessage(CC.color("&cThat player is not online!"));
            return;
        }

        if(player == friend){
            player.sendMessage(CC.color("&cYou can't remove yourself as a friend!"));
            return;
        }

        PlayerHandler playerHandler = PlayerHandler.get(player);

        playerHandler.removeFriend(friend.getUniqueId());
        player.sendMessage(CC.color("&eYou have removed &6" + friend.getName() + " &efrom your friends list!"));
    }

    @Execute(name = "list")
    public void friendList(@Context CommandSender sender) {
        if(!(sender instanceof Player)){
            sender.sendMessage(CC.color("&cYou must be a player to execute this command!"));
            return;
        }

        Player player = (Player) sender;
        PlayerHandler playerHandler = PlayerHandler.get(player);
        List<String> playerFriends = playerHandler.getFriends();

        if(playerFriends.isEmpty()){
            player.sendMessage(CC.color("&eYou don't have any friends!"));
            return;
        }

        player.sendMessage(CC.color("&eYour friends:"));
        player.sendMessage(CC.color("&7&m-----------------------------"));
        playerFriends.forEach(friend -> player.sendMessage(
                CC.color("&6- " +
                        Bukkit.getOfflinePlayer(friend).getName() +
                        "&7 - " +
                        (Bukkit.getOfflinePlayer(friend).isOnline() ? "&aOnline" : "&cOffline")))
        );
        player.sendMessage(CC.color("&7&m-----------------------------"));
    }
}
