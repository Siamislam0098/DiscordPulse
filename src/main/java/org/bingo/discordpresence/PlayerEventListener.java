package org.bingo.discordpresence;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.awt.Color;

public class PlayerEventListener implements Listener {

    private final DiscordBot discordBot;
    private final DebugLogger debugLogger;

    public PlayerEventListener(DiscordBot discordBot, DebugLogger debugLogger) {
        this.discordBot = discordBot;
        this.debugLogger = debugLogger;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        String deathMessage = event.deathMessage() != null
                ? PlainTextComponentSerializer.plainText().serialize(event.deathMessage())
                : playerName + " died";
        debugLogger.verbose("Death event: " + deathMessage);
        discordBot.sendEmbed("💀 " + deathMessage, Color.GRAY, null);
    }

    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        String playerName = event.getPlayer().getName();
        String advancementKey = event.getAdvancement().getKey().getKey();

        if (advancementKey.startsWith("recipes/")) {
            return;
        }

        var display = event.getAdvancement().getDisplay();
        String title = display != null
                ? PlainTextComponentSerializer.plainText().serialize(display.title())
                : advancementKey;

        String verbPhrase;
        if (display != null) {
            switch (display.frame()) {
                case GOAL -> verbPhrase = "has reached the goal";
                case CHALLENGE -> verbPhrase = "has completed the challenge";
                default -> verbPhrase = "has made the advancement";
            }
        } else {
            verbPhrase = "has made the advancement";
        }

        debugLogger.verbose("Advancement event: " + playerName + " - " + title);
        discordBot.sendEmbed("🏆 " + playerName + " " + verbPhrase + " [" + title + "]", Color.YELLOW, null);
    }
}