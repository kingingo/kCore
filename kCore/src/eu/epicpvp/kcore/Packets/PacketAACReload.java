package eu.epicpvp.kcore.Packets;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.Packet;

public class PacketAACReload extends Packet{

	public static void register(){
		registerToClient();
		registerToServer();
	}
	
	public static void registerToClient(){
		Packet.registerPacket(0xA4, PacketPlayerVote.class, PacketDirection.TO_CLIENT);
	}
	
	public static void registerToServer(){
		Packet.registerPacket(0xA4, PacketPlayerVote.class, PacketDirection.TO_SERVER);
	}
	
	public PacketAACReload(){}
	
	public PacketAACReload(DataBuffer buffer){
		read(buffer);
	}

	public void read(DataBuffer buffer) {
	}
	
	public void write(DataBuffer buffer){
	}
	
}
