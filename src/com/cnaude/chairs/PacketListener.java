package com.cnaude.chairs;

import org.bukkit.entity.Player;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketListener {
	
	private ProtocolManager pm;
	private Chairs pluginInstance;
	public PacketListener(ProtocolManager pm, Chairs plugin)
	{
		this.pm = pm;
		this.pluginInstance = plugin;
		playerDismountListener();
		falledPlayerDismountListener();
	}
	
	private void playerDismountListener()
	{
		pm.getAsynchronousManager().registerAsyncHandler(
				new PacketAdapter(PacketAdapter
						.params(pluginInstance, Packets.Client.PLAYER_INPUT)
						.clientSide()
						.listenerPriority(ListenerPriority.HIGHEST)
						.optionIntercept()
		)
		{
				@Override
				public void onPacketReceiving(final PacketEvent e) 
				{
					if (!e.isCancelled())
					{
						final Player player = e.getPlayer();
						if (e.getPacket().getBooleans().getValues().get(1))
						{
							//just eject player if he is sitting on chair
							if (pluginInstance.sit.containsKey(player.getName()))
							{
								pluginInstance.unSitPlayer(player,false);
							}
						}
					}
				}
		}).syncStart();
	}
	
	private void falledPlayerDismountListener()
	{
		pm.getAsynchronousManager().registerAsyncHandler(
				new PacketAdapter(PacketAdapter
						.params(pluginInstance, Packets.Client.ENTITY_ACTION)
						.clientSide()
						.listenerPriority(ListenerPriority.HIGHEST)
						.optionIntercept()
		)
		{
				@Override
				public void onPacketReceiving(final PacketEvent e) 
				{
					if (!e.isCancelled())
					{
						//eject player if he is in chair and tryes to sneak (it is impossible unless arrow will disappear in client due to server lags)
						Player player = e.getPlayer();
						if (pluginInstance.sit.containsKey(player.getName()))
						{
							pluginInstance.unSitPlayer(player,false);
						}
					}
				}
		}).syncStart();
	}
	
}
