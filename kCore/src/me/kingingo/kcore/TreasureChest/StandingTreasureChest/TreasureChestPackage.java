package me.kingingo.kcore.TreasureChest.StandingTreasureChest;

import me.kingingo.kcore.Inventory.Item.BooleanClick;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.SalesPackageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TreasureChestPackage extends SalesPackageBase{

	//Überprüft ob der User dies schon besitzt!
	private BooleanClick booleanClick;
	
	public TreasureChestPackage(Click click,BooleanClick booleanClick,ItemStack itemStack) {
		super(click, itemStack);
		this.booleanClick=booleanClick;
	}
	
	public void click(Player player){
		if(getClick()==null){
			player.getInventory().addItem(getItemStack().clone());
		}else{
			Clicked(player,ActionType.R,getItemStack());
		}
	}
	
	public boolean hasPlayer(Player player){
		if(booleanClick==null)return false;
		return booleanClick.onBooleanClick(player, ActionType.R,getItemStack());
	}

}
