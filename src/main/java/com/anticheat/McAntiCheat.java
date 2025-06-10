package com.anticheat;

import com.anticheat.commands.AntiCheatCommand;
import com.anticheat.commands.ProtectCommand;
import com.anticheat.commands.UnprotectCommand;
import com.anticheat.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.*;

public class McAntiCheat extends JavaPlugin {
    private final Set<UUID> protectedPlayers = new HashSet<>();
    private final Map<String, Boolean> modules = new HashMap<>();
    private final Map<UUID, Set<String>> exemptions = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        getCommand("protect").setExecutor(new ProtectCommand(this));
        getCommand("unprotect").setExecutor(new UnprotectCommand(this));
        getCommand("anticheat").setExecutor(new AntiCheatCommand(this));
        registerListeners();
        getLogger().info("mc_anticheat enabled");
    }

    @Override
    public void onDisable() {
        saveConfigValues();
    }

    private void loadConfigValues() {
        FileConfiguration cfg = getConfig();
        for (String s : cfg.getStringList("protected-players")) {
            try {
                protectedPlayers.add(UUID.fromString(s));
            } catch (IllegalArgumentException ignored) {
                Player p = Bukkit.getPlayerExact(s);
                if (p != null) protectedPlayers.add(p.getUniqueId());
            }
        }
        for (String key : Objects.requireNonNull(cfg.getConfigurationSection("modules")).getKeys(false)) {
            modules.put(key.toLowerCase(), cfg.getBoolean("modules." + key));
        }
        if (cfg.isConfigurationSection("exemptions")) {
            for (String player : cfg.getConfigurationSection("exemptions").getKeys(false)) {
                UUID uuid = Bukkit.getOfflinePlayer(player).getUniqueId();
                List<String> list = cfg.getStringList("exemptions." + player);
                exemptions.put(uuid, new HashSet<>());
                for (String m : list) {
                    exemptions.get(uuid).add(m.toLowerCase());
                }
            }
        }
    }

    private void saveConfigValues() {
        FileConfiguration cfg = getConfig();
        List<String> protectedList = new ArrayList<>();
        for (UUID id : protectedPlayers) {
            protectedList.add(id.toString());
        }
        cfg.set("protected-players", protectedList);
        for (String key : modules.keySet()) {
            cfg.set("modules." + key, modules.get(key));
        }
        for (UUID id : exemptions.keySet()) {
            List<String> list = new ArrayList<>(exemptions.get(id));
            cfg.set("exemptions." + Bukkit.getOfflinePlayer(id).getName(), list);
        }
        saveConfig();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new CommandProtectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FlySpeedListener(this), this);
        Bukkit.getPluginManager().registerEvents(new KillAuraListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AutoClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new NoFallListener(this), this);
    }

    public boolean isProtected(UUID uuid) {
        return protectedPlayers.contains(uuid);
    }

    public void addProtected(UUID uuid) {
        protectedPlayers.add(uuid);
    }

    public void removeProtected(UUID uuid) {
        protectedPlayers.remove(uuid);
    }

    public boolean isModuleEnabled(String module) {
        return modules.getOrDefault(module.toLowerCase(), false);
    }

    public void toggleModule(String module) {
        module = module.toLowerCase();
        modules.put(module, !modules.getOrDefault(module, false));
    }

    public boolean isExempt(Player player, String module) {
        if (player.hasPermission("mc_anticheat.exempt")) return true;
        Set<String> set = exemptions.get(player.getUniqueId());
        return set != null && set.contains(module.toLowerCase());
    }

    public void allow(Player player, String module) {
        exemptions.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(module.toLowerCase());
    }

    public void deny(Player player, String module) {
        Set<String> set = exemptions.get(player.getUniqueId());
        if (set != null) set.remove(module.toLowerCase());
    }

    public void logViolation(Player player, String module) {
        String msg = String.format("[%s] %s violated %s", LocalDateTime.now(), player.getName(), module);
        getLogger().info(msg);
    }
}
