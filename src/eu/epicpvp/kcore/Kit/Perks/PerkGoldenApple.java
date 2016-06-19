package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Setter;

public class PerkGoldenApple extends Perk{

	@Setter
	private double prozent=25;
	private int absorption=20*60*2;
	private int regeneration=20*30;
	private int fire_protection=20*60*5;
	private int resistenz=20*60*5;
	
	public PerkGoldenApple() {
		super("GoldenApple",UtilItem.RenameItem(new ItemStack(Material.GOLDEN_APPLE),"§eGoldenApple"));
	}

	@EventHandler
	public void consum(PlayerItemConsumeEvent ev){
		if(!this.getPerkData().hasPlayer(this,ev.getPlayer()))return;
		if(ev.getItem().getType()==Material.GOLDEN_APPLE && ev.getItem().getDurability()==1){
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
