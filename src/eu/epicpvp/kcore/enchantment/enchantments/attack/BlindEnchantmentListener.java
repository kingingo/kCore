package eu.epicpvp.kcore.enchantment.enchantments.attack;

import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.enchantment.enchantments.PotionDirectAttackEnchantmentListener;

public class BlindEnchantmentListener extends PotionDirectAttackEnchantmentListener{

	public BlindEnchantmentListener() {
		super(PotionEffectType.BLINDNESS,4,1);
	}
	
}
