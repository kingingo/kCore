package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryLotto extends InventoryPageBase{

	private ItemStack[] items;
	
	public InventoryLotto(JavaPlugin instance,Click click,ItemStack[] items) {
		super(instance, 27, "Lotto:");
		this.items=items;
		this.addButton(13,new ButtonBase(click));
		setItem(4, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " ") );
		setItem(22, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " ") );
	}

	private ItemStack item;
	@EventHandler
	public void UpdateEvent(UpdateEvent ev){
		if(ev.getType()!=UpdateType.TICK)return;
		for(int i = 9; i <= 17; i++){
			if(i==9){
				setItem(i, items[UtilMath.r(items.length)]);
			}else{
				item=getItem(i);
				setItem(i, getItem(i-1) );
				setItem(i-1, item);
			}
		}
	}
	
}