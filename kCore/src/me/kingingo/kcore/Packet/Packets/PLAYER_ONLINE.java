package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PLAYER_ONLINE extends Packet{

	@Setter
	@Getter
	private String player;
	@Setter
	@Getter
	private String server;
	@Setter
	@Getter
	private String reason;
	@Setter
	@Getter
	private String from_server;
	
	public PLAYER_ONLINE(){}
	
	public PLAYER_ONLINE(String[] packet){
		Set(packet);
	}
	
	public PLAYER_ONLINE(String player,String from_server,String reason,String server){
		this.player=player;
		this.from_server=from_server;
		this.reason=reason;
		this.server=server;
	}
	
	public PLAYER_ONLINE create(String[] packet){
		return new PLAYER_ONLINE(packet);
	}
	
	public String getName(){
		return "PLAYER_ONLINE";
	}
	
	public void Set(String[] split){
		player=split[1];
		from_server=split[2];
		server=split[3];
		reason=split[4];
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s-/-%s", player,from_server,server,reason);
	}
	
}
