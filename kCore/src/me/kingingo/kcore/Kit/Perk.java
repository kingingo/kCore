package me.kingingo.kcore.Kit;

import org.bukkit.event.Listener;

import lombok.Getter;

public abstract class Perk implements Listener{

	@Getter
	String name;
	@Getter
	Kit kit;
	
	public Perk(String name){
		this.name=name;
	}
	
	public void setKit(Kit kit){
		this.kit=kit;
	}
	
}
