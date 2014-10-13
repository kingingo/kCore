package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class PerkArrowInfinity extends Perk{
	
	public PerkArrowInfinity() {
		super("ArrowInfinity");
	}

	@EventHandler
	public void Shoot(EntityShootBowEvent ev){
		if(ev.getEntity() instanceof Player){
			Player p = (Player)ev.getEntity();
			p.getInventory().addItem(new ItemStack(Material.ARROW));
		}
	}
	
}
