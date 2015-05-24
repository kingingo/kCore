package me.kingingo.kcore.Inventory.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

public class InventoryLoad extends InventoryPageBase{

	private int slot=0;
	
	public InventoryLoad(JavaPlugin instance, String title) {
		super(9, title);
		this.fill(Material.WOOL);
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()==UpdateType.FAST){
			if(slot-1>=1)setItem(slot-1, new ItemStack(Material.WOOL));
			if(slot<=8){
				slot=slot+1;
				setItem(slot+1, new ItemStack(Material.WOOL,1,(byte)14));
			}else{
				slot=0;
			}
		}
	}

}
