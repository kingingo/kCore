package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Packet.Packet;

public class SERVER_TYPE_CHANGE extends Packet{

	@Setter
	@Getter
	GameType typ;
	
	public SERVER_TYPE_CHANGE(){}
	
	public SERVER_TYPE_CHANGE(String[] packet){
		Set(packet);
	}
	
	public SERVER_TYPE_CHANGE create(String[] packet){
		return new SERVER_TYPE_CHANGE(packet);
	}
	
	SERVER_TYPE_CHANGE(String packet){
		Set(packet);
	}
	
	public SERVER_TYPE_CHANGE(GameType typ) {
	    this.typ = typ;
	  }
	
	public String getName(){
		return "SERVER_TYPE_CHANGE";
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}

	public void Set(String[] packet) {
	 this.typ = GameType.get(packet[1]);
	}
	
	public String toString(){
		//SERVER_TYPE_CHANGE-/-GAMETYPE
		return String.format(getName() + "-/-%s", new Object[] { this.typ.getTyp() });
	}
	
}
