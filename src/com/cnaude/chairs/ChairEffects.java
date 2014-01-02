/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cnaude.chairs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author cnaude
 */
public class ChairEffects {

    Chairs plugin;
    int taskID;

    public ChairEffects(Chairs plugin) {
        this.plugin = plugin;
    }
    
    public void startHealing() {
        effectsTask();
    }

    public void cancelHealing() {
        plugin.getServer().getScheduler().cancelTask(taskID);
        taskID = 0;
    }
    
    public void restartHealing() {
    	cancelHealing();
        startHealing();
    }

    private void effectsTask() {
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (plugin.getPlayerSitData().isSitting(p)) {
                        if (p.hasPermission("chairs.sit.health")) {
                            double pHealthPcnt = ((double) p.getHealth()) / (double) p.getMaxHealth() * 100d;
                            if ((pHealthPcnt < plugin.sitMaxHealth) && (p.getHealth() < p.getMaxHealth())) {
                                double newHealth = plugin.sitHealthPerInterval + p.getHealth();
                                if (newHealth > p.getMaxHealth()) {
                                    newHealth = p.getMaxHealth();
                                }
                                p.setHealth(newHealth);
                            }
                        }
                    }
                }
            }
        }, plugin.sitHealInterval, plugin.sitHealInterval);
    }
}
