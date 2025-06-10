# mc_anticheat

A simple Minecraft anti-cheat plugin for Spigot/Paper servers. It disables common hacks instead of kicking or banning and protects selected players from ban or kick commands.

## Features
- Protection system preventing `/ban`, `/kick` and `/ban-ip` on protected players
- Toggleable anti-cheat modules: fly, speed, killaura, autoclicker and nofall
- Player specific exemptions per module
- Logs violations to console

## Commands
- `/protect <player>` – protect a player
- `/unprotect <player>` – remove protection
- `/anticheat toggle <module>` – enable/disable a module
- `/anticheat allow <player> <module>` – exempt a player from a module
- `/anticheat deny <player> <module>` – remove exemption
- `/anticheat status` – show module status

## Permissions
- `mc_anticheat.exempt`
- `mc_anticheat.admin`
- `mc_anticheat.bypass.*` – bypass module checks

## Building
Run `mvn package` to build the plugin jar.
