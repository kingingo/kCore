package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Packet.Packet;

public class SERVER_STATUS extends Packet{

	@Setter
	@Getter
	GameState state;
	@Setter
	@Getter
	int online;
	@Setter
	@Getter
	int max_online;
	@Setter
	@Getter
	String map;
	@Setter
	@Getter
	GameType typ;
	@Getter
	String id;
	
	public SERVER_STATUS(){}
	
	public SERVER_STATUS(String[] packet){
		Set(packet);
	}
	
	public SERVER_STATUS(GameState state, int online, int max_online, String map, GameType typ, String id) {
	    this.online = online;
	    this.state = state;
	    this.max_online = max_online;
	    this.map = map;
	    this.typ = typ;
	    this.id = id;
	  }
	
	public String getName(){
		return "SERVER_STATUS";
	}

	public void Set(String[] packet) {
	 this.state = GameState.valueOf(packet[1]);
	 this.online = Integer.valueOf(packet[2]).intValue();
	 this.max_online = Integer.valueOf(packet[3]).intValue();
	 this.map = packet[4];
	 this.typ = GameType.valueOf(packet[5]);
	 this.id = packet[6];
	}
	
	public String toString(){
		//SERVER_STATUS-/-STATE-/-ONLINE-/-MAX-/-MAP-/-TYP-/-SERVER
		return String.format(getName() + "-/-%s-/-%d-/-%d-/-%s-/-%s-/-%s", new Object[] { this.state.string(), Integer.valueOf(this.online), Integer.valueOf(this.max_online), this.map, this.typ.string(), this.id });
	}
	
}
