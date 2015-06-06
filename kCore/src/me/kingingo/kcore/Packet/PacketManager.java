package me.kingingo.kcore.Packet;

import lombok.Getter;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Packet.Events.PacketSendEvent;
import me.kingingo.kcore.Packet.Packets.BROADCAST;
import me.kingingo.kcore.Packet.Packets.HUB_ONLINE;
import me.kingingo.kcore.Packet.Packets.NOT_SAVE_COINS;
import me.kingingo.kcore.Packet.Packets.PERMISSION_GROUP_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_REMOVE_ALL;
import me.kingingo.kcore.Packet.Packets.SEND_MESSAGE;
import me.kingingo.kcore.Packet.Packets.SERVER_INFO_ALL;
import me.kingingo.kcore.Packet.Packets.SERVER_READY;
import me.kingingo.kcore.Packet.Packets.SERVER_RESET;
import me.kingingo.kcore.Packet.Packets.SERVER_SETTINGS;
import me.kingingo.kcore.Packet.Packets.SERVER_STATUS;
import me.kingingo.kcore.Packet.Packets.SERVER_TYPE_CHANGE;
import me.kingingo.kcore.Packet.Packets.TEAMSPEAK_ADD_CLIENT_GROUP;
import me.kingingo.kcore.Packet.Packets.TEAMSPEAK_CLIENT;
import me.kingingo.kcore.Packet.Packets.TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP;
import me.kingingo.kcore.Packet.Packets.TEAMSPEAK_REMOVE_CLIENT_GROUP;
import me.kingingo.kcore.Packet.Packets.VERSUS_SETTINGS;
import me.kingingo.kcore.Packet.Packets.WORLD_CHANGE_DATA;
import me.kingingo.kcore.Packet.Packets.YOUTUBE_GET_DATA;
import me.kingingo.kcore.Packet.Packets.YOUTUBE_IS_DATA;

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
		return new SERVER_STATUS(packet);
		}else if (packet.contains("BROADCAST")) {
			return new BROADCAST(packet);
		}else if (packet.contains("SEND_MESSAGE")) {
			return new SEND_MESSAGE(packet);
		}else if (packet.contains("NOT_SAVE_COINS")) {
			return new NOT_SAVE_COINS(packet.split("-/-"));
		}else if (packet.contains("SERVER_INFO_ALL")) {
			return new SERVER_INFO_ALL();
		}else if (packet.contains("PERMISSION_USER_RELOAD")) {
			return new PERMISSION_USER_RELOAD(packet.split("-/-"));
		}else if (packet.contains("PERMISSION_GROUP_RELOAD")) {
			return new PERMISSION_GROUP_RELOAD(packet);
		}else if (packet.contains("TEAMSPEAK_ADD_CLIENT_GROUP")) {
			return new TEAMSPEAK_ADD_CLIENT_GROUP(packet.split("-/-"));
		}else if (packet.contains("TEAMSPEAK_REMOVE_CLIENT_GROUP")) {
			return new TEAMSPEAK_REMOVE_CLIENT_GROUP(packet.split("-/-"));
		}else if (packet.contains("TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP")) {
			return new TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(packet.split("-/-"));
		}else if (packet.contains("TEAMSPEAK_CLIENT")) {
			return new TEAMSPEAK_CLIENT(packet.split("-/-"));
		}else if (packet.contains("YOUTUBE_GET_DATA")) {
			return new YOUTUBE_GET_DATA(packet.split("-/-"));
		}else if (packet.contains("YOUTUBE_IS_DATA")) {
			return new YOUTUBE_IS_DATA(packet.split("-/-"));
		}else if (packet.contains("PERMISSION_GROUP_RELOAD")) {
			return new PERMISSION_GROUP_RELOAD(packet.split("-/-"));
		}else if (packet.contains("SERVER_TYPE_CHANGE")) {
			return new SERVER_TYPE_CHANGE(packet.split("-/-"));
		}else if(packet.contains("PERMISSION_USER_REMOVE_ALL")){
			return new PERMISSION_USER_REMOVE_ALL(packet.split("-/-"));
		}else if(packet.contains("WORLD_CHANGE_DATA")){
			return new WORLD_CHANGE_DATA(packet.split("-/-"));
		}else if(packet.contains("HUB_ONLINE")){
			return new HUB_ONLINE(packet.split("-/-"));
		}else if(packet.contains("SERVER_SETTINGS")){
			return new SERVER_SETTINGS(packet.split("-/-"));
		}else if(packet.contains("SERVER_READY")){
			return new SERVER_READY(packet.split("-/-"));
		}else if(packet.contains("SERVER_RESET")){
			return new SERVER_RESET(packet.split("-/-"));
		}else if(packet.contains("VERSUS_SETTINGS")){
			return new VERSUS_SETTINGS(packet.split("-/-"));
		}
	 return null;
	}
	
	public void SendPacket(String toServer,Packet packet){
		Bukkit.getPluginManager().callEvent(new PacketSendEvent(packet,toServer));
		c.sendMessageToServer(toServer+"=/="+packet.toString());
	}
	
}
