package eu.epicpvp.kcore.Packets;
import java.util.UUID;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PacketArenaWinner extends Packet{

	public static void register(){
		Packet.registerPacket(0xA2, PacketDirection.TO_CLIENT, PacketArenaWinner.class);
	}
	
	@Setter
	@Getter
	private UUID winner;
	@Setter
	@Getter
	private GameType typ;
	@Getter
	private String arena;
	@Getter
	private String server;
	
	public PacketArenaWinner(DataBuffer buffer){
		read(buffer);
	}
	
	public PacketArenaWinner(UUID winner,String server,GameType type,String arena) {
	    this.server=server;
	    this.typ = type;
	    this.winner = winner;
	    this.arena = arena;
	}

	public void read(DataBuffer buffer) {
		this.winner= buffer.readUUID();
		this.typ = GameType.get(buffer.readString());
		this.arena = buffer.readString();
		this.server = buffer.readString();
	}
	
	public void write(DataBuffer buffer){
		buffer.writeUUID(winner);
		buffer.writeString(typ.name());
		buffer.writeString(arena);
		buffer.writeString(arena);
	}
	
}