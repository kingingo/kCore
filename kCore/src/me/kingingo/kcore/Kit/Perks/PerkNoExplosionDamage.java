package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;

public class PerkNoExplosionDamage extends Perk{

	public PerkNoExplosionDamage() {
		super("noFiredamage");
	}
	
	@EventHandler
	public void onBreak(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(!this.getKit().hasPlayer(this,p))return;
			if(e.getCause()==DamageCause.BLOCK_EXPLOSION||e.getCause()==DamageCause.ENTITY_EXPLOSION){
				e.setCancelled(true);
			}
		}
	}
	
}
