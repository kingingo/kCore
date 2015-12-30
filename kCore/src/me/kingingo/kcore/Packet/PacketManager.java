package me.kingingo.kcore.Packet;

import lombok.Getter;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.Packet.Events.PacketSendEvent;
import me.kingingo.kcore.Packet.Packets.ARENA_SETTINGS;
import me.kingingo.kcore.Packet.Packets.ARENA_STATUS;
import me.kingingo.kcore.Packet.Packets.BG_IP_RELOAD;
import me.kingingo.kcore.Packet.Packets.BROADCAST;
import me.kingingo.kcore.Packet.Packets.GIVE_COINS;
import me.kingingo.kcore.Packet.Packets.GIVE_GEMS;
import me.kingingo.kcore.Packet.Packets.HUB_ONLINE;
import me.kingingo.kcore.Packet.Packets.NICK_DEL;
import me.kingingo.kcore.Packet.Packets.NICK_SET;
import me.kingingo.kcore.Packet.Packets.PERMISSION_GROUP_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_REMOVE_ALL;
import me.kingingo.kcore.Packet.Packets.PLAYER_LANGUAGE_CHANGE;
import me.kingingo.kcore.Packet.Packets.PLAYER_ONLINE;
import me.kingingo.kcore.Packet.Packets.PLAYER_VOTE;
import me.kingingo.kcore.Packet.Packets.PROTECTION_CAPTCHA;
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
import me.kingingo.kcore.Packet.Packets.TWIITTER_IS_PLAYER_FOLLOWER;
import me.kingingo.kcore.Packet.Packets.TWITTER_PLAYER_FOLLOW;
import me.kingingo.kcore.Packet.Packets.WORLD_CHANGE_DATA;
import me.kingingo.kcore.Packet.Packets.YOUTUBE_GET_DATA;
import me.kingingo.kcore.Packet.Packets.YOUTUBE_IS_DATA;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketManager {

	@Getter
	private JavaPlugin instance;
	@Getter
	private Client client;
	
	public PacketManager(JavaPlugin instance,Client client){
		this.instance=instance;
		this.client=client;
		new PacketListener(this);
	}
	
	public Packet getPacket(String packet) {	
		if (packet.contains("SERVER_STATUS")) {
		return new SERVER_STATUS(packet);
		}else if (packet.contains("BROADCAST")) {
			return new BROADCAST(packet);
		}else if (packet.contains("SEND_MESSAGE")) {
			return new SEND_MESSAGE(packet);
		}else if (packet.contains("GIVE_GEMS")) {
			return new GIVE_GEMS(packet.split("-/-"));
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
		}else if(packet.contains("ARENA_SETTINGS")){
			return new ARENA_SETTINGS(packet.split("-/-"));
		}else if(packet.contains("ARENA_STATUS")){
			return new ARENA_STATUS(packet.split("-/-"));
		}else if(packet.contains("PLAYER_VOTE")){
			return new PLAYER_VOTE(packet.split("-/-"));
		}else if(packet.contains("NICK_DEL")){
			return new NICK_DEL(packet.split("-/-"));
		}else if(packet.contains("NICK_SET")){
			return new NICK_SET(packet.split("-/-"));
		}else if(packet.contains("PLAYER_LANGUAGE_CHANGE")){
			return new PLAYER_LANGUAGE_CHANGE(packet.split("-/-"));
		}else if(packet.contains("PROTECTION_CAPTCHA")){
			return new PROTECTION_CAPTCHA(packet.split("-/-"));
		}else if(packet.contains("TWIITTER_IS_PLAYER_FOLLOWER")){
			return new TWIITTER_IS_PLAYER_FOLLOWER(packet.split("-/-"));
		}else if(packet.contains("TWITTER_PLAYER_FOLLOW")){
			return new TWITTER_PLAYER_FOLLOW(packet.split("-/-"));
		}else if(packet.contains("PLAYER_ONLINE")){
			return new PLAYER_ONLINE(packet.split("-/-"));
		}else if(packet.contains("GIVE_COINS")){
			return new GIVE_COINS(packet.split("-/-"));
		}else if(packet.contains("BG_IP_RELOAD")){
			return new BG_IP_RELOAD(packet.split("-/-"));
		}
	 return null;
	}
	
	public void SendPacket(String toServer,Packet packet){
		Bukkit.getPluginManager().callEvent(new PacketSendEvent(packet,toServer));
		getClient().sendMessageToServer(toServer+"=/="+packet.toString());
	}
	
}
