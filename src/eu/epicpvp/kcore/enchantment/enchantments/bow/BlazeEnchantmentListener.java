package eu.epicpvp.kcore.enchantment.enchantments.bow;

import eu.epicpvp.kcore.enchantment.enchantments.ShootBowEnchantmentListener;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.ItemStack;

public class BlazeEnchantmentListener extends ShootBowEnchantmentListener {

	@Override
	public boolean doesFit(ItemStack item) {
		return item.getType() == Material.BOW;
	}

	@Override
	protected ItemStack checkForEnchantment(Player player) {
		return player.getItemInHand();
	}

	@Override
	protected boolean onDirectAttack(Player shooter, Entity projectile, int level) {
		shooter.launchProjectile(SmallFireball.class);
		return true;
	}
}
