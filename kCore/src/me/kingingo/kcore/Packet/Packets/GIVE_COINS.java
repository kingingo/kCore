package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class GIVE_COINS extends Packet{

	@Getter
	@Setter
	private String player;
	@Getter
	@Setter
	private int coins;
	
	public GIVE_COINS(){}
	
	public GIVE_COINS(String player,int coins){
		this.player=player;
		this.coins=coins;
	}
	
	public GIVE_COINS(String[] packet){
		Set(packet);
	}
	
	public GIVE_COINS create(String[] packet){
		return new GIVE_COINS(packet);
	}
	
	public String getName(){
		return "GIVE_COINS";
	}
	
	public void Set(String[] split){
		this.player=split[1];
		this.coins=Integer.valueOf(split[2]);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Set(split);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d", getPlayer(),getCoins());
	}
	
}
