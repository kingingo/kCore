package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.IButton;
import eu.epicpvp.kcore.Inventory.Item.IButtonOneSlot;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;
import lombok.Setter;

public class ButtonBase implements IButtonOneSlot{

	@Setter
	@Getter
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
	@Getter
	@Setter
	private boolean cancelled=true;
	
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
	
	public void setMaterial(Material material,byte data){
		this.itemStack.setType(material);
		this.itemStack.setData(new MaterialData(material,data));
	}
	
	public void setMaterial(Material material){
		this.itemStack.setType(material);
	}
	
	public boolean isSlot(int slot){
		return slot==getSlot();
	}
	
	public void remove(){
		this.click=null;
		this.name=null;
		this.description=null;
		this.itemStack=null;
		if(this.inventoryPageBase!=null)this.inventoryPageBase.setItem(slot, null);
		this.slot=0;
		this.inventoryPageBase=null;
	}
	
	public void refreshItemStack(){
		if(getDescription()!=null)UtilItem.SetDescriptions(itemStack, getDescription());
		this.inventoryPageBase.setItem(slot, itemStack);
	}
	
	public void setItemStack(ItemStack item){
		this.inventoryPageBase.setItem(slot, item);
		this.itemStack=item;
	}
	
	public void Clicked(Player player, ActionType type,Object object) {
		if(click!=null)click.onClick(player, type,object);
	}
	
	@Override
	public IButton clone() {
		try{
			return (IButton) super.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}