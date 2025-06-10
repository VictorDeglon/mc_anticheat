package com.anticheat.listeners;

import com.anticheat.McAntiCheat;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class KillAuraListener implements Listener {
    private final McAntiCheat plugin;
    private final Map<Player, Long> lastHit = new HashMap<>();

    public KillAuraListener(McAntiCheat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.hasPermission("mc_anticheat.bypass.killaura"))
            return;
        if (!plugin.isModuleEnabled("killaura") || plugin.isExempt(player, "killaura")) return;

        long now = System.currentTimeMillis();
        long last = lastHit.getOrDefault(player, 0L);
        if (now - last < 100) {
            event.setCancelled(true);
            plugin.logViolation(player, "killaura");
        }
        lastHit.put(player, now);
    }
}
