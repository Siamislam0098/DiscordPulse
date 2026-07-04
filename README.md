# About

**A lightweight Discord-Minecraft bridge plugin for Paper servers. Syncs chat both ways, posts join/leave, death, and achievement events, and shows live server status with an in-game player list command and reloadable config**

# Commands


```
/dp reload
```


```
/dp status
```


# Config.yml


```
# Your Discord bot's token, from the Discord Developer Portal (Bot tab).
# Keep this private — anyone with this token can control your bot.
bot-token: "PUT_YOUR_TOKEN_HERE"

# The Discord channel ID where chat sync, join/leave, death, and achievement messages will be sent/read.
# Enable Developer Mode in Discord (Settings > Advanced), then right-click the channel > Copy Channel ID.
channel-id: "PUT_CHANNEL_ID_HERE"

# If true, prints plugin enable/disable and connection status to the console.
# Does not send anything to Discord. Leave this off for normal use.
debug: false

# If true, prints detailed logs for every event (chat relay, join/leave, death, achievements, commands).
# This is verbose and will flood your console — only enable when actively troubleshooting a specific issue.
debug-logging: false
```
