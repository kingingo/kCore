package eu.epicpvp.kcore.enchantment.enchantments;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.enchantment.DirectAttackEnchantmentListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LifestealEnchantmentListener extends DirectAttackEnchantmentListener {

	@Override
	public boolean doesFit(ItemStack item) {
		return UtilItem.isSword(item);
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
