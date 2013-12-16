package com.cnaude.chairs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandRestrict implements Listener {
	
	private Chairs plugin;
	public CommandRestrict(Chairs plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event)
    {
    	Player player = event.getPlayer();
    	String playercommand = event.getMessage().toLowerCase();
    	if (plugin.sit.containsKey(player.getName()))
    	{
			if (plugin.sitDisableAllCommands)
			{
				event.setCancelled(true);
				player.sendMessage(plugin.msgCommandRestricted);
				return;
			}
    		for (String disabledCommand : plugin.sitDisabledCommands)
    		{
    			if (disabledCommand.startsWith(playercommand))
    			{
    				String therest = disabledCommand.replace(playercommand, "");
    				if (therest.isEmpty() || therest.startsWith(" "))
    				{
    					event.setCancelled(true);
    					player.sendMessage(plugin.msgCommandRestricted);
    					return;
    				}
    			}
    		}
    	}
    }

}
