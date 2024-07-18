package me.auvq.friendsystem.utils;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CC {
    private static final Pattern PATTERN = Pattern.compile(
            "<(#[a-f0-9]{6}|aqua|black|blue|dark_(aqua|blue|gray|green|purple|red)|gray|gold|green|light_purple|red|white|yellow)>",
            Pattern.CASE_INSENSITIVE
    );

    public static String color(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        final Matcher matcher = PATTERN.matcher(text);

        while (matcher.find()) {
            try {
                final ChatColor chatColor = ChatColor.of(matcher.group(1));

                if (chatColor != null) {
                    text = StringUtils.replace(text, matcher.group(), chatColor.toString());
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }


    public static List<String> toColor(List<String> message) {
        return (List<String>)message.stream().map(CC::color).collect(Collectors.toList());
    }

    public static String color(String text, String name) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        text = text.replaceAll("%player%", name);
        text = text.replaceAll("%countdown%", name);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String color(String text, int countDown) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        text = text.replaceAll("%countdown%", String.valueOf(countDown));
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String color(String text, String playerName, int tokensAmount) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        text = text.replaceAll("%tokens%", String.valueOf(tokensAmount));
        text = text.replaceAll("%player%", String.valueOf(playerName));
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
