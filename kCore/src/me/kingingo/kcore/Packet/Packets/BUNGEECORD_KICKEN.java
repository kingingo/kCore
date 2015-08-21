package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import me.kingingo.kcore.Packet.Packet;

public class BUNGEECORD_KICKEN extends Packet{
	
	@Getter
	private String kicked;
	@Getter
	private String reason;
	
	public BUNGEECORD_KICKEN(){}
	
	public BUNGEECORD_KICKEN(String kicked){
		this.kicked=kicked;
		this.reason="Kein Grund";
	}
	
	public BUNGEECORD_KICKEN(String kicked,String reason){
		this.kicked=kicked;
		this.reason=reason;
	}
	
	public BUNGEECORD_KICKEN create(String[] packet){
		return new BUNGEECORD_KICKEN(packet);
	}
	
	public BUNGEECORD_KICKEN(String[] packet){
		Set(packet);
	}
	
	@Override
	public String getName() {
		return "BUNGEECORD_KICKEN";
	}

	@Override
	public String toString() {
		return String.format(getName()+"-/-%s-/-%s", getKicked(),getReason());
	}

	@Override
	public void Set(String[] split) {
		this.kicked=split[1];
		this.reason=split[2];
	}

	@Override
	public void Set(String split) {
		String[] splitt = split.split("-/-");
		this.kicked=splitt[1];
		this.reason=splitt[2];
	}

}
