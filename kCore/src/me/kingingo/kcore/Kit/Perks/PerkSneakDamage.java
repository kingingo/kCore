package me.kingingo.kcore.Kit.Perks;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;

public class PerkSneakDamage extends Perk{
	
	double damage;
	public PerkSneakDamage(double damage) {
		super("Sneakdamage");
		this.damage=damage;
	}
	
	Player defend;
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player){
			defend=(Player)ev.getEntity();
			if(!this.getKit().hasPlayer(this,defend))return;
			if(defend.isSneaking()){
				defend.playEffect(defend.getEyeLocation(),Effect.MOBSPAWNER_FLAMES,-30);
				ev.setDamage(damage);
			}
		}else if(ev.getDamager() instanceof Player){
			defend=(Player)ev.getDamager();
			if(!this.getKit().hasPlayer(this,defend))return;
			if(defend.isSneaking()){
				ev.setCancelled(true);
			}
		}
	}

}
