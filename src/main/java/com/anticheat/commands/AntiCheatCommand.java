package com.anticheat.commands;

import com.anticheat.McAntiCheat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AntiCheatCommand implements CommandExecutor {
    private final McAntiCheat plugin;

    public AntiCheatCommand(McAntiCheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/anticheat <toggle|allow|deny|status>");
            return true;
        }
        String sub = args[0].toLowerCase();
        if (sub.equals("toggle")) {
            if (!sender.hasPermission("mc_anticheat.admin")) {
                sender.sendMessage("No permission.");
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage("Usage: /anticheat toggle <module>");
                return true;
            }
            plugin.toggleModule(args[1]);
            sender.sendMessage(args[1] + " toggled to " + plugin.isModuleEnabled(args[1]));
            return true;
        } else if (sub.equals("allow")) {
            if (!sender.hasPermission("mc_anticheat.admin")) {
                sender.sendMessage("No permission.");
                return true;
            }
            if (args.length != 3) {
                sender.sendMessage("Usage: /anticheat allow <player> <module>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("Player not found");
                return true;
            }
            plugin.allow(target, args[2]);
            sender.sendMessage("Allowed " + target.getName() + " for " + args[2]);
            return true;
        } else if (sub.equals("deny")) {
            if (!sender.hasPermission("mc_anticheat.admin")) {
                sender.sendMessage("No permission.");
                return true;
            }
            if (args.length != 3) {
                sender.sendMessage("Usage: /anticheat deny <player> <module>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("Player not found");
                return true;
            }
            plugin.deny(target, args[2]);
            sender.sendMessage("Denied " + target.getName() + " for " + args[2]);
            return true;
        } else if (sub.equals("status")) {
            sender.sendMessage("Module status:");
            plugin.getConfig().getConfigurationSection("modules").getKeys(false).forEach(m -> {
                sender.sendMessage("- " + m + ": " + plugin.isModuleEnabled(m));
            });
            return true;
        }
        return false;
    }
}
