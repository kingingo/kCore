package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PerkHitEffect extends Perk{

	int time;
	int chance;
	PotionEffectType potion;
	
	public PerkHitEffect(int time,int chance,PotionEffectType potion){
		super("HitEffect");
		this.time=time;
		this.potion=potion;
		this.chance=chance;
	}
	
	Player defend;
	Player attack;
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player&&ev.getDamager() instanceof Player){
			defend=(Player)ev.getDamager();
			if(!this.getKit().hasPlayer(this,defend))return;
			if(!(UtilMath.RandomInt(100, 0)<chance))return;
			defend=(Player)ev.getEntity();
			defend.addPotionEffect(new PotionEffect(potion,time*20,1));
		}
	}
	

}
