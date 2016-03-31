package eu.epicpvp.kcore.TreasureChest.StandingTreasureChest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.Item.BooleanClick;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;

public class TreasureChestPackage extends SalesPackageBase{

	//§berpr§ft ob der User dies schon besitzt!
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
