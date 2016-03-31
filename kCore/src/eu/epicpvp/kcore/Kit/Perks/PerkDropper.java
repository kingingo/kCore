package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkDropper extends Perk{
	
	public PerkDropper() {
		super("Dropper",UtilItem.RenameItem(new ItemStack(Material.BUCKET),"§eDropper"));
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
