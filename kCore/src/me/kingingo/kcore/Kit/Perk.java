package me.kingingo.kcore.Kit;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Permission.Permission;

import org.bukkit.event.Listener;

public abstract class Perk implements Listener{

	@Getter
	String name;
	@Getter
	PerkData perkData;
	@Getter
	@Setter
	Permission permission;
	
	public Perk(String name){
		this.name=name;
	}
	
	public void setPerkData(PerkData perkData){
		this.perkData=perkData;
	}
	
}
