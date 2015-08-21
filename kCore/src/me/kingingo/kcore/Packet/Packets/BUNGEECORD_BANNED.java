package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import me.kingingo.kcore.Packet.Packet;

public class BUNGEECORD_BANNED extends Packet{
	
	@Getter
	private String banned;
	@Getter
	private String banner;
	@Getter
	private String reason;
	
	public BUNGEECORD_BANNED(){}
	
	public BUNGEECORD_BANNED(int level,String banned,String banner){
		this.banned=banned;
		this.banner=banner;
		this.reason="Kein Grund Ban-LvL("+level+")";
	}
	
	public BUNGEECORD_BANNED(String banned,String banner,String reason){
		this.banned=banned;
		this.banner=banner;
		this.reason=reason;
	}
	
	public BUNGEECORD_BANNED create(String[] packet){
		return new BUNGEECORD_BANNED(packet);
	}
	
	public BUNGEECORD_BANNED(String[] packet){
		Set(packet);
	}
	
	@Override
	public String getName() {
		return "BUNGEECORD_BANNED";
	}

	@Override
	public String toString() {
		return String.format(getName()+"-/-%s-/-%s-/-%s", getBanned(),getBanner(),getReason());
	}

	@Override
	public void Set(String[] split) {
		this.banned=split[1];
		this.banner=split[2];
		this.reason=split[3];
	}

	@Override
	public void Set(String split) {
		String[] splitt = split.split("-/-");
		this.banned=splitt[1];
		this.banner=splitt[2];
		this.reason=splitt[3];
	}

}
