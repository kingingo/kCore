package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.IButtonOneSlot;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;
import lombok.Setter;

public class ButtonBooleanInventory implements IButtonOneSlot{

	@Setter
	@Getter
	private Click click;
	@Setter
	@Getter
	private String name;
	@Setter
	@Getter
	private String[] description;
	@Setter
	@Getter
	private ItemStack itemStack;
	@Getter
	@Setter
	private int slot;
	@Getter
	@Setter
	private InventoryPageBase inventoryPageBase;
	@Getter
	private boolean toggle;
	@Getter
	@Setter
	private boolean cancelled=true;
	
	public ButtonBooleanInventory(Click click,String name,boolean toggle){
		this.click=click;
		this.name=name;
		onOroff(toggle);
	}
	
	public void onOroff(boolean toggle){
		this.toggle=toggle;
		if(toggle){
			this.description=new String[]{" "};
			this.name=name.substring(0, name.indexOf(" | "))+"§a ON";
			this.itemStack=UtilItem.Item(new ItemStack(Material.WOOL,1,(byte)5), getDescription(), getName());
		}else{
			this.description=new String[]{" "};
			this.name=name.substring(0, name.indexOf(" | "))+"§4 OFF";
			this.itemStack=UtilItem.Item(new ItemStack(Material.WOOL,1,(byte)14), getDescription(), getName());
		}
		getInventoryPageBase().setItem(getSlot(), getItemStack());
	}
	
	public void refreshItemStack(){
		UtilItem.SetDescriptions(itemStack, getDescription());
		this.inventoryPageBase.setItem(slot, itemStack);
	}
	
	public void setMaterial(Material material,byte data){
		this.itemStack.setData( new MaterialData(material, data) );
	}
	
	public void setMaterial(Material material){
		this.itemStack.setType(material);
	}
	
	public boolean isSlot(int slot){
		return slot==getSlot();
	}
	
	public void Clicked(Player player, ActionType type,Object object) {
		if(toggle){
			onOroff(false);
		}else{
			onOroff(true);
		}
		click.onClick(player, type,object);
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
