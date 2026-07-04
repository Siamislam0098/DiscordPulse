package org.bingo.discordpresence;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class MinecraftChatListener implements Listener {

    private final DiscordBot discordBot;
    private final DebugLogger debugLogger;

    public MinecraftChatListener(DiscordBot discordBot, DebugLogger debugLogger) {
        this.discordBot = discordBot;
        this.debugLogger = debugLogger;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        debugLogger.verbose("Chat event: " + playerName + " said: " + message);
        discordBot.sendMessage("**" + playerName + "**: " + message);
    }
}