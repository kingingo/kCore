package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PerkPoisen extends Perk{

	int time;
	int chance;
	public PerkPoisen(int time,int chance){
		super("Poisen");
		this.time=time;
		this.chance=chance;
	}
	
	Player defend;
	Player attack;
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player&&ev.getDamager() instanceof Player){
			defend=(Player)ev.getEntity();
			if(!this.getKit().hasPlayer(this,defend))return;
			if(!(UtilMath.RandomInt(100, 0)<chance))return;
			attack=(Player)ev.getDamager();
			attack.addPotionEffect(new PotionEffect(PotionEffectType.POISON,time*20,1));
		}
	}
	

}
