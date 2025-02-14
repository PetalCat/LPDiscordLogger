package com.petalcat.logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LPDiscordLogger extends JavaPlugin implements Listener {

    // These values are loaded from the config.yml in the plugin folder.
    private String discordWebhookUrl;
    private String targetGroup;
    private List<String> ignoredCommands;

    @Override
    public void onEnable() {
        // Save the default config if not already present.
        saveDefaultConfig();
        // Load configuration values.
        loadConfiguration();
        // Register event listener.
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("LPDiscordLogger enabled!");
    }

    // Loads the config values into local variables.
    private void loadConfiguration() {
        discordWebhookUrl = getConfig().getString("discordWebhookUrl");
        targetGroup = getConfig().getString("targetGroup");
        ignoredCommands = getConfig().getStringList("ignoredCommands");
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase().trim();
        // Check if the command starts with an ignored prefix.
        for (String ignored : ignoredCommands) {
            if (message.startsWith(ignored.toLowerCase())) {
                return; // Skip logging for ignored commands.
            }
        }

        Player player = event.getPlayer();
        try {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            // Only log if the player's primary group matches the targetGroup.
            if (user != null && user.getPrimaryGroup().equalsIgnoreCase(targetGroup)) {
                String command = event.getMessage();
                getLogger().info(player.getName() + " executed: " + command);
                sendDiscordWebhook(player.getName() + " executed: " + command);
            }
        } catch (Exception e) {
            getLogger().warning("Error handling command event: " + e.getMessage());
        }
    }

    private void sendDiscordWebhook(String content) {
        try {
            URL url = new URL(discordWebhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Build a simple JSON payload with proper escaping.
            String jsonPayload = "{\"content\":\"" + content.replace("\"", "\\\"") + "\"}";
            byte[] out = jsonPayload.getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(out.length);
            connection.connect();
            try (OutputStream os = connection.getOutputStream()) {
                os.write(out);
            }
            // Optionally check the response code.
            connection.getResponseCode();
        } catch (Exception ex) {
            getLogger().severe("Failed to send Discord webhook: " + ex.getMessage());
        }
    }

    // This command reloads the plugin's configuration.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Use "lpdiscordreload" as the command name.
        if (command.getName().equalsIgnoreCase("lpdiscordreload")) {
            reloadConfig();
            loadConfiguration();
            sender.sendMessage("LPDiscordLogger configuration reloaded!");
            getLogger().info("Configuration reloaded by " + sender.getName());
            return true;
        }
        return false;
    }
}
