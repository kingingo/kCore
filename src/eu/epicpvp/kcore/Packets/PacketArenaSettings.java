package eu.epicpvp.kcore.Packets;
import org.bukkit.entity.Player;

import eu.epicpvp.dataserver.protocoll.packets.Packet;
import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Arena.ArenaType;
import eu.epicpvp.kcore.Enum.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PacketArenaSettings extends Packet{

	public static void register(){
		Packet.registerPacket(0xA0, PacketDirection.TO_CLIENT, PacketArenaSettings.class);
	}

	@Getter
	@Setter
	private ArenaType type;
	@Getter
	@Setter
	private String player;
	@Getter
	@Setter
	private Team team;
	@Getter
	@Setter
	private String kit;
	@Getter
	@Setter
	private String arena;
	@Getter
	@Setter
	private int min_team;
	@Getter
	@Setter
	private int max_team;

	public PacketArenaSettings(ArenaType type,String arena,String kit,Player player,Team team,int min_team,int max_team){
		this.type=type;
		this.player=player.getName();
		this.kit=kit;
		this.arena=arena;
		this.team=team;
		this.max_team=max_team;
		this.min_team=min_team;
	}

	public PacketArenaSettings(DataBuffer buffer){
		read(buffer);
	}

	public void read(DataBuffer buffer){
		this.type=ArenaType.valueOf(buffer.readString());
		this.kit=buffer.readString();
		this.player=buffer.readString();
		this.team=Team.valueOf(buffer.readString());
		this.arena=buffer.readString();
		this.min_team=Integer.valueOf(buffer.readInt());
		this.max_team=Integer.valueOf(buffer.readInt());
	}

	public void write(DataBuffer buffer){
		buffer.writeString(type.name());
		buffer.writeString(kit);
		buffer.writeString(player);
		buffer.writeString(team.name());
		buffer.writeString(arena);
		buffer.writeInt(min_team);
		buffer.writeInt(max_team);
	}
}
