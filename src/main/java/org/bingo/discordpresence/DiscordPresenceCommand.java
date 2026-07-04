package org.bingo.discordpresence;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordPresenceCommand implements CommandExecutor, TabCompleter {

    private final Discordpresence plugin;

    public DiscordPresenceCommand(Discordpresence plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /discordpresence <reload|status>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                return handleReload(sender);
            case "status":
                return handleStatus(sender);
            default:
                sender.sendMessage(ChatColor.YELLOW + "Usage: /discordpresence <reload|status>");
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "status").stream()
                    .filter(sub -> sub.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("discordpresence.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        plugin.reloadPluginConfig();
        sender.sendMessage(ChatColor.GREEN + "DiscordPresence config reloaded.");
        return true;
    }

    private boolean handleStatus(CommandSender sender) {
        if (!sender.hasPermission("discordpresence.status")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        DiscordBot bot = plugin.getDiscordBot();
        boolean channelSet = !plugin.getConfig().getString("channel-id", "").isEmpty();
        boolean botConnected = bot != null && bot.getJda() != null
                && bot.getJda().getStatus() == net.dv8tion.jda.api.JDA.Status.CONNECTED;
        boolean channelValid = bot != null && bot.isChannelValid();

        sender.sendMessage(ChatColor.GOLD + "--- DiscordPresence Status ---");
        sender.sendMessage(botConnected
                ? ChatColor.GREEN + "✔ Token valid, bot connected"
                : ChatColor.RED + "✘ Bot not connected — check token and console for errors");
        sender.sendMessage((channelSet ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘") + ChatColor.WHITE + " Channel ID configured: " + channelSet);
        sender.sendMessage((channelValid ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘") + ChatColor.WHITE + " Channel is reachable: " + channelValid);
        sender.sendMessage(ChatColor.WHITE + "Debug mode: " + plugin.getConfig().getBoolean("debug", false));
        sender.sendMessage(ChatColor.WHITE + "Verbose logging: " + plugin.getConfig().getBoolean("debug-logging", false));

        return true;
    }
}