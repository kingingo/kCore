package eu.epicpvp.kcore.Packets;

import java.util.UUID;

import eu.epicpvp.dataserver.protocoll.packets.Packet;
import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayerPermissionReload extends Packet{

	public static void register(){
		Packet.registerPacket(0xA3, PacketDirection.TO_CLIENT, PacketPlayerPermissionReload.class);
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
