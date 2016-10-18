package eu.epicpvp.kcore.enchantment.enchantments.attack;

import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.enchantment.enchantments.PotionDirectAttackEnchantmentListener;

public class PoisonEnchantmentListener extends PotionDirectAttackEnchantmentListener{

	public PoisonEnchantmentListener() {
		super(PotionEffectType.POISON,4,1);
	}
	
}
