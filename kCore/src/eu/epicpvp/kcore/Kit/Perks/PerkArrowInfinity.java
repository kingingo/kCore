package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;

public class PerkArrowInfinity extends Perk{
	
	public PerkArrowInfinity() {
		super("ArrowInfinity");
	}

	@EventHandler
	public void Shoot(EntityShootBowEvent ev){
		if(ev.getEntity() instanceof Player){
			Player p = (Player)ev.getEntity();
			if(!getPerkData().hasPlayer(this, p))return;
			p.getInventory().addItem(new ItemStack(Material.ARROW));
		}
	}
	
}
