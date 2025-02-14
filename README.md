# LPDiscordLogger

LPDiscordLogger is a Minecraft Paper plugin that logs specific player commands executed by members of a designated LuckPerms group and sends these logs to a specified Discord channel via a webhook. This is particularly useful for monitoring administrative actions on your server.

## Features

- **Command Logging**: Monitors and logs commands executed by players in a specified LuckPerms group.
- **Discord Integration**: Sends logged commands to a Discord channel using a webhook URL.
- **Configurable**: Allows customization of the target LuckPerms group, Discord webhook URL, and commands to ignore through the `config.yml` file.
- **Runtime Configuration Reload**: Provides an in-game command to reload the plugin's configuration without restarting the server. `/lpdiscordreload`

## Prerequisites

- **Minecraft Server**: Running a compatible version with the PaperMC API.
- **LuckPerms Plugin**: Ensure the LuckPerms plugin is installed and configured on your server.
- **Java**: Version 17 or above installed on your server.

## Installation

1. **Download the Plugin**:
   - [Download the latest release](https://github.com/PetalCat/LPDiscordLogger/releases) of LPDiscordLogger.

2. **Add to Plugins Folder**:
   - Place the downloaded `LPDiscordLogger.jar` file into your server's `plugins` directory.

3. **Start the Server**:
   - Launch your Minecraft server. The plugin will generate a default `config.yml` file in the `plugins/LPDiscordLogger` directory.

4. **Configure the Plugin**:
   - Navigate to `plugins/LPDiscordLogger/config.yml` and adjust the settings as needed:
     - `discordWebhookUrl`: Your Discord webhook URL.
     - `targetGroup`: The LuckPerms group to monitor (e.g., `admin`).
     - `ignoredCommands`: A list of command prefixes to ignore (e.g., `/msg`, `/whisper`).

5. **Reload Configuration**:
   - Use the `/lpdiscordreload` command in-game or through the server console to apply configuration changes without restarting the server.

## Configuration

The `config.yml` file allows you to customize the plugin's behavior:

```yaml
discordWebhookUrl: "https://discord.com/api/webhooks/your_webhook_url"
targetGroup: "admin"
ignoredCommands:
  - "/msg"
  - "/whisper"
  - "/tell"
