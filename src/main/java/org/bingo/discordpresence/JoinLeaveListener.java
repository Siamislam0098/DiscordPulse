package org.bingo.discordpresence;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.Color;

public class JoinLeaveListener implements Listener {

    private final DiscordBot discordBot;
    private final DebugLogger debugLogger;

    public JoinLeaveListener(DiscordBot discordBot, DebugLogger debugLogger) {
        this.discordBot = discordBot;
        this.debugLogger = debugLogger;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        debugLogger.verbose("Join event: " + playerName);
        String avatarUrl = "https://mc-heads.net/avatar/" + playerName + "/64";
        discordBot.sendEmbed(playerName + " joined the server", Color.GREEN, avatarUrl);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        debugLogger.verbose("Quit event: " + playerName);
        String avatarUrl = "https://mc-heads.net/avatar/" + playerName + "/64";
        discordBot.sendEmbed(playerName + " left the server", Color.RED, avatarUrl);
    }
}