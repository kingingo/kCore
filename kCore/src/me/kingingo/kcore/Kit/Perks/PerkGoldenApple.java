package me.kingingo.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Setter;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilInv;

public class PerkGoldenApple extends Perk{

	@Setter
	private double prozent=25;
	private int absorption=20*60*2;
	private int regeneration=20*30;
	private int fire_protection=20*60*5;
	private int resistenz=20*60*5;
	
	public PerkGoldenApple() {
		super("GoldenApple");
	}

	@EventHandler
	public void consum(PlayerItemConsumeEvent ev){
		if(!this.getPerkData().hasPlayer(this,ev.getPlayer()))return;
		if(ev.getItem().getType()==Material.GOLDEN_APPLE && ev.getItem().getDurability()==1){
//			UtilInv.remove(ev.getPlayer(), ev.getItem(), 1);
//			ev.setCancelled(true);
			
			ev.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
			ev.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
			ev.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			ev.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,(absorption + (int) ((prozent/100)*(absorption))), 1));
			ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (regeneration + (int) ((prozent/100)*(regeneration))), 5));
			ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (fire_protection + (int) ((prozent/100)*(fire_protection))), 1));
			ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (resistenz + (int) ((prozent/100)*(resistenz))), 1));
		}
	}
	
}
