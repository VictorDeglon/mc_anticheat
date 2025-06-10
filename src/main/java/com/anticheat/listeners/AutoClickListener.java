package com.anticheat.listeners;

import com.anticheat.McAntiCheat;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class AutoClickListener implements Listener {
    private final McAntiCheat plugin;
    private final Map<Player, Integer> clicks = new HashMap<>();

    public AutoClickListener(McAntiCheat plugin) {
        this.plugin = plugin;
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                clicks.clear();
            }
        };
        task.runTaskTimer(plugin, 20, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE || player.hasPermission("mc_anticheat.bypass.autoclicker"))
            return;
        if (!plugin.isModuleEnabled("autoclicker") || plugin.isExempt(player, "autoclicker")) return;
        clicks.put(player, clicks.getOrDefault(player, 0) + 1);
        if (clicks.get(player) > 20) {
            event.setCancelled(true);
            plugin.logViolation(player, "autoclicker");
        }
    }
}
