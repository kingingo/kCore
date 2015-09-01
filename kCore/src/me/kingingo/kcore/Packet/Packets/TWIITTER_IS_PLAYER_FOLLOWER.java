package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class TWIITTER_IS_PLAYER_FOLLOWER extends Packet{

	@Getter
	@Setter
	private String twitterUsername;
	@Getter
	@Setter
	private String player;
	
	public TWIITTER_IS_PLAYER_FOLLOWER(){}
	
	public TWIITTER_IS_PLAYER_FOLLOWER(String[] packet){
		Set(packet);
	}
	
	public TWIITTER_IS_PLAYER_FOLLOWER(String twitterUsername,String player){
		this.twitterUsername=twitterUsername;
		this.player=player;
	}
	
	public TWIITTER_IS_PLAYER_FOLLOWER create(String[] packet){
		return new TWIITTER_IS_PLAYER_FOLLOWER(packet);
	}
	
	public String getName(){
		return "TWIITTER_IS_PLAYER_FOLLOWER";
	}
	
	public void Set(String[] split){
		this.twitterUsername=split[1];
		this.player=split[2];
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Set(split);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s", this.twitterUsername,player);
	}
	
}
