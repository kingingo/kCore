package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class SERVER_SETTINGS extends Packet{
	
	@Getter
	@Setter
	public String game;
	@Getter
	@Setter
	public String player;
	@Getter
	@Setter
	private String infos;
	@Getter
	@Setter
	private String hub;
	@Getter
	@Setter
	private boolean apublic=true;
	
	public SERVER_SETTINGS(){}
	

	public SERVER_SETTINGS(String game,String infos,String player,String hub,boolean apublic){
		this.game=game;
		this.infos=infos;
		this.player=player;
		this.hub=hub;
		this.apublic=apublic;
	}
	
	public SERVER_SETTINGS(String s){
		Set(s);
	}
	
	public SERVER_SETTINGS(String[] s){
		Set(s);
	}
	
	public String getName(){
		return "SERVER_SETTINGS";
	}
	
	public SERVER_SETTINGS create(String[] packet){
		return new SERVER_SETTINGS();
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s-/-%s-/-%s", getGame(),getInfos(),getPlayer(),getHub(),apublic);
	}
	
	@Override
	public void Set(String[] split) {
		game=split[1];
		infos=split[2];
		player=split[3];
		hub=split[4];
		apublic=Boolean.valueOf(split[5]);
	}
	
	@Override
	public void Set(String s) {
		Set(s.split("-/-"));
	}
	
}
