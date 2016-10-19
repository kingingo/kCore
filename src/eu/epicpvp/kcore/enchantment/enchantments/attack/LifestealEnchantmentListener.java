package eu.epicpvp.kcore.enchantment.enchantments.attack;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.enchantment.enchantments.DirectAttackEnchantmentListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
	protected boolean onAttack(Player damager, Entity hurt, int level, DamageCause cause) {
		double diff = damager.getMaxHealth() - damager.getHealth();
		if (diff <= 0) {
			return false;
		}
		if (diff <= level) {
			damager.setHealth(damager.getMaxHealth());
			return false;
		}
		if (!getEnchantment().hasCooldown(damager)) {
			damager.setHealth(damager.getHealth() + level);
		}
		return false;
	}
}
