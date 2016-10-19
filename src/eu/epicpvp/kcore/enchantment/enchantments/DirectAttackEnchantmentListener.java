package eu.epicpvp.kcore.enchantment.enchantments;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public abstract class DirectAttackEnchantmentListener extends SimpleEnchantmentListener {

	protected abstract ItemStack checkForEnchantment(Player player); //evtl. liste zurückgeben und alle einzeln durchchecken

	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			ItemStack hand = checkForEnchantment(damager);
			if (hand != null && getEnchantment().contains(hand) && !getEnchantment().hasCooldown(damager) && doesFit(hand)) {
				event.setCancelled(event.isCancelled() || onAttack(damager, event.getEntity(), getEnchantment().getLevel(hand), event.getCause()));
			}
		}

		if (event.getEntity() instanceof Player) {
			Player hurt = (Player) event.getEntity();
			ItemStack item = checkForEnchantment(hurt);
			if (item != null && getEnchantment().contains(item) && !getEnchantment().hasCooldown(hurt) && doesFit(item)) {
				event.setCancelled(event.isCancelled() || onBeingAttacked(event.getDamager(), hurt, getEnchantment().getLevel(item), event.getCause()));
			}
		}
	}

	protected boolean onAttack(Player damager, Entity hurt, int level, DamageCause cause) {
		return false;
	}

	protected boolean onBeingAttacked(Entity damager, Player hurt, int level, DamageCause cause) {
		return false;
	}
}
