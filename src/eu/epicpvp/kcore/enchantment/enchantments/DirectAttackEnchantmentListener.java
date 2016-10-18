package eu.epicpvp.kcore.enchantment.enchantments;

import org.bukkit.entity.Damageable;
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
			if (hand != null && !getEnchantment().hasCooldown(damager) && doesFit(hand)) {
				event.setCancelled(onDirectAttack(damager, event.getEntity(), getEnchantment().getLevel(hand),event.getCause()));
			}
		}
		
		if (event.getEntity() instanceof Player) {
			Player hurt = (Player) event.getEntity();
			ItemStack hand = checkForEnchantment(hurt);
			if (hand != null && !getEnchantment().hasCooldown(hurt) && doesFit(hand)) {
				event.setCancelled(onDirectAttacked(event.getDamager(), hurt, getEnchantment().getLevel(hand),event.getCause()));
			}
		}
	}

	protected boolean onDirectAttack(Player damager, Entity hurt, int level,DamageCause cause) {
		return false;
	}

	protected boolean onDirectAttacked(Entity damager, Player hurt, int level,DamageCause cause) {
		return false;
	}
}
