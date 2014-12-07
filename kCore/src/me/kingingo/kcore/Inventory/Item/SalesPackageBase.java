package me.kingingo.kcore.Inventory.Item;

import lombok.Getter;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SalesPackageBase implements IButton{

	@Getter
	private String name;
	@Getter
	private String[] description;
	@Getter
	private ItemStack itemStack;
	private Click click;
	@Getter
	private int coins;
	@Getter
	private int tokens;
	@Getter
	private Permission permission;
	
	public SalesPackageBase(Click click,Material material,String name,String[] description,Permission permission,int coins,int tokens){
		this.name=name;
		this.description=description;
		this.coins=coins;
		this.tokens=tokens;
		this.permission=permission;
		this.itemStack=UtilItem.Item(new ItemStack(material), getDescription(), getName());
	}
	
	public SalesPackageBase(Click click,Material material,int data,String name,String[] description,Permission permission,int coins,int tokens){
		this.name=name;
		this.description=description;
		this.coins=coins;
		this.tokens=tokens;
		this.permission=permission;
		this.itemStack=UtilItem.Item(new ItemStack(material,data), getDescription(), getName());
	}

	public void Clicked(Player player, ActionType type) {
		click.onClick(player, type);
	}
	
}