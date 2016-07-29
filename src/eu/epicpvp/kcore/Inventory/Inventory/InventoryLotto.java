package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilMath;

public class InventoryLotto extends InventoryPageBase implements Listener{

	private ItemStack[] items;
	
	public InventoryLotto(JavaPlugin instance,Click click,ItemStack[] items) {
		super("InventoryLotto",27, "Lotto:");
		this.items=items;
		this.addButton(13,new ButtonBase(click));
		setItem(4, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " ") );
		setItem(22, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " ") );
		Bukkit.getPluginManager().registerEvents(this, instance);
	}

	private ItemStack item;
	@EventHandler
	public void UpdateEvent(UpdateEvent ev){
		if(ev.getType()!=UpdateType.TICK)return;
		for(int i = 9; i <= 17; i++){
			if(i==9){
				setItem(i, items[UtilMath.randomInteger(items.length)]);
			}else{
				item=getItem(i);
				setItem(i, getItem(i-1) );
				setItem(i-1, item);
			}
		}
	}
	
}
