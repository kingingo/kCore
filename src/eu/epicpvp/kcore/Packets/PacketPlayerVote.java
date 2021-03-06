package eu.epicpvp.kcore.Packets;
import eu.epicpvp.dataserver.protocoll.packets.Packet;
import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PacketPlayerVote extends Packet{

	public static void register(){
		Packet.registerPacket(0xA4, PacketDirection.TO_CLIENT, PacketPlayerVote.class);
	}

	@Getter
	@Setter
	private String playername;

	public PacketPlayerVote(DataBuffer buffer){
		read(buffer);
	}

	public PacketPlayerVote(String playername) {
	    this.playername=playername;
	}

	public void read(DataBuffer buffer) {
		this.playername = buffer.readString();
	}

	public void write(DataBuffer buffer){
		buffer.writeString(this.playername);
	}

}
