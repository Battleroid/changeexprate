package com.caseyweed.changeexprate;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Changeexprate extends JavaPlugin implements Listener {

    public int rate = 1;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("exprate").setExecutor(new CommandRate());
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info(String.format("Started, rate is %d", rate));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPickup(PlayerPickupExperienceEvent event) {
        int exp = event.getExperienceOrb().getExperience() * rate;
        event.getExperienceOrb().setExperience(exp);
    }

    private class CommandRate implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                if (!sender.hasPermission("exprate")) {
                    sender.sendMessage(command.getPermissionMessage());
                    return false;
                }
            }

            // Informational; display current rate
            if (args.length == 0) {
                sender.sendMessage(String.format("Current experience rate is %d", rate));
                return true;
            }

            // Parse first arg as new rate
            String arg = args[0].toLowerCase();
            if (arg.equals("reset")) {
                rate = 1;
                String msg = "Reset experience rate to 1";
                sender.sendMessage(msg);
                getLogger().info(msg);
            } else {
                try {
                    rate = Integer.parseInt(arg);
                    String msg = String.format("New experience rate is %d", rate);
                    sender.sendMessage(msg);
                    getLogger().info(msg);
                } catch (NumberFormatException e) {
                    sender.sendMessage(String.format("%s is not a valid integer!", args));
                    return false;
                }
            }
            return true;
        }
    }
}
