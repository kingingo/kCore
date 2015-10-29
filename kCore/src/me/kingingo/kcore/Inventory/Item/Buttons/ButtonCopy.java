package me.kingingo.kcore.Inventory.Item.Buttons;

import lombok.Getter;
import me.kingingo.kcore.Inventory.Item.Click;

import org.bukkit.inventory.ItemStack;

public class ButtonCopy extends ButtonBase{

	@Getter
	public Click set;
	
	public ButtonCopy(Click set ,Click click, ItemStack item) {
		super(click, item);
		this.set=set;
	}

}
