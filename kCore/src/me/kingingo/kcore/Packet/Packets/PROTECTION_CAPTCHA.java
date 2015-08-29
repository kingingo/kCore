package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PROTECTION_CAPTCHA extends Packet{

	@Setter
	@Getter
	private boolean captcha;
	
	public PROTECTION_CAPTCHA(){}
	
	public PROTECTION_CAPTCHA(String packet){
		Set(packet);
	}
	
	public PROTECTION_CAPTCHA(String[] packet){
		Set(packet);
	}
	
	public PROTECTION_CAPTCHA create(String[] packet){
		return new PROTECTION_CAPTCHA(packet);
	}
	
	public PROTECTION_CAPTCHA(boolean captcha){
		this.captcha=captcha;
	}
	
	public String getName(){
		return "PROTECTION_CAPTCHA";
	}
	
	public void Set(String[] split){
		captcha=(split[1].equalsIgnoreCase("true")?true:false);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Set(split);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", (captcha?"true":"false"));
	}
	
}
