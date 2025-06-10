package com.anticheat.commands;

import com.anticheat.McAntiCheat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class UnprotectCommand implements CommandExecutor {
    private final McAntiCheat plugin;

    public UnprotectCommand(McAntiCheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mc_anticheat.admin")) {
            sender.sendMessage("You don't have permission to do that.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("Usage: /unprotect <player>");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        UUID id = target.getUniqueId();
        plugin.removeProtected(id);
        sender.sendMessage(target.getName() + " is no longer protected.");
        return true;
    }
}
