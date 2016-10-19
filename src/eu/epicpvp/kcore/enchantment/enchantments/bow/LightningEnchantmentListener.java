package eu.epicpvp.kcore.enchantment.enchantments.bow;

import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.enchantment.enchantments.ArrowHitEnchantmentListener;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class LightningEnchantmentListener extends ArrowHitEnchantmentListener {

	private int chance;

	@Override
	public boolean doesFit(ItemStack item) {
		return item.getType() == Material.BOW;
	}

	@Override
	protected ItemStack checkForEnchantment(Player player) {
		return player.getItemInHand();
	}

	@Override
	protected void onArrowHit(Player damager, Entity hurt, Arrow arrow, int level) {
		int tempt = level;
		while (tempt != 0) {
			if (UtilMath.randomInteger(100) < chance * level) {
				hurt.getWorld().strikeLightning(hurt.getLocation());
			}
			tempt--;
		}
	}
}
