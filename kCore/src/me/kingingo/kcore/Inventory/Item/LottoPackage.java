package me.kingingo.kcore.Inventory.Item;

import lombok.Getter;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2.InventoryLotto2Type;
import me.kingingo.kcore.Inventory.Item.BooleanClick;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.SalesPackageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LottoPackage extends SalesPackageBase{

	//Überprüft ob der User dies schon besitzt!
	private BooleanClick booleanClick;
	@Getter
	private InventoryLotto2.InventoryLotto2Type type;
	
	public LottoPackage(Click click,BooleanClick booleanClick,ItemStack itemStack,InventoryLotto2Type type) {
		super(click, itemStack);
		this.booleanClick=booleanClick;
		this.type=type;
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
