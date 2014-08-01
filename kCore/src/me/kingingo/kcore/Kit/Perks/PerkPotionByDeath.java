package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PerkPotionByDeath extends Perk{

	PotionEffect potion;
	public PerkPotionByDeath(PotionEffect potion,String name){
		super("PotionByDeath", Text.PERK_POTIONBYDEATH.getTexts(name));
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player && ev.getEntity().getKiller() instanceof Player){
			if(!this.getKit().hasPlayer(this, ((Player)ev.getEntity()) ))return;
			Player k = (Player)ev.getEntity().getKiller();
			k.addPotionEffect(potion);
		}
	}
	

}
