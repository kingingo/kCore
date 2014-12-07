package me.kingingo.kcore.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class InventoryPageBase extends CraftInventoryCustom implements Listener{
 
	@Getter
	private ArrayList<IButton> buttons;
	
	public InventoryPageBase(JavaPlugin instance,int size,String title){
		super(null, size, title);
		this.buttons=new ArrayList<>();
		register(instance);
	}
	
	public void useButton(Player player,ActionType type,ItemStack item){
		for(IButton button : buttons){
			if(UtilItem.ItemNameEquals(button.getItemStack(), item)){
				button.Clicked(player, type);
				break;
			}
		}
	}
	
	public InventoryPageBase(int size,String title){
		super(null, size, title);
		this.buttons=new ArrayList<>();
	}
	
	public void fill(ItemStack item){
		for(int i = 0 ; i < getSize(); i++){
			if(getItem(i)==null||getItem(i).getType()==Material.AIR){
				if(getItem(i)==null)setItem(i, new ItemStack(Material.IRON_FENCE));
				getItem(i).setTypeId(item.getTypeId());
				getItem(i).setDurability((short) item.getDurability());
				ItemMeta im = getItem(i).getItemMeta();
				im.setDisplayName(" ");
				getItem(i).setItemMeta(im);
			}
		}
	}
	
	public void register(JavaPlugin instance){
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public void addButton(int slot,IButton button){
		setItem(slot, button.getItemStack());
		this.buttons.add(button);
	}
	
}