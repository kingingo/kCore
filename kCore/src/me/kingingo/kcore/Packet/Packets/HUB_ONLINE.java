package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class HUB_ONLINE extends Packet{

	@Getter
	@Setter
	private String server;
	@Getter
	@Setter
	private int online;
	
	public HUB_ONLINE(){}
	
	public HUB_ONLINE(String[] packet){
		Set(packet);
	}
	
	public HUB_ONLINE(String server,int online){
		this.server=server;
		this.online=online;
	}
	
	public HUB_ONLINE create(String[] packet){
		return new HUB_ONLINE(packet);
	}
	
	public String getName(){
		return "HUB_ONLINE";
	}
	
	public void Set(String[] split){
		server=split[1];
		this.online=Integer.valueOf(split[2]);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		server=split[1];
		this.online=Integer.valueOf(split[2]);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d", server,online);
	}
	
}
