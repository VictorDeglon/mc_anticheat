package com.anticheat.listeners;

import com.anticheat.McAntiCheat;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFallListener implements Listener {
    private final McAntiCheat plugin;

    public NoFallListener(McAntiCheat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getGameMode() == GameMode.CREATIVE || player.hasPermission("mc_anticheat.bypass.nofall"))
                return;
            if (!plugin.isModuleEnabled("nofall") || plugin.isExempt(player, "nofall")) return;
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getDamage() == 0) {
                double dmg = player.getFallDistance() / 2.0;
                if (dmg > 0) {
                    player.damage(dmg);
                    plugin.logViolation(player, "nofall");
                }
            }
        }
    }
}
