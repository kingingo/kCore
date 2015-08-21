package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import me.kingingo.kcore.Packet.Packet;

public class BUNGEECORD_ZEITBAN extends Packet{
	
	@Getter
	private String banned;
	@Getter
	private String banner;
	@Getter
	private String reason;
	@Getter
	private String time;
	@Getter
	private String typ;
	
	public BUNGEECORD_ZEITBAN(){}
	
	public BUNGEECORD_ZEITBAN(String banned,String banner,String time,String typ){
		this.banned=banned;
		this.banner=banner;
		this.reason="Kein Grund";
		this.time=time;
		this.typ=typ;
	}
	
	public BUNGEECORD_ZEITBAN(String banned,String banner,String reason,String time,String typ){
		this.banned=banned;
		this.banner=banner;
		this.reason=reason;
		this.time=time;
		this.typ=typ;
	}
	
	public BUNGEECORD_ZEITBAN create(String[] packet){
		return new BUNGEECORD_ZEITBAN(packet);
	}
	
	public BUNGEECORD_ZEITBAN(String[] packet){
		Set(packet);
	}
	
	@Override
	public String getName() {
		return "BUNGEECORD_ZEITBAN";
	}

	@Override
	public String toString() {
		return String.format(getName()+"-/-%s-/-%s-/-%s-/-%s-/-%s", getBanned(),getBanner(),getReason(),getTime(),getTyp());
	}

	@Override
	public void Set(String[] split) {
		this.banned=split[1];
		this.banner=split[2];
		this.reason=split[3];
		this.time=split[4];
		this.typ=split[5];
	}

	@Override
	public void Set(String split) {
		String[] splitt = split.split("-/-");
		this.banned=splitt[1];
		this.banner=splitt[2];
		this.reason=splitt[3];
		this.time=splitt[4];
		this.typ=splitt[5];
	}

}
