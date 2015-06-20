package me.kingingo.kcore.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class InventoryPageBase extends CraftInventoryCustom{
 
	@Getter
	private ArrayList<IButton> buttons;
	
	public InventoryPageBase(JavaPlugin instance,int size,String title){
		super(null,size,title);
		this.buttons=new ArrayList<>();
	}
	
	public void useButton(Player player,ActionType type,ItemStack item,int slot){
		for(IButton button : buttons){
			if(UtilItem.ItemNameEquals(button.getItemStack(), item)
					|| (button.getItemStack()==null&&slot==button.getSlot()) ){
				button.Clicked(player, type,item);
				break;
			}
		}
	}
	
	public void remove(){
		buttons.clear();
		buttons=null;
	}
	
	public int getSlotSort(){
		if(getSize()==9){
			if(!contains(4))return 4;
			else if(!contains(5))return 5;
			else if(!contains(3))return 3;
			else if(!contains(6))return 6;
			else if(!contains(2))return 2;
			else if(!contains(7))return 7;
			else if(!contains(1))return 1;
			else if(!contains(8))return 8;
			else if(!contains(0))return 0;
		}
		return 0;
	}
	
	public InventoryPageBase(int size,String title){
		super(null, size, title);
		this.buttons=new ArrayList<>();
	}
	
	public void fill(Material material){
		for(int i = 0 ; i < getSize(); i++){
			if(getItem(i)==null||getItem(i).getType()==Material.AIR){
				if(getItem(i)==null)setItem(i, new ItemStack(Material.IRON_FENCE));
				getItem(i).setType(material);
				ItemMeta im = getItem(i).getItemMeta();
				im.setDisplayName(" ");
				getItem(i).setItemMeta(im);
			}
		}
	}
	
	public void fill(Material material,int data){
		for(int i = 0 ; i < getSize(); i++){
			if(getItem(i)==null||getItem(i).getType()==Material.AIR){
				if(getItem(i)==null)setItem(i, new ItemStack(Material.IRON_FENCE));
				getItem(i).setType(material);
				getItem(i).setDurability((short) data);
				ItemMeta im = getItem(i).getItemMeta();
				im.setDisplayName(" ");
				getItem(i).setItemMeta(im);
			}
		}
	}
	
	public IButton getButton(int slot){
		for(IButton button : getButtons()){
			if(button.getSlot()==slot){
				return button;
			}
		}
		return null;
	}
	
	public void addButton(IButton button){
		int slot=firstEmpty();
		button.setSlot(slot);
		button.setInventoryPageBase(this);
		setItem(slot, button.getItemStack());
		this.buttons.add(button);
	}
	
	public void addButton(int slot,IButton button){
		button.setSlot(slot);
		button.setInventoryPageBase(this);
		setItem(slot, button.getItemStack());
		this.buttons.add(button);
	}
	
}
