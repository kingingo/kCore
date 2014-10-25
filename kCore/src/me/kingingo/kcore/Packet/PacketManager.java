package me.kingingo.kcore.Packet;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Packet.Events.PacketSendEvent;
import me.kingingo.kcore.Packet.Packets.BROADCAST;
import me.kingingo.kcore.Packet.Packets.SEND_MESSAGE;
import me.kingingo.kcore.Packet.Packets.SERVER_INFO_ALL;
import me.kingingo.kcore.Packet.Packets.SERVER_STATUS;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketManager {

	@Getter
	private JavaPlugin instance;
	@Getter
	private Client c;
	private HashMap<String,Packet> packet_list=new HashMap<>();
	
	public PacketManager(JavaPlugin instance,Client c){
		this.instance=instance;
		this.c=c;
		new PacketListener(this);
		packet_list.put("SERVER_STATUS",new SERVER_STATUS());
		packet_list.put("BROADCAST",new BROADCAST());
		packet_list.put("SEND_MESSAGE",new SEND_MESSAGE());
		packet_list.put("SERVER_INFO_ALL",new SERVER_INFO_ALL());
	}
	
	public Packet getPacket(String packet) {	
		
		for(String n : packet_list.keySet()){
			if(packet.contains(n)){
				return packet_list.get(n).create(packet.split("-/-"));
			}
		}
		
//		if (packet.contains("SERVER_STATUS")) {
//		return new SERVER_STATUS(packet);
//		}else if (packet.contains("BROADCAST")) {
//			return new BROADCAST(packet);
//		}else if (packet.contains("SEND_MESSAGE")) {
//			return new SEND_MESSAGE(packet);
//		}else if (packet.contains("SERVER_INFO_ALL")) {
//			return new SERVER_INFO_ALL();
//		}
	 return null;
	}
	
	public void SendPacket(String toServer,Packet packet){
		Bukkit.getPluginManager().callEvent(new PacketSendEvent(packet,toServer));
		c.sendMessageToServer(toServer+"=/="+packet.toString());
	}
	
}
