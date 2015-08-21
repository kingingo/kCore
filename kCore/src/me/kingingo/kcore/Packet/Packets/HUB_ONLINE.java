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
	@Getter
	@Setter
	private int tps;
	
	public HUB_ONLINE(){}
	
	public HUB_ONLINE(String[] packet){
		Set(packet);
	}
	
	public HUB_ONLINE(String server,int online,int tps){
		this.server=server;
		this.online=online;
		this.tps=tps;
	}
	
	public HUB_ONLINE create(String[] packet){
		return new HUB_ONLINE(packet);
	}
	
	public String getName(){
		return "HUB_ONLINE";
	}
	
	public void Set(String[] split){
		this.server=split[1];
		this.online=Integer.valueOf(split[2]);
		this.tps=Integer.valueOf(split[3]);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Set(split);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d-/-%d", server,online,tps);
	}
	
}
