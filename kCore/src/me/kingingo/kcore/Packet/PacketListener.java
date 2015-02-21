package me.kingingo.kcore.Packet;

import me.kingingo.kcore.Client.Events.ClientReceiveMessageEvent;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class PacketListener extends kListener{

	PacketManager pManager;
	
	public PacketListener(PacketManager pManager) {
		super(pManager.getInstance(), "PacketListener");
		this.pManager=pManager;
	}

	@EventHandler
	public void Receive(ClientReceiveMessageEvent ev){
		Packet packet = pManager.getPacket(ev.getMessage());
		if(packet!=null)Bukkit.getPluginManager().callEvent(new PacketReceiveEvent(packet));
	}
	
}
