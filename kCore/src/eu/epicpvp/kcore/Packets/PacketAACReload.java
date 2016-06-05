package eu.epicpvp.kcore.Packets;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketAACReload extends Packet{

	public static void register(){
		Packet.registerPacket(0xA5, PacketDirection.TO_CLIENT, PacketAACReload.class);
	}
	
	public PacketAACReload(DataBuffer buffer){
		read(buffer);
	}

	public void read(DataBuffer buffer) {
	}
	
	public void write(DataBuffer buffer){
	}
	
}
