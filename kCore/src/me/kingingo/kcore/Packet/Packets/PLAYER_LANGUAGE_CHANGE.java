package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Language.LanguageType;
import me.kingingo.kcore.Packet.Packet;

public class PLAYER_LANGUAGE_CHANGE extends Packet{

	@Setter
	@Getter
	UUID uuid;
	@Setter
	@Getter
	LanguageType type;
	
	public PLAYER_LANGUAGE_CHANGE(){}
	
	public PLAYER_LANGUAGE_CHANGE(String[] packet){
		Set(packet);
	}
	
	public PLAYER_LANGUAGE_CHANGE(UUID uuid,LanguageType type){
		this.uuid=uuid;
		this.type=type;
	}
	
	public PLAYER_LANGUAGE_CHANGE create(String[] packet){
		return new PLAYER_LANGUAGE_CHANGE(packet);
	}
	
	public String getName(){
		return "PLAYER_LANGUAGE_CHANGE";
	}
	
	public void Set(String[] split){
		uuid=UUID.fromString(split[1]);
		type=LanguageType.get(split[2]);
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s", uuid,type.getDef());
	}
	
}
