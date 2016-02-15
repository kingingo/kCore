package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Packet.Packet;

public class ARENA_WIN extends Packet{

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
	
	public ARENA_WIN(){}
	
	public ARENA_WIN(String[] packet){
		Set(packet);
	}
	
	public ARENA_WIN create(String[] packet){
		return new ARENA_WIN(packet);
	}
	
	public ARENA_WIN(String packet){
		Set(packet);
	}
	
	public ARENA_WIN(UUID winner,String server,GameType type,String arena) {
	    this.server=server;
	    this.typ = type;
	    this.winner = winner;
	    this.arena = arena;
	  }
	
	public String getName(){
		return "ARENA_WIN";
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}

	public void Set(String[] packet) {
	 this.winner = UUID.fromString(packet[1]);
	 this.typ = GameType.get(packet[2]);
	 this.arena = packet[3];
	 this.server = packet[4];
	}
	
	public String toString(){
		return String.format(getName() + "-/-%s-/-%s-/-%s-/-%s", new Object[] { this.winner,this.typ.name(),this.arena,this.server });
	}
	
}
