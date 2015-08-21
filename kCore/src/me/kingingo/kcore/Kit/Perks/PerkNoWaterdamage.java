package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PerkNoWaterdamage extends Perk{

	public PerkNoWaterdamage() {
		super("noWaterdamage");
	}
	
	@EventHandler
	public void onBreak(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(!this.getPerkData().hasPlayer(this,p))return;
			if(e.getCause()==DamageCause.DROWNING){
				e.setCancelled(true);
			}
		}
	}
	
}
