package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PLAYER_VOTE extends Packet{

	@Setter
	@Getter
	String player;
	@Getter
	@Setter
	UUID uuid;
	
	public PLAYER_VOTE(){}
	
	public PLAYER_VOTE(String[] packet){
		Set(packet);
	}
	
	public PLAYER_VOTE(String player,UUID uuid){
		this.player=player;
		this.uuid=uuid;
	}
	
	public PLAYER_VOTE create(String[] packet){
		return new PLAYER_VOTE(packet);
	}
	
	public String getName(){
		return "PLAYER_VOTE";
	}
	
	public void Set(String[] split){
		player=split[1];
		uuid=UUID.fromString(split[2]);
	}
	
	public void Set(String packet){
		player=packet;
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s", player,uuid);
	}
	
}
