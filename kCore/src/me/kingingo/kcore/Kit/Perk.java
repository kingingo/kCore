package me.kingingo.kcore.Kit;

import lombok.Getter;

import org.bukkit.event.Listener;

public abstract class Perk implements Listener{

	@Getter
	String name;
	@Getter
	PerkData perkData;
	
	public Perk(String name){
		this.name=name;
	}
	
	public void setPerkData(PerkData perkData){
		this.perkData=perkData;
	}
	
}
