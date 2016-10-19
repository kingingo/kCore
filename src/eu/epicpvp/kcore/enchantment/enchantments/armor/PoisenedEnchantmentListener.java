package eu.epicpvp.kcore.enchantment.enchantments.armor;

import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.enchantment.enchantments.ArmorAttackEnchantmentListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

public class PoisenedEnchantmentListener extends ArmorAttackEnchantmentListener {

	@Override
	protected boolean onBeingAttacked(Entity damager, Player hurt, int level, DamageCause cause) {
		if (damager instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) damager;

			if (!le.hasPotionEffect(PotionEffectType.POISON)) {
				le.addPotionEffect(UtilPlayer.getPotionEffect(PotionEffectType.POISON, 3 + level, 1), true);
			}
		}
		return false;
	}
}
