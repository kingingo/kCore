package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import me.kingingo.kcore.Enum.Team;
import me.kingingo.kcore.Packet.Packet;
import me.kingingo.kcore.Versus.VersusType;

import org.bukkit.entity.Player;

public class VERSUS_SETTINGS extends Packet{

	@Getter
	private VersusType type;
	@Getter
	private String player;
	@Getter
	private Team team;
	@Getter
	private String kit;
	@Getter
	private String arena;
	
	public VERSUS_SETTINGS(VersusType type,String arena,String kit,Player player,Team team){
		this.type=type;
		this.player=player.getName();
		this.kit=kit;
		this.arena=arena;
		this.team=team;
	}
	
	public VERSUS_SETTINGS(String[] data){
		Set(data);
	}
	
	public VERSUS_SETTINGS(String data){
		Set(data);
	}
	
	public VERSUS_SETTINGS create(String[] packet){
		return new VERSUS_SETTINGS(packet);
	}
	
	public void Set(String data){
		Set(data.split("-/-"));
	}
	
	public void Set(String[] split){
		this.type=VersusType.valueOf(split[1]);
		this.kit=split[2];
		this.player=split[3];
		this.team=Team.valueOf(split[4]);
		this.arena=split[5];
	}
	
	public String getName(){
		return "VERSUS_SETTINGS";
	}
	
	//VERSUS_SETTINGS-/-TYPE-/-KIT-/-PLAYER-/-TEAM-/-ARENA
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s-/-%s-/-%s", type.name(), kit ,player,team.Name(),arena);
	}
	
}
