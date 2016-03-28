package eu.epicpvp.kcore.Packets;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import dev.wolveringer.dataserver.gamestats.GameState;
import lombok.Getter;
import lombok.Setter;

public class PacketArenaStatus extends Packet{
	
	public static void register(){
		registerToClient();
		registerToServer();
	}
	
	public static void registerToClient(){
		Packet.registerPacket(0xA1, PacketArenaStatus.class, PacketDirection.TO_CLIENT);
	}
	
	public static void registerToServer(){
		Packet.registerPacket(0xA1, PacketArenaStatus.class, PacketDirection.TO_SERVER);
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
	private GameType typ;
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
	
	public PacketArenaStatus(){}
	
	public PacketArenaStatus(DataBuffer buffer){
		read(buffer);
	}
	
	public PacketArenaStatus(GameState state,int online,int teams, int team,GameType typ, String server, String arena,boolean apublic,String map,int min_team,int max_team,String kit) {
	    this.state = state;
	    this.teams=teams;
	    this.online=online;
	    this.team=team;
	    this.server=server;
	    this.typ = typ;
	    this.arena = arena;
	    this.apublic=apublic;
	    this.map=map;
	    this.min_team=min_team;
	    this.max_team=max_team;
	    this.kit=kit;
	}

	public void read(DataBuffer buffer) {
		this.state = GameState.valueOf(buffer.readString());
		this.team = buffer.readInt();
		this.typ = GameType.get(buffer.readString());
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
		buffer.writeString(state.name());
		buffer.writeInt(team);
		buffer.writeString(typ.name());
		buffer.writeString(arena);
		buffer.writeBoolean(apublic);
		buffer.writeString(server);
		buffer.writeString(map);
		buffer.writeInt(online);
		buffer.writeInt(min_team);
		buffer.writeInt(max_team);
		buffer.writeString(kit);
		buffer.writeInt(max_team);
	}
}