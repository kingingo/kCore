package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkKillZombie extends Perk{

	public PerkKillZombie() {
		super("One-Hit",UtilItem.RenameItem(new ItemStack(Material.IRON_AXE),"§eOne-Hit"));
	}

	@EventHandler
	public void damage(EntityDamageByEntityEvent ev){
		if(ev.getDamager() instanceof Player && ev.getEntity() instanceof Zombie){
			Player damager = (Player)ev.getDamager();
			
			if(this.getPerkData().hasPlayer(this, damager)){
				ev.setDamage(50);
			}
		}
	}
}
