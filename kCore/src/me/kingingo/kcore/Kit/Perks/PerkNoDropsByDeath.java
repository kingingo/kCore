package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PerkNoDropsByDeath extends Perk{

	public PerkNoDropsByDeath(){
		super("NoDropsByDeath");
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player && ev.getEntity().getKiller() instanceof Player){
			if(!this.getPerkData().hasPlayer(this,((Player)ev.getEntity())))return;
			ev.getDrops().clear();
		}
	}
	

}
