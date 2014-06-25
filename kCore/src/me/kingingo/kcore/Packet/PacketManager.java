package me.kingingo.kcore.Packet;

import lombok.Getter;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Packet.Events.PacketSendEvent;
import me.kingingo.kcore.Packet.Packets.SERVER_STATUS;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketManager {

	@Getter
	private JavaPlugin instance;
	@Getter
	private Client c;
	
	public PacketManager(JavaPlugin instance,Client c){
		this.instance=instance;
		this.c=c;
		new PacketListener(this);
	}
	
	public Packet getPacket(String packet) {
	 if (packet.contains("SERVER_STATUS")) {
		return new SERVER_STATUS(packet.split("-/-"));
		}
	 return null;
	}
	
	public void SendPacket(String toServer,Packet packet){
		Bukkit.getPluginManager().callEvent(new PacketSendEvent(packet,toServer));
		c.sendMessageToServer(toServer+"-/-"+packet.toString());
	}
	
}
