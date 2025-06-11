package com.anticheat.listeners;

import com.anticheat.McAntiCheat;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlySpeedListener implements Listener {
    private final McAntiCheat plugin;
    private final double maxSpeed = 0.75; // blocks per tick

    public FlySpeedListener(McAntiCheat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;
        if (plugin.isModuleEnabled("fly") && !plugin.isExempt(player, "fly") && !player.hasPermission("mc_anticheat.bypass.fly")) {
            if (player.isFlying()) {
                event.setCancelled(true);
                player.setFlying(false);
                player.setAllowFlight(false);
                player.teleport(player.getLocation().add(0, -1, 0));
                plugin.logViolation(player, "fly");
                return;
            }
        }
        if (plugin.isModuleEnabled("speed") && !plugin.isExempt(player, "speed") && !player.hasPermission("mc_anticheat.bypass.speed")) {
            double distance = event.getFrom().distance(event.getTo());
            if (distance > maxSpeed) {
                event.setTo(event.getFrom());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 1, true, false, false));
                plugin.logViolation(player, "speed");
            }
        }
    }
}
