package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Packet.Packet;

public class SIGN_SEND extends Packet{
	
	@Getter
	private GameType type;
	@Getter
	private int signs;
	
	public SIGN_SEND(){}
	
	public SIGN_SEND(GameType type,int signs){
		this.type=type;
		this.signs=signs;
	}
	
	public SIGN_SEND(String s){
		Set(s);
	}
	
	public SIGN_SEND(String[] s){
		Set(s);
	}
	
	public String getName(){
		return "SIGN_SEND";
	}
	
	public SIGN_SEND create(String[] packet){
		return new SIGN_SEND();
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d",type.getTyp(),signs);
	}
	
	@Override
	public void Set(String[] split) {
		type=GameType.get(split[1]);
		signs=Integer.valueOf(split[2]);
	}
	
	@Override
	public void Set(String s) {
		Set(s.split("-/-"));
	}
	
}
