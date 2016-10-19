package eu.epicpvp.kcore.enchantment.enchantments.attack;

import eu.epicpvp.kcore.enchantment.enchantments.PotionDirectAttackEnchantmentListener;
import org.bukkit.potion.PotionEffectType;

public class PoisonEnchantmentListener extends PotionDirectAttackEnchantmentListener {

	public PoisonEnchantmentListener() {
		super(PotionEffectType.POISON, 3, 1);
	}
}
