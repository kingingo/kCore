package eu.epicpvp.kcore.enchantment.enchantments;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class DirectAttackEnchantmentListener extends SimpleEnchantmentListener {

	protected abstract ItemStack checkForEnchantment(Player player); //evtl. liste zurückgeben und alle einzeln durchchecken

	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			ItemStack hand = checkForEnchantment(damager);
			if (hand != null && !getEnchantment().hasCooldown(damager) && doesFit(hand)) {
				onDirectAttack(damager, event.getEntity(), getEnchantment().getLevel(hand));
			}
		}
		
		if (event.getEntity() instanceof Player) {
			Player hurt = (Player) event.getEntity();
			ItemStack hand = checkForEnchantment(hurt);
			if (hand != null && !getEnchantment().hasCooldown(hurt) && doesFit(hand)) {
				onDirectAttacked(event.getDamager(), hurt, getEnchantment().getLevel(hand));
			}
		}
	}

	protected void onDirectAttack(Player damager, Entity hurt, int level) {

	}

	protected void onDirectAttacked(Entity damager, Player hurt, int level) {

	}
}
