package eu.epicpvp.kcore.enchantment.enchantments;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BlockBreakEnchantmentListener extends SimpleEnchantmentListener {

	protected abstract ItemStack checkForEnchantment(Player player); //evtl. liste zurückgeben und alle einzeln durchchecken

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		ItemStack hand = checkForEnchantment(event.getPlayer());
		
		if(hand!=null){
			if(doesFit(hand)){
				event.setCancelled(onBreak(event.getPlayer(), event.getBlock(), getEnchantment().getLevel(hand)));
			}
		}
	}

	protected boolean onBreak(Player player,Block block, int level) {
		return false;
	}
}
