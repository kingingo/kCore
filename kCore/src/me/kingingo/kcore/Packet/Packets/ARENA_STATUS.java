package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Packet.Packet;

public class ARENA_STATUS extends Packet{

	@Setter
	@Getter
	private GameState state;
	@Setter
	@Getter
	private int teams;
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
	private String kit;
	
	public ARENA_STATUS(){}
	
	public ARENA_STATUS(String[] packet){
		Set(packet);
	}
	
	public ARENA_STATUS create(String[] packet){
		return new ARENA_STATUS(packet);
	}
	
	public ARENA_STATUS(String packet){
		Set(packet);
	}
	
	public ARENA_STATUS(GameState state,int online, int teams, int team,GameType typ, String server, String arena,boolean apublic,String map,int min_team,int max_team,String kit) {
	    this.state = state;
	    this.online=online;
	    this.teams=teams;
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
	
	public String getName(){
		return "ARENA_STATUS";
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}

	public void Set(String[] packet) {
	 this.state = GameState.valueOf(packet[1]);
	 this.teams = Integer.valueOf(packet[2]).intValue();
	 this.team = Integer.valueOf(packet[3]).intValue();
	 this.typ = GameType.valueOf(packet[4]);
	 this.arena = packet[5];
	 this.apublic=Boolean.valueOf(packet[6]);
	 this.server = packet[7];
	 this.map= packet[8];
	 this.online= Integer.valueOf(packet[9]);
	 this.min_team= Integer.valueOf(packet[10]);
	 this.max_team= Integer.valueOf(packet[11]);
	 this.kit= packet[12];
	}
	
	public String toString(){
		//ARENA_STATUS-/-STATE-/-TEAMS-/-TYP-/-ID-/-PUBLIC
		return String.format(getName() + "-/-%s-/-%d-/-%d-/-%s-/-%s-/-%s-/-%s-/-%s-/-%d-/-%d-/-%d-/-%s", new Object[] { this.state.string(), Integer.valueOf(teams),Integer.valueOf(team),typ.name(),arena,apublic,server,map,online,min_team,max_team,kit });
	}
	
}
