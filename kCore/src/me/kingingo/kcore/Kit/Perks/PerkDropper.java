package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilInv;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PerkDropper extends Perk{
	
	public PerkDropper() {
		super("Dropper");
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Dropper(PlayerDeathEvent ev){
		if(getPerkData().hasPlayer(this, ev.getEntity().getKiller())){
			if(UtilInv.FreeInventory(ev.getEntity().getKiller()) >= ev.getDrops().size()){
				for(ItemStack item : ev.getDrops())ev.getEntity().getPlayer().getInventory().addItem(item);
				ev.getDrops().clear();
			}
		}
	}


}
