package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilInv;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PerkDropper extends Perk{
	
	public PerkDropper() {
		super("Dropper");
	}
	
	int a=0;
	@EventHandler
	public void Dropper(PlayerDeathEvent ev){
		if(ev.getDrops().isEmpty())return;
		if(getPerkData().hasPlayer(this, ev.getEntity().getKiller())){
			a=UtilInv.getAnzahlInventory(ev.getEntity().getPlayer());
			if(UtilInv.FreeInventory(ev.getEntity().getKiller()) > a){
				for(ItemStack item : ev.getEntity().getInventory().getContents()){
					if(item!=null&&item.getType()!=Material.AIR){
						ev.getEntity().getKiller().getInventory().addItem(item);
					}
				}
				
				for(ItemStack item : ev.getEntity().getInventory().getArmorContents()){
					if(item!=null&&item.getType()!=Material.AIR){
						ev.getEntity().getKiller().getInventory().addItem(item);
					}
				}
				ev.getDrops().clear();
			}
		}
	}
}
