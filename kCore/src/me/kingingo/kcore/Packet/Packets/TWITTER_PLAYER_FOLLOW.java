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
	@Getter
	@Setter
	private long twitterID;
	@Getter
	@Setter
	private String reason;
	
	public TWITTER_PLAYER_FOLLOW(){}
	
	public TWITTER_PLAYER_FOLLOW(String[] packet){
		Set(packet);
	}
	
	public TWITTER_PLAYER_FOLLOW(String twitterUsername,String player,boolean follow,String reason,long twitterID){
		this.twitterUsername=twitterUsername;
		this.player=player;
		this.follow=follow;
		this.twitterID=twitterID;
		this.reason=reason;
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
		this.reason=split[3];
		this.twitterID=Long.valueOf(split[4]);
		this.follow=(split[5].equalsIgnoreCase("true")?true:false);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Set(split);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s-/-%s-/-%s", this.twitterUsername,this.player,(this.reason==null?"NULL":this.reason),this.twitterID,(follow?"true":"false"));
	}
	
}
