package me.kingingo.kcore.Pet.Shop;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.SalesPackageBase;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Tokens;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PetShop extends InventoryBase{

	@Getter
	private PetManager manager;
	
	public PetShop(PetManager manager,Coins coins,Tokens tokens){
		super(manager.getInstance(),"Pet-Shop",tokens,coins);
		this.manager=manager;
		
		getMain().setItem(0, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(1, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(2, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(3, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(4, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(5, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(6, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(7, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(8, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));	
		getMain().setItem(9, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(17, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));	
		getMain().setItem(18, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(19, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(20, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(21, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(22, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(23, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(24, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(25, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(26, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		
//		getMain().addButton(new SalesPackageBase(new Click(){
//			public void onClick(Player player, ActionType type) {
//				if(type == ActionType.L||type == ActionType.R){
//					
//				}
//			}
//			
//		}, Material.MONSTER_EGG,90, "Pig", new String[]{""},Permission.PET_PIG,4000,100));
	}
	
}
