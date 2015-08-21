package me.kingingo.kcore.Packet.Packets;

import me.kingingo.kcore.Packet.Packet;

public class SERVER_INFO_ALL extends Packet{
	
	public SERVER_INFO_ALL(){}
	
	public String getName(){
		return "SERVER_INFO_ALL";
	}
	
	public SERVER_INFO_ALL create(String[] packet){
		return new SERVER_INFO_ALL();
	}
	
	public String toString(){
		return getName();
	}
	
	@Override
	public void Set(String[] split) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Set(String split) {
		// TODO Auto-generated method stub
		
	}
	
}
