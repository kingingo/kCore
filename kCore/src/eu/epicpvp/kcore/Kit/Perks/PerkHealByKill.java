package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PerkHealByKill extends Perk{

	int herz;
	
	public PerkHealByKill(int herz){
		super("HealByKill");
		this.herz=herz;
	}
	
	Player attack;
	@EventHandler
	public void Damage(EntityDeathEvent ev){
		if((ev.getEntity() instanceof Monster||ev.getEntity() instanceof Animals)&&ev.getEntity().getKiller() instanceof Player){
			attack=(Player)ev.getEntity().getKiller();
			if(!this.getPerkData().hasPlayer(this,attack))return;
				UtilPlayer.health(attack, herz);
		}
	}
	

}
