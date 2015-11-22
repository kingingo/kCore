package me.kingingo.kcore.Packet.Packets;

import me.kingingo.kcore.Packet.Packet;

public class BG_IP_RELOAD extends Packet{
	
	public BG_IP_RELOAD(){}
	
	public BG_IP_RELOAD(String[] split){
		Set(split);
	}
	
	public BG_IP_RELOAD create(String[] packet){
		return new BG_IP_RELOAD(packet);
	}
	
	public String getName(){
		return "BG_IP_RELOAD";
	}
	
	public void Set(String[] split){
		
	}
	
	public void Set(String packet){
		
	}
	
	public String toString(){
		return getName();
	}
	
}
