package org.bingo.discordpresence;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class DiscordMessageListener extends ListenerAdapter {

    private final JavaPlugin plugin;
    private final String channelId;

    private static final TextColor DISCORD_BLURPLE = TextColor.fromHexString("#5865F2");

    public DiscordMessageListener(JavaPlugin plugin, String channelId) {
        this.plugin = plugin;
        this.channelId = channelId;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (!event.getChannel().getId().equals(channelId)) {
            return;
        }

        String rawContent = event.getMessage().getContentRaw();

        if (rawContent.equalsIgnoreCase("Playerlist")) {
            handlePlayerListCommand(event);
            return;
        }

        relayToMinecraft(event);
    }

    private void handlePlayerListCommand(MessageReceivedEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            StringBuilder sb = new StringBuilder("**Online players (" + Bukkit.getOnlinePlayers().size() + "):**\n");
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                sb.append("No players online.");
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    sb.append("- ").append(player.getName()).append("\n");
                }
            }

            event.getChannel().sendMessage(sb.toString()).queue(sentMessage -> {
                // Delete both the bot's list and the user's trigger message at the same time, after 8 seconds
                sentMessage.delete().queueAfter(8, TimeUnit.SECONDS);
                event.getMessage().delete().queueAfter(8, TimeUnit.SECONDS);
            });
        });
    }

    private void relayToMinecraft(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String authorName = event.getAuthor().getName();
        String content = message.getContentDisplay();

        Member member = event.getMember();
        TextColor roleColor;
        if (member != null && member.getColor() != null) {
            roleColor = TextColor.color(member.getColor().getRGB());
        } else {
            roleColor = NamedTextColor.GRAY;
        }

        Component chatMessage = Component.text("[Discord] ", DISCORD_BLURPLE)
                .append(Component.text(authorName + ": ", roleColor))
                .append(Component.text(content, NamedTextColor.WHITE).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD));

        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getServer().sendMessage(chatMessage);
        });
    }
}