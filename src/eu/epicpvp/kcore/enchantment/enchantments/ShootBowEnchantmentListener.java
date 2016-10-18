package eu.epicpvp.kcore.enchantment.enchantments;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ShootBowEnchantmentListener extends SimpleEnchantmentListener{
	protected abstract ItemStack checkForEnchantment(Player player); //evtl. liste zurückgeben und alle einzeln durchchecken

	@EventHandler
	public void onBow(EntityShootBowEvent event) {
		if(event.getEntity() instanceof Player){
			Player shooter = (Player)event.getEntity();
			
			if( getEnchantment().contains(event.getBow()) && !getEnchantment().hasCooldown(shooter) && doesFit(event.getBow())){
				event.setCancelled( onDirectAttack(shooter, event.getProjectile(), getEnchantment().getLevel(event.getBow())) );
			}
		}
	}

	protected boolean onDirectAttack(Player shooter, Entity projectile, int level) {
		return false;
	}
}
