package me.kingingo.kcore.Inventory.Item;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SalesPackageBase implements IButton{

	@Setter
	@Getter
	private String name;
	@Getter
	@Setter
	private String[] description;
	@Getter
	@Setter
	private ItemStack itemStack;
	@Getter
	private Click click;
	@Getter
	@Setter
	private int slot;
	@Getter
	@Setter
	private InventoryPageBase inventoryPageBase;
	
	public SalesPackageBase(Click click,ItemStack itemStack){
		this.name=(itemStack.hasItemMeta() ? (itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : "") : "");
		this.description=(itemStack.hasItemMeta() ? UtilList.changeListType(itemStack.getItemMeta().getLore()) : new String[]{});
		this.click=click;
		this.itemStack=itemStack;
	}
	
	public SalesPackageBase(Click click,Material material,String name,String[] description){
		this.name=name;
		this.description=description;
		this.click=click;
		this.itemStack=UtilItem.Item(new ItemStack(material), getDescription(), getName());
	}
	
	public SalesPackageBase(Click click,Material material,int data,String name,String[] description){
		this.name=name;
		this.description=description;
		this.click=click;
		this.itemStack=UtilItem.Item(new ItemStack(material,1,(byte)data), getDescription(), getName());
	}

	public void Clicked(Player player, ActionType type,Object object) {
		click.onClick(player, type,object);
	}
	
	public void refreshItemStack(){
		this.inventoryPageBase.setItem(slot, itemStack);
	}
	
	public void setMaterial(Material material,byte data){
		this.itemStack.setData( new MaterialData(material, data) );
	}
	
	public void setMaterial(Material material){
		this.itemStack.setType(material);
	}
	
	public void remove(){
		this.click=null;
		this.name=null;
		this.description=null;
		this.itemStack=null;
		this.slot=0;
		this.inventoryPageBase=null;
	}
	
}