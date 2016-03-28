package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PerkHealByHit extends Perk{

	int herz;
	int chance;
	
	public PerkHealByHit(int chance,int herz){
		super("HealByHit");
		this.chance=chance;
		this.herz=herz;
	}
	
	Player defend;
	Player attack;
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player&&ev.getDamager() instanceof Player){
			defend=(Player)ev.getEntity();
			attack=(Player)ev.getDamager();
			if(!this.getPerkData().hasPlayer(this,attack))return;
			if(UtilMath.RandomInt(100, 1) < chance){
				UtilPlayer.health(attack, herz);
			}
		}
	}
	

}
