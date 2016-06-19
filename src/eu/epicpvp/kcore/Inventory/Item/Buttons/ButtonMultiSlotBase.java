package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.IButton;
import eu.epicpvp.kcore.Inventory.Item.IButtonMultiSlot;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import lombok.Getter;
import lombok.Setter;

public class ButtonMultiSlotBase implements IButtonMultiSlot{

	@Getter
	@Setter
	private InventoryPageBase inventoryPageBase;
	@Getter
	private ButtonForMultiButtons[] buttons;
	@Getter
	@Setter
	private boolean cancelled=true;
	@Setter
	private boolean remove=true;
	
	public ButtonMultiSlotBase(ButtonForMultiButtons[] buttons){
		this(buttons,null);
	}
	
	public ButtonMultiSlotBase(ButtonForMultiButtons[] buttons, InventoryPageBase inventoryPageBase){
		this.buttons=buttons;
		this.inventoryPageBase=inventoryPageBase;
	}
	
	public void remove() {
		if(remove)for(ButtonBase b : buttons)b.remove();
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
