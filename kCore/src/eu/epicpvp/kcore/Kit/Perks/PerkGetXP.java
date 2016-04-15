package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkGetXP extends Perk{
	
	public PerkGetXP() {
		super("GetXP",UtilItem.RenameItem(new ItemStack(Material.ENCHANTMENT_TABLE),"§eGetXP"));
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Death(PlayerDeathEvent ev){
		if(getPerkData().hasPlayer(this, ev.getEntity().getKiller())){
			ev.getEntity().getKiller().setExp( ev.getNewExp()+ev.getEntity().getKiller().getExp() );
			ev.setDroppedExp(0);
		}
	}

}
