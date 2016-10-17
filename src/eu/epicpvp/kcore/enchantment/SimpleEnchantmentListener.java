package eu.epicpvp.kcore.enchantment;

import lombok.Getter;

public abstract class SimpleEnchantmentListener implements EnchantmentListener {

	@Getter
	private CustomEnchantment enchantment;

	@Override
	public void setCustomEnchantment(CustomEnchantment enchantment) {
		this.enchantment = enchantment;
	}
}
