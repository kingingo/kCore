package me.kingingo.kcore.Inventory.Item;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BooleanButton implements IButton{

	@Setter
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
	
	public BooleanButton(Click click,String name,boolean toggle){
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
	
	public void Clicked(Player player, ActionType type,Object object) {
		if(toggle){
			onOroff(false);
		}else{
			onOroff(true);
		}
		click.onClick(player, type,object);
	}

}
