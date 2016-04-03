package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkDoubleXP extends Perk{
	
	public PerkDoubleXP() {
		super("DoubleXP",UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE),"§eDoubleXP"));
	}
	
	@EventHandler
	public void XP(PlayerExpChangeEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			ev.setAmount(ev.getAmount()*2);
		}
	}

}
