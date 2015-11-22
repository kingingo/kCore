package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PerkMoreHeart extends Perk{

	private int heart=20; 
	
	public PerkMoreHeart(int heart){
		super("MoreHeart");
		this.heart=heart;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		if(!this.getPerkData().getPlayers().containsKey(this))return;
		for(Player p : this.getPerkData().getPlayers().get(this)){
			p.setMaxHealth(heart);
			p.setHealth(heart);
		}
	}
	
}
