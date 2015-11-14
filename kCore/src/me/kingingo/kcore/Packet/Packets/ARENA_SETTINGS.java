package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Arena.ArenaType;
import me.kingingo.kcore.Enum.Team;
import me.kingingo.kcore.Packet.Packet;

import org.bukkit.entity.Player;

public class ARENA_SETTINGS extends Packet{

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
	
	public ARENA_SETTINGS(ArenaType type,String arena,String kit,Player player,Team team,int min_team,int max_team){
		this.type=type;
		this.player=player.getName();
		this.kit=kit;
		this.arena=arena;
		this.team=team;
		this.max_team=max_team;
		this.min_team=min_team;
	}
	
	public ARENA_SETTINGS(String[] data){
		Set(data);
	}
	
	public ARENA_SETTINGS(String data){
		Set(data);
	}
	
	public ARENA_SETTINGS create(String[] packet){
		return new ARENA_SETTINGS(packet);
	}
	
	public void Set(String data){
		Set(data.split("-/-"));
	}
	
	public void Set(String[] split){
		this.type=ArenaType.valueOf(split[1]);
		this.kit=split[2];
		this.player=split[3];
		this.team=Team.valueOf(split[4]);
		this.arena=split[5];
		this.min_team=Integer.valueOf(split[6]);
		this.max_team=Integer.valueOf(split[7]);
	}
	
	public String getName(){
		return "ARENA_SETTINGS";
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s-/-%s-/-%s-/-%d-/-%d", type.name(), kit ,player,team.Name(),arena,min_team,max_team);
	}
	
}
