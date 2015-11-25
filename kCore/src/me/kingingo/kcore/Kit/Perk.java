package me.kingingo.kcore.Kit;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.event.Listener;

public abstract class Perk implements Listener{

	@Getter
	private String name;
	@Getter
	private PerkData perkData;
	@Getter
	@Setter
	private kPermission permission;
	
	public Perk(String name){
		this.name=name;
	}
	
	public void setPerkData(PerkData perkData){
		this.perkData=perkData;
	}
	
}
