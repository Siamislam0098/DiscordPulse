package org.bingo.discordpresence;

import org.bukkit.plugin.java.JavaPlugin;

public final class Discordpresence extends JavaPlugin {

    private DiscordBot discordBot;
    private DebugLogger debugLogger;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initialize();
        DiscordPresenceCommand cmd = new DiscordPresenceCommand(this);
        getCommand("discordpresence").setExecutor(cmd);
        getCommand("discordpresence").setTabCompleter(cmd);
    }

    private void initialize() {
        String token = getConfig().getString("bot-token", "");
        String channelId = getConfig().getString("channel-id", "");
        boolean debugEnabled = getConfig().getBoolean("debug", false);
        boolean verboseEnabled = getConfig().getBoolean("debug-logging", false);

        debugLogger = new DebugLogger(this, debugEnabled, verboseEnabled);

        if (token.isEmpty() || channelId.isEmpty()) {
            getLogger().severe("Bot token or channel ID missing in config.yml. Plugin disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        discordBot = new DiscordBot();
        try {
            discordBot.start(token, channelId, this);
            getLogger().info("Discord bot connected successfully.");
            discordBot.sendStatusMessage("online");
            debugLogger.debug("Plugin enabled successfully.");
        } catch (Exception e) {
            debugLogger.severe("Failed to connect Discord bot: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new MinecraftChatListener(discordBot, debugLogger), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(discordBot, debugLogger), this);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(discordBot, debugLogger), this);
    }

    public void reloadPluginConfig() {
        String oldToken = getConfig().getString("bot-token", "");
        reloadConfig();
        String newToken = getConfig().getString("bot-token", "");

        if (!oldToken.equals(newToken)) {
            debugLogger.severe("Bot token was changed in config.yml. Reload cannot apply a token change — restart the server to reconnect with the new token.");
        }

        String newChannelId = getConfig().getString("channel-id", "");
        boolean newDebugEnabled = getConfig().getBoolean("debug", false);
        boolean newVerboseEnabled = getConfig().getBoolean("debug-logging", false);

        discordBot.setChannelId(newChannelId);
        debugLogger.updateFlags(newDebugEnabled, newVerboseEnabled);

        debugLogger.debug("Config reloaded. Channel ID and debug flags updated.");
    }

    @Override
    public void onDisable() {
        if (debugLogger != null) {
            debugLogger.debug("Plugin disabling.");
        }
        if (discordBot != null) {
            discordBot.sendStatusMessage("offline");
            discordBot.stop();
        }
    }

    public DebugLogger getDebugLogger() {
        return debugLogger;
    }

    public DiscordBot getDiscordBot() {
        return discordBot;
    }
}