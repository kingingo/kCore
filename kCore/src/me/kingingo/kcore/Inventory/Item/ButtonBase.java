package me.kingingo.kcore.Inventory.Item;

import lombok.Getter;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonBase implements IButton{
	
	private Click click;
	@Getter
	private String name;
	@Getter
	private String[] description;
	@Getter
	private ItemStack itemStack;
	
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
	
	public void Clicked(Player player, ActionType type,Object object) {
		click.onClick(player, type,object);
	}
	
}