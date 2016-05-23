package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkNoPotion extends Perk{

	private PotionEffectType type;
	
	public PerkNoPotion(PotionEffectType type){
		super("NoPotion");
		this.type=type;
		setItem(UtilItem.RenameItem(new ItemStack(Material.POTION,1,(byte)8292), "§eAnti Potion"));
	}
	
	@EventHandler
	public void PotionSplash(PotionSplashEvent ev){
		for(PotionEffect pe : ev.getPotion().getEffects()){
			if(pe.getType() == type){
				ev.getAffectedEntities().removeAll(getPerkData().getPlayers().get(this));
				break;
			}
		}
	}
}
