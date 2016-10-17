package eu.epicpvp.kcore.enchantment;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class DirectAttackEnchantmentListener extends SimpleEnchantmentListener {

	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			ItemStack hand = damager.getItemInHand();
			if (doesFit(hand)) {
				onDirectAttack(damager, event.getEntity(), getEnchantment().getLevel(hand));
			}
		}
		if (event.getEntity() instanceof Player) {
			Player hurt = (Player) event.getEntity();
			ItemStack hand = hurt.getItemInHand();
			if (doesFit(hand)) {
				onDirectAttacked(event.getDamager(), hurt, getEnchantment().getLevel(hand));
			}
		}
	}

	protected void onDirectAttack(Player damager, Entity hurt, int level) {

	}

	protected void onDirectAttacked(Entity damager, Player hurt, int level) {

	}
}
