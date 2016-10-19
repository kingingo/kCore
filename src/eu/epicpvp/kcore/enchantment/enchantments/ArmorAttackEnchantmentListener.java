package eu.epicpvp.kcore.enchantment.enchantments;

import eu.epicpvp.kcore.Util.UtilItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorAttackEnchantmentListener extends DirectAttackEnchantmentListener {

	@Override
	public boolean doesFit(ItemStack item) {
		return UtilItem.isArmor(item);
	}

	@Override
	protected ItemStack checkForEnchantment(Player player) {
		return checkForEnchantment(player, 0);
	}

	public ItemStack checkForEnchantment(Player player, int i) {
		ItemStack item;
		switch (i) {
			case 0:
				item = player.getInventory().getHelmet();
				break;
			case 1:
				item = player.getInventory().getChestplate();
				break;
			case 2:
				item = player.getInventory().getLeggings();
				break;
			case 3:
				item = player.getInventory().getBoots();
				break;
			default:
				return null;
		}

		if (item != null && doesFit(item) && getEnchantment().contains(item)) {
			return item;
		} else {
			i++;
			return checkForEnchantment(player, i);
		}
	}
}
