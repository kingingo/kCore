package eu.epicpvp.kcore.enchantment.enchantments;

import eu.epicpvp.kcore.enchantment.CustomEnchantment;
import eu.epicpvp.kcore.enchantment.EnchantmentListener;
import lombok.Getter;

public abstract class SimpleEnchantmentListener implements EnchantmentListener {

	@Getter
	private CustomEnchantment enchantment;

	@Override
	public void setCustomEnchantment(CustomEnchantment enchantment) {
		this.enchantment = enchantment;
	}
}
