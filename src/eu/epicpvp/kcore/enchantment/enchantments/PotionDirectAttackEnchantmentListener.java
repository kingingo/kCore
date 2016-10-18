package eu.epicpvp.kcore.enchantment.enchantments;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PotionDirectAttackEnchantmentListener extends DirectAttackEnchantmentListener{

	private PotionEffectType type;
	private int sec;
	private int potion_lvl;
	
	@Override
	public boolean doesFit(ItemStack item) {
		return UtilItem.isSword(item) ^ UtilItem.isAxt(item);
	}

	@Override
	protected ItemStack checkForEnchantment(Player player) {
		return player.getItemInHand();
	}
	
	@Override
	protected void onDirectAttack(Player damager, Entity hurt, int level) {
		if(hurt instanceof Player){
			Player victim = (Player)hurt;
			
			if(!victim.hasPotionEffect(type)){
				UtilPlayer.addPotionEffect(victim, type, sec, potion_lvl);
			}
		}
	}
}
