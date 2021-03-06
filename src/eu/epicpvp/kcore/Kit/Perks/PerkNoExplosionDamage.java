package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import eu.epicpvp.kcore.Kit.Perk;

public class PerkNoExplosionDamage extends Perk{

	public PerkNoExplosionDamage() {
		super("noFiredamage");
	}
	
	@EventHandler
	public void onBreak(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(!this.getPerkData().hasPlayer(this,p))return;
			if(e.getCause()==DamageCause.BLOCK_EXPLOSION||e.getCause()==DamageCause.ENTITY_EXPLOSION){
				e.setCancelled(true);
			}
		}
	}
	
}
