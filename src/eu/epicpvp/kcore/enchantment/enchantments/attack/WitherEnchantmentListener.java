package eu.epicpvp.kcore.enchantment.enchantments.attack;

import eu.epicpvp.kcore.enchantment.enchantments.PotionDirectAttackEnchantmentListener;
import org.bukkit.potion.PotionEffectType;

public class WitherEnchantmentListener extends PotionDirectAttackEnchantmentListener {

	public WitherEnchantmentListener() {
		super(PotionEffectType.WITHER, 3, 1);
	}
}
