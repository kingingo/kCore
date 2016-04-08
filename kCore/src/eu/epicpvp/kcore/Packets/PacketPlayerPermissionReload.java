package eu.epicpvp.kcore.Packets;

import java.util.UUID;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayerPermissionReload extends Packet{

	public static void register(){
		Packet.registerPacket(0xA3, PacketPlayerPermissionReload.class, PacketDirection.TO_CLIENT);
	}
	
	@Getter
	private UUID uuid;
	
	public PacketPlayerPermissionReload(UUID uuid){
		this.uuid=uuid;
	}
	
	public void read(DataBuffer buffer){
		this.uuid=buffer.readUUID();
	}
	
	public void write(DataBuffer buffer){
		buffer.writeUUID(uuid);
	}
}
