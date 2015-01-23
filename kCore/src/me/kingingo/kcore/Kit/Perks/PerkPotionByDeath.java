package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

public class PerkPotionByDeath extends Perk{

	PotionEffect potion;
	public PerkPotionByDeath(PotionEffect potion){
		super("PotionByDeath");
		this.potion=potion;
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player && ev.getEntity().getKiller() instanceof Player){
			if(!this.getPerkData().hasPlayer(this, ((Player)ev.getEntity()) ))return;
			Player k = (Player)ev.getEntity().getKiller();
			k.addPotionEffect(potion);
		}
	}
	

}
