package me.kingingo.kcore.Packet.Packets;

import me.kingingo.kcore.Packet.Packet;

public class SERVER_RESET extends Packet{
	
	public SERVER_RESET(){}
	
	public SERVER_RESET(String[] packet){
		Set(packet);
	}
	
	public SERVER_RESET create(String[] packet){
		return new SERVER_RESET(packet);
	}
	
	public String getName(){
		return "SERVER_RESET";
	}
	
	public void Set(String packet){
	}

	public void Set(String[] split) {
	}
	
	public String toString(){
		return getName();
	}
	
}
