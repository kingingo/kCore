package me.kingingo.kcore.Packet.Packets;

import me.kingingo.kcore.Packet.Packet;

public class SIGNS_GET extends Packet{
	
	public SIGNS_GET(){}
	
	public SIGNS_GET(String s){
		Set(s);
	}
	
	public SIGNS_GET(String[] s){
		Set(s);
	}
	
	public String getName(){
		return "SIGNS_GET";
	}
	
	public SIGNS_GET create(String[] packet){
		return new SIGNS_GET();
	}
	
	public String toString(){
		return String.format(getName());
	}
	
	@Override
	public void Set(String[] split) {
	}
	
	@Override
	public void Set(String s) {
	}
	
}
