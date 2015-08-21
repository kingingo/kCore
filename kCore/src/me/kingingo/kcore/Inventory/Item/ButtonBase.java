package me.kingingo.kcore.Inventory.Item;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonBase implements IButton{

	@Setter
	private Click click;
	@Setter
	@Getter
	private String name;
	@Setter
	@Getter
	private String[] description;
	@Getter
	private ItemStack itemStack;
	@Getter
	@Setter
	private int slot;
	@Getter
	@Setter
	private InventoryPageBase inventoryPageBase;
	
	public ButtonBase(Click click,Material material,String name,String[] description){
		this.click=click;
		this.description=description;
		this.name=name;
		this.itemStack=UtilItem.Item(new ItemStack(material), getDescription(), getName());
	}
	
	public ButtonBase(Click click,Material material,String name){
		this.click=click;
		this.name=name;
		this.itemStack=UtilItem.RenameItem(new ItemStack(material), getName());
	}

	public ButtonBase(Click click,Material material,int data,String name,String[] description){
		this.click=click;
		this.description=description;
		this.name=name;
		this.itemStack=UtilItem.Item(new ItemStack(material,1,(byte)data), getDescription(), getName());
	}
	
	public ButtonBase(Click click,Material material,int data,String name){
		this.click=click;
		this.name=name;
		this.itemStack=UtilItem.RenameItem(new ItemStack(material,1,(byte)data), getName());
	}
	
	public ButtonBase(Click click,ItemStack item){
		this.click=click;
		if(item.hasItemMeta()&&item.getItemMeta().hasDisplayName())this.name=item.getItemMeta().getDisplayName();
		this.itemStack=item;
	}
	
	public ButtonBase(Click click){
		this.click=click;
		this.itemStack=null;
	}
	
	public void remove(){
		this.click=null;
		this.name=null;
		this.description=null;
		this.itemStack=null;
		this.slot=0;
		this.inventoryPageBase=null;
	}
	
	public void refreshItemStack(){
		this.inventoryPageBase.setItem(slot, itemStack);
	}
	
	public void setItemStack(ItemStack item){
		this.inventoryPageBase.setItem(slot, item);
		this.itemStack=item;
	}
	
	public void Clicked(Player player, ActionType type,Object object) {
		click.onClick(player, type,object);
	}
	
}