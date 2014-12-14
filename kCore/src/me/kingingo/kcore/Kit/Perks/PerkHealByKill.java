package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

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
