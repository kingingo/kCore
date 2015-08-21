package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

public class PerkArrowFire extends Perk{

	int i;
	
	public PerkArrowFire(int i) {
		super("ArrowFire");
		this.i=i;
	}

	@EventHandler
	public void Shoot(EntityShootBowEvent ev){
		if(ev.getEntity() instanceof Player){
			Player p = (Player)ev.getEntity();
			if(!this.getPerkData().hasPlayer(this,p))return;
			if(UtilMath.RandomInt(100, 0)<=i){
				ev.getProjectile().setFireTicks(1000);
			}
		}
	}
	
}
