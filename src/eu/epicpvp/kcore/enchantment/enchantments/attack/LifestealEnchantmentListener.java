package eu.epicpvp.kcore.enchantment.enchantments.attack;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.enchantment.enchantments.DirectAttackEnchantmentListener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LifestealEnchantmentListener extends DirectAttackEnchantmentListener {

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
		double diff = damager.getMaxHealth() - damager.getHealth();
		if (diff <= 0) {
			return;
		}
		if (diff <= level) {
			damager.setHealth(damager.getMaxHealth());
			return;
		}
		damager.setHealth(damager.getHealth() + level);
	}
}
