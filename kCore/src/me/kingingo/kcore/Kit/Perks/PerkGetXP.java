package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

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
