package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Packet.Packet;

public class SERVER_READY extends Packet{

	@Setter
	@Getter
	String server;
	@Getter
	@Setter
	String player;
	
	public SERVER_READY(){}
	
	public SERVER_READY(String[] packet){
		Set(packet);
	}
	
	public SERVER_READY create(String[] packet){
		return new SERVER_READY(packet);
	}
	
	public SERVER_READY(String packet){
		Set(packet);
	}
	
	public SERVER_READY(String player,String server) {
	    this.player=player;
	    this.server=server;
	  }
	
	public String getName(){
		return "SERVER_READY";
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}

	public void Set(String[] split) {
		this.player=split[1];
		this.server=split[2];
	}
	
	public String toString(){
		return String.format(getName() + "-/-%s-/-%s", new Object[] { this.player,this.server });
	}
	
}
