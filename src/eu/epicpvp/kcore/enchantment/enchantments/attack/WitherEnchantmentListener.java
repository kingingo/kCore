package eu.epicpvp.kcore.enchantment.enchantments.attack;

import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.enchantment.enchantments.PotionDirectAttackEnchantmentListener;

public class WitherEnchantmentListener extends PotionDirectAttackEnchantmentListener{

	public WitherEnchantmentListener() {
		super(PotionEffectType.WITHER, 4, 1);
	}
}
