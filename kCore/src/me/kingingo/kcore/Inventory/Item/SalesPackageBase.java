package me.kingingo.kcore.Inventory.Item;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	private Click click;
	@Getter
	@Setter
	private int slot;
	
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
	
}