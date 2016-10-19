package eu.epicpvp.kcore.enchantment.enchantments.attack;

import eu.epicpvp.kcore.enchantment.enchantments.PotionDirectAttackEnchantmentListener;
import org.bukkit.potion.PotionEffectType;

public class BlindEnchantmentListener extends PotionDirectAttackEnchantmentListener {

	public BlindEnchantmentListener() {
		super(PotionEffectType.BLINDNESS, 3, 1);
	}
}
