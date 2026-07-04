package org.bingo.discordpresence;

import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;

public class DebugLogger {

    private final JavaPlugin plugin;
    private boolean debugEnabled;
    private boolean verboseEnabled;

    public DebugLogger(JavaPlugin plugin, boolean debugEnabled, boolean verboseEnabled) {
        this.plugin = plugin;
        this.debugEnabled = debugEnabled;
        this.verboseEnabled = verboseEnabled;
    }

    public void updateFlags(boolean debugEnabled, boolean verboseEnabled) {
        this.debugEnabled = debugEnabled;
        this.verboseEnabled = verboseEnabled;
    }

    public void debug(String message) {
        if (!debugEnabled) return;
        plugin.getLogger().info("[DEBUG] " + Instant.now() + " " + message);
    }

    public void verbose(String message) {
        if (!verboseEnabled) return;
        plugin.getLogger().info("[VERBOSE] " + Instant.now() + " " + message);
    }

    public void severe(String message) {
        plugin.getLogger().severe("[ERROR] " + Instant.now() + " " + message);
    }
}