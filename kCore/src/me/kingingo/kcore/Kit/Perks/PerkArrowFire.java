package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;

public class PerkArrowFire extends Perk{

	int i;
	
	public PerkArrowFire(int i) {
		super("ArrowFire",Text.PERK_ARROWFIRE.getTexts(i));
		this.i=i;
	}

	@EventHandler
	public void Shoot(EntityShootBowEvent ev){
		if(ev.getEntity() instanceof Player){
			Player p = (Player)ev.getEntity();
			if(!this.getKit().hasPlayer(this,p))return;
			if(UtilMath.RandomInt(10, 0)==i){
				ev.getProjectile().setFireTicks(ev.getProjectile().getMaxFireTicks());
			}
		}
	}
	
}
