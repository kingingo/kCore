package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkHat extends Perk{

	public PerkHat() {
		super("Hat",UtilItem.RenameItem(new ItemStack(Material.SKULL_ITEM),"§eHat"));
	}
	
	@EventHandler
	public void HatDeath(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player){
			if(ev.getEntity().getKiller() instanceof Player){
				if(!this.getPerkData().hasPlayer(this, ((Player)ev.getEntity()).getKiller() ))return;
				((Player)ev.getEntity()).getKiller().getInventory().addItem( UtilItem.Head( ((Player)ev.getEntity()).getName() ) );
			}
		}
	}

}
