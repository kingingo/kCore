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
	private GameType typ;
	@Getter
	private String arena;
	@Getter
	boolean apublic=true;
	@Getter
	private String server;
	
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
	
	public ARENA_STATUS(GameState state, int teams,GameType typ, String server, String arena,boolean apublic) {
	    this.state = state;
	    this.teams=teams;
	    this.server=server;
	    this.typ = typ;
	    this.arena = arena;
	    this.apublic=apublic;
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
	 this.typ = GameType.valueOf(packet[3]);
	 this.arena = packet[4];
	 this.apublic=Boolean.valueOf(packet[5]);
	 this.server = packet[6];
	}
	
	public String toString(){
		//ARENA_STATUS-/-STATE-/-TEAMS-/-TYP-/-ID-/-PUBLIC
		return String.format(getName() + "-/-%s-/-%d-/-%s-/-%s-/-%s-/-%s", new Object[] { this.state.string(), Integer.valueOf(teams),typ.name(),arena,apublic,server });
	}
	
}
