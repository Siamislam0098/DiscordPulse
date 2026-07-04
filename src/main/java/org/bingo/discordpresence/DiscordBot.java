package org.bingo.discordpresence;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;
import javax.security.auth.login.LoginException;

public class DiscordBot {

    private JDA jda;
    private String channelId;

    public void start(String token, String channelId, JavaPlugin plugin) throws LoginException, InterruptedException {
        this.channelId = channelId;
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new DiscordMessageListener(plugin, channelId))
                .build();
        jda.awaitReady();
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public boolean isChannelValid() {
        return jda != null && jda.getTextChannelById(channelId) != null;
    }

    public void sendMessage(String message) {
        MessageChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(message).queue();
        } else {
            System.out.println("Channel not found. Check channel-id in config.yml.");
        }
    }

    public void sendEmbed(String title, Color color, String avatarUrl) {
        MessageChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(title, null, avatarUrl);
            embed.setColor(color);
            channel.sendMessageEmbeds(embed.build()).queue();
        } else {
            System.out.println("Channel not found. Check channel-id in config.yml.");
        }
    }

    public void sendStatusMessage(String status) {
        MessageChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            String dot = status.equalsIgnoreCase("online") ? "🟢" : "🔴";
            String text = dot + " **Server is " + status + "**";
            if (status.equalsIgnoreCase("offline")) {
                channel.sendMessage(text).complete();
            } else {
                channel.sendMessage(text).queue();
            }
        } else {
            System.out.println("Channel not found. Check channel-id in config.yml.");
        }
    }

    public void stop() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    public JDA getJda() {
        return jda;
    }
}