package eu.epicpvp.kcore.enchantment.enchantments.armor;

import eu.epicpvp.kcore.enchantment.enchantments.ArmorAttackEnchantmentListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class ObsidianshieldEnchantmentListener extends ArmorAttackEnchantmentListener {

	@Override
	protected boolean onBeingAttacked(Entity damager, Player hurt, int level, DamageCause cause) {
		if (cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) {
			return true;
		}
		return false;
	}
}
