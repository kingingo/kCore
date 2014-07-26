package me.kingingo.kcore.Kit;

import org.bukkit.event.Listener;

import lombok.Getter;

public abstract class Perk implements Listener{

	@Getter
	String name;
	@Getter
	String description[];
	@Getter
	Kit kit;
	
	public Perk(String name,String[] description){
		this.name=name;
		this.description=description;
	}
	
	public void setKit(Kit kit){
		this.kit=kit;
	}
	
}
