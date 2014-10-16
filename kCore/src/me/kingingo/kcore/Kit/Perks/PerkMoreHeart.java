package me.kingingo.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;

public class PerkMoreHeart extends Perk{

	private int heart=20; 
	
	public PerkMoreHeart(int heart){
		super("MoreHeart");
		this.heart=heart;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		for(Player p : getKit().getPlayers()){
			p.setMaxHealth(heart);
			p.setHealth(heart);
		}
	}
	
}
