package eu.epicpvp.kcore.enchantment.enchantments;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ArrowHitEnchantmentListener extends SimpleEnchantmentListener{
	protected abstract ItemStack checkForEnchantment(Player player); //evtl. liste zurückgeben und alle einzeln durchchecken

	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			
			if(arrow.getShooter() instanceof Player){
				Player damager = (Player)arrow.getShooter();
				ItemStack hand = checkForEnchantment(damager);

				if (hand != null && getEnchantment().contains(hand) && !getEnchantment().hasCooldown(damager) && doesFit(hand)) {
					onDirectAttack(damager, event.getEntity(), arrow, getEnchantment().getLevel(hand));
				}
			}
		}
	}

	protected void onDirectAttack(Player damager, Entity hurt,Arrow arrow, int level) {

	}
}
