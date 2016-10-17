package eu.epicpvp.kcore.enchantment;

import org.bukkit.event.Listener;

public interface EnchantmentListener extends Listener {

	void setCustomEnchantment(CustomEnchantment enchantment);
}
