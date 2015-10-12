package me.kingingo.kcore.Inventory.Item.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.IButtonMultiSlot;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

public class ButtonMultiSlotBase implements IButtonMultiSlot{

	@Getter
	@Setter
	private InventoryPageBase inventoryPageBase;
	@Getter
	private Click click;
	@Getter
	@Setter
	private Integer[] slots;
	@Getter
	@Setter
	private boolean cancelled=true;
	
	public ButtonMultiSlotBase(Click click, Integer[] slots){
		this(click,null,slots);
	}
	
	public ButtonMultiSlotBase(Click click, InventoryPageBase inventoryPageBase, Integer[] slots){
		this.click=click;
		this.slots=slots;
		this.inventoryPageBase=inventoryPageBase;
	}
	
	public void remove() {
		for(int slot : slots)if(getInventoryPageBase().getItem(slot)!=null&&getInventoryPageBase().getItem(slot).getType()!=Material.AIR)getInventoryPageBase().getItem(slot).setType(Material.AIR);
		this.slots=null;
		this.click=null;
		this.inventoryPageBase=null;
	}

	public void Clicked(Player player, ActionType type, Object object) {
		click.onClick(player, type, object);
	}

	public boolean isSlot(int slot) {
		for(int s : slots)if(s==slot)return true;
		return false;
	}
}
