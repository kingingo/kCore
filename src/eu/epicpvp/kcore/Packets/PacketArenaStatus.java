package eu.epicpvp.kcore.Packets;
import eu.epicpvp.dataserver.protocoll.packets.Packet;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameState;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PacketArenaStatus extends Packet{

	public static void register(){
		Packet.registerPacket(0xA1, PacketDirection.TO_CLIENT, PacketArenaStatus.class);
	}

	@Setter
	@Getter
	private GameState state;
	@Setter
	@Getter
	private int team;
	@Getter
	@Setter
	private int online;
	@Setter
	@Getter
	private GameType type;
	@Getter
	private String arena;
	@Getter
	boolean apublic=true;
	@Getter
	private String server;
	@Getter
	private String map;
	@Getter
	@Setter
	private int min_team;
	@Getter
	@Setter
	private int max_team;
	@Getter
	@Setter
	private int teams; //Maximale Teams Team 2-8
	@Getter
	@Setter
	private String kit;

	public PacketArenaStatus(DataBuffer buffer){
		read(buffer);
	}

	public PacketArenaStatus(GameState state,int online,int teams, int team,GameType type, String server, String arena,boolean apublic,String map,int min_team,int max_team,String kit) {
	    this.state = state;
	    this.teams=teams;
	    this.online=online;
	    this.team=team;
	    this.server=server;
	    this.type = type;
	    this.arena = arena;
	    this.apublic=apublic;
	    this.map=map;
	    this.min_team=min_team;
	    this.max_team=max_team;
	    this.kit=kit;
	}

	public void read(DataBuffer buffer) {
		this.state = GameState.values()[buffer.readByte()];
		this.team = buffer.readInt();
		this.type = GameType.values()[buffer.readByte()];
		this.arena = buffer.readString();
		this.apublic=buffer.readBoolean();
		this.server = buffer.readString();
		this.map= buffer.readString();
		this.online= buffer.readInt();
		this.min_team= buffer.readInt();
		this.max_team= buffer.readInt();
		this.kit= buffer.readString();
		this.teams= buffer.readInt();
	}

	public void write(DataBuffer buffer){
		buffer.writeByte(state.ordinal());
		buffer.writeInt(team);
		buffer.writeByte(type.ordinal());
		buffer.writeString(arena);
		buffer.writeBoolean(apublic);
		buffer.writeString(server);
		buffer.writeString(map);
		buffer.writeInt(online);
		buffer.writeInt(min_team);
		buffer.writeInt(max_team);
		buffer.writeString(kit);
		buffer.writeInt(teams);
	}
}
