package me.kingingo.kcore.Packet.Packets;

import me.kingingo.kcore.Packet.Packet;

public class SERVER_INFO_ALL extends Packet{
	
	public SERVER_INFO_ALL(){}
	public String getName(){
		return "SERVER_INFO_ALL";
	}
	
	public String toString(){
		return getName();
	}
	
}
