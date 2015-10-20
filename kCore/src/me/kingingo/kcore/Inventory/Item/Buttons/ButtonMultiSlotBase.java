package me.kingingo.kcore.Inventory.Item.Buttons;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.IButtonMultiSlot;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonMultiSlotBase implements IButtonMultiSlot{

	@Getter
	@Setter
	private InventoryPageBase inventoryPageBase;
	@Getter
	private ButtonForMultiButtons[] buttons;
	@Getter
	@Setter
	private boolean cancelled=true;
	
	public ButtonMultiSlotBase(ButtonForMultiButtons[] buttons){
		this(buttons,null);
	}
	
	public ButtonMultiSlotBase(ButtonForMultiButtons[] buttons, InventoryPageBase inventoryPageBase){
		this.buttons=buttons;
		this.inventoryPageBase=inventoryPageBase;
	}
	
	public void remove() {
		for(ButtonBase b : buttons)b.remove();
		this.buttons=null;
		this.inventoryPageBase=null;
	}

	public boolean Clicked(int slot,Player player, ActionType type, Object object) {
		for(ButtonBase b : buttons){
			if(b.getSlot()==slot){
				b.Clicked(player, type, object);
				return true;
			}
		}
		return false;
	}

	public boolean isSlot(int slot) {
		for(ButtonBase b : buttons)if(b.getSlot()==slot)return true;
		return false;
	}
}
