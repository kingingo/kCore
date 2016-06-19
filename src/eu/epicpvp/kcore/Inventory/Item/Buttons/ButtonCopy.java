package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.Item.Click;
import lombok.Getter;

public class ButtonCopy extends ButtonBase{

	@Getter
	public Click set;
	
	public ButtonCopy(Click set ,Click click, ItemStack item) {
		super(click, item);
		this.set=set;
	}

}
