package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;

public class InventoryLoad extends InventoryPageBase implements Listener{

	private int slot=0;
	
	public InventoryLoad(JavaPlugin instance, String title) {
		super("InventoryLoad",9, title);
		this.fill(Material.WOOL);
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	//BEWEGT DEN WOOL BLOCK IMMER EIN WEITER
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()==UpdateType.FAST){
			//DER SLOT WIRD ZUR§CKGESETZT WENN DER SLOT-1 >=0
			if(slot-1>=0)setItem(slot-1, new ItemStack(Material.WOOL));
			if(slot<8){
				slot=slot+1;
				setItem(slot, new ItemStack(Material.WOOL,1,(byte)14));
			}else{
				setItem(slot, new ItemStack(Material.WOOL));
				slot=0;
				setItem(slot, new ItemStack(Material.WOOL,1,(byte)14));
			}
		}
	}

}
