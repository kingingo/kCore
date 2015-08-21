package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PerkNoFalldamage extends Perk{

	public PerkNoFalldamage() {
		super("noFalldamage");
	}
	
	@EventHandler
	public void onBreak(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(e.getCause()==DamageCause.FALL){
			if(!this.getPerkData().hasPlayer(this,p))return;
				e.setCancelled(true);
			}
		}
	}
	
}
