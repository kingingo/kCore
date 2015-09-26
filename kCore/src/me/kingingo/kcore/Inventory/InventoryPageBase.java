package me.kingingo.kcore.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilDebug;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryPageBase extends CraftInventoryCustom{
 
	@Getter
	private ArrayList<IButton> buttons;
	@Getter
	private String inventoryType;
	
	public InventoryPageBase(int size,String title){
		super(null,InventorySize.invSize(size).getSize(),(title==null?"Inventory":title));
		this.buttons=new ArrayList<>();
	}
	
	public InventoryPageBase(InventorySize size,String title){
		super(null, size.getSize(), (title==null?"Inventory":title));
		this.buttons=new ArrayList<>();
	}
	
	public InventoryPageBase(String inventoryType,int size,String title){
		super(null,InventorySize.invSize(size).getSize(),(title==null?"Inventory":title));
		this.buttons=new ArrayList<>();
		this.inventoryType=inventoryType;
	}
	
	public InventoryPageBase(String inventoryType,InventorySize size,String title){
		super(null, size.getSize(), (title==null?"Inventory":title));
		this.buttons=new ArrayList<>();
		this.inventoryType=inventoryType;
	}
	
	public void useButton(Player player,ActionType type,ItemStack item,int slot){
		if(!isSlot(slot,"useButton(Player,ActionType,ItemStack,int)"))return;
		for(IButton button : buttons){
			if(slot==button.getSlot() ){
				button.Clicked(player, type,item);
				break;
			}
		}
	}
	
	public void remove(){
		buttons.clear();
		buttons=null;
	}
	
	public boolean isSlot(int index, String methode){
		if( index<0 ){
			UtilDebug.debug(this,methode, "index("+index+"<0) ist zu klein.");
			return false;
		}else if( index>getSize() ){
			UtilDebug.debug(this,methode, "index("+index+">"+getSize()+") ist zu groﬂ.");
			return false;
		}
		return true;
	}
	
	public org.bukkit.inventory.ItemStack getItem(int index) {
		if(!isSlot(index,"getItem(int)"))return null;
	    net.minecraft.server.v1_8_R3.ItemStack item = getInventory().getItem(index);
	    return item == null ? null : CraftItemStack.asCraftMirror(item);
	}
	
	public void setItemReflect(int index, org.bukkit.inventory.ItemStack item){
		setItem(index,item);
		setItem(InventorySplit.getSlotRelfect(index), item);
	}
	
	public void setItem(int index, org.bukkit.inventory.ItemStack item){
		if(!isSlot(index,"setItem(int,ItemStack)"))return;
	    getInventory().setItem(index, (item == null) || (item.getTypeId() == 0) ? null : CraftItemStack.asNMSCopy(item));
	}
	
	public int getSlotSort(){
		if(getSize()==InventorySize._9.getSize()){
			if(getItem(4)==null)return 4;
			else if(getItem(5)==null)return 5;
			else if(getItem(3)==null)return 3;
			else if(getItem(6)==null)return 6;
			else if(getItem(2)==null)return 2;
			else if(getItem(7)==null)return 7;
			else if(getItem(1)==null)return 1;
			else if(getItem(8)==null)return 8;
			else if(getItem(0)==null)return 0;
		}
		return 0;
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
		if(!isSlot(slot,"getButton(int)"))return null;
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
		if(!isSlot(slot,"addButton(int,IButton)"))return;
		button.setSlot(slot);
		button.setInventoryPageBase(this);
		setItem(slot, button.getItemStack());
		this.buttons.add(button);
	}
	
	public void closeInventory(){
		if(!getViewers().isEmpty())
			for(int i = 0; i<getViewers().size(); i++)
					getViewers().get(i).closeInventory();
	}
	
	public void updateInventory(){
		if(!getViewers().isEmpty())
			for(HumanEntity h : getViewers())
				if(h!=null && h instanceof Player)
					((Player)h).updateInventory();
	}
	
}
