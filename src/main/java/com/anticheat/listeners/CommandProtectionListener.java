package com.anticheat.listeners;

import com.anticheat.McAntiCheat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.UUID;

public class CommandProtectionListener implements Listener {
    private final McAntiCheat plugin;

    public CommandProtectionListener(McAntiCheat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (shouldCancel(event.getPlayer(), event.getMessage())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        CommandSender sender = event.getSender();
        if (sender instanceof Player player) {
            if (shouldCancel(player, "/" + event.getCommand())) {
                event.setCommand("#");
            }
        } else {
            if (shouldCancel(null, "/" + event.getCommand())) {
                event.setCommand("#");
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player target = event.getPlayer();
        if (plugin.isProtected(target.getUniqueId()) || target.hasPermission("mc_anticheat.exempt")) {
            event.setCancelled(true);
        }
    }

    private boolean shouldCancel(Player executor, String msg) {
        String lower = msg.toLowerCase();
        if (lower.startsWith("/ban") || lower.startsWith("/kick") || lower.startsWith("/ban-ip")) {
            String[] parts = msg.split(" ");
            if (parts.length > 1) {
                Player target = Bukkit.getPlayer(parts[1]);
                if (target != null) {
                    UUID id = target.getUniqueId();
                    if (plugin.isProtected(id) || target.hasPermission("mc_anticheat.exempt")) {
                        if (executor != null) executor.sendMessage("That player is protected.");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
