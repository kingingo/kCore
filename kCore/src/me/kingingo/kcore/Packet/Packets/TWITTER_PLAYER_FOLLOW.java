package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class TWITTER_PLAYER_FOLLOW extends Packet{

	@Getter
	@Setter
	private String twitterUsername;
	@Getter
	@Setter
	private String player;
	@Getter
	@Setter
	private boolean follow;
	
	public TWITTER_PLAYER_FOLLOW(){}
	
	public TWITTER_PLAYER_FOLLOW(String[] packet){
		Set(packet);
	}
	
	public TWITTER_PLAYER_FOLLOW(String twitterUsername,String player,boolean follow){
		this.twitterUsername=twitterUsername;
		this.player=player;
		this.follow=follow;
	}
	
	public TWITTER_PLAYER_FOLLOW create(String[] packet){
		return new TWITTER_PLAYER_FOLLOW(packet);
	}
	
	public String getName(){
		return "TWITTER_PLAYER_FOLLOW";
	}
	
	public void Set(String[] split){
		this.twitterUsername=split[1];
		this.player=split[2];
		this.follow=(split[3].equalsIgnoreCase("true")?true:false);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Set(split);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s", this.twitterUsername,this.player,(follow?"true":"false"));
	}
	
}
