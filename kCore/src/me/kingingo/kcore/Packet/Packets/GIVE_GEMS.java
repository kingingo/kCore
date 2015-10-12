package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class GIVE_GEMS extends Packet{

	@Getter
	@Setter
	private String player;
	@Getter
	@Setter
	private int gems;
	
	public GIVE_GEMS(){}
	
	public GIVE_GEMS(String player,int gems){
		this.player=player;
		this.gems=gems;
	}
	
	public GIVE_GEMS(String[] packet){
		Set(packet);
	}
	
	public GIVE_GEMS create(String[] packet){
		return new GIVE_GEMS(packet);
	}
	
	public String getName(){
		return "GIVE_GEMS";
	}
	
	public void Set(String[] split){
		this.player=split[1];
		this.gems=Integer.valueOf(split[2]);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Set(split);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d", getPlayer(),getGems());
	}
	
}
