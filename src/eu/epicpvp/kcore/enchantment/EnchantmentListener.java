package eu.epicpvp.kcore.enchantment;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public interface EnchantmentListener extends Listener {

	void setCustomEnchantment(CustomEnchantment enchantment);

	boolean doesFit(ItemStack item);
}
