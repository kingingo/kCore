package eu.epicpvp.kcore.Packets;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PacketPlayerVote extends Packet{

	public static void register(){
		Packet.registerPacket(0xA4, PacketPlayerVote.class, PacketDirection.TO_CLIENT);
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