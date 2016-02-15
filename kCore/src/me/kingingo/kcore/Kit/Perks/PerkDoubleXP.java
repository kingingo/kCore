package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

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
