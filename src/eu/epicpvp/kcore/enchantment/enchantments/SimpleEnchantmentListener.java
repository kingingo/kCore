package eu.epicpvp.kcore.enchantment.enchantments;

import eu.epicpvp.kcore.enchantment.CustomEnchantment;
import eu.epicpvp.kcore.enchantment.EnchantmentListener;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class SimpleEnchantmentListener implements EnchantmentListener {

	@Getter
	private CustomEnchantment enchantment;

	@Override
	public void setCustomEnchantment(CustomEnchantment enchantment) {
		this.enchantment = enchantment;
	}

	public boolean hasCooldown(Player plr) {
		return enchantment.hasCooldown(plr);
	}
}
