package me.kingingo.kcore.Inventory.Item.Buttons;

import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2.InventoryLotto2Type;
import me.kingingo.kcore.Inventory.Item.BooleanClick;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LottoPackage extends SalesPackageBase{
	public static int counter=0;

	//Überprüft ob der User dies schon besitzt!
	private BooleanClick booleanClick;
	@Getter
	private InventoryLotto2.InventoryLotto2Type type;
	@Getter
	private int id=0;
	
	public LottoPackage(Click click,BooleanClick booleanClick,ItemStack itemStack,InventoryLotto2Type type) {
		super(click, itemStack);
		this.booleanClick=booleanClick;
		this.type=type;
		LottoPackage.counter++;
		this.id=LottoPackage.counter;
		
		if(!itemStack.getItemMeta().hasLore()){
			UtilItem.SetDescriptions(itemStack, new String[]{"  ","N"+id});	
		}else{
			ItemMeta meta = itemStack.getItemMeta();
			List<String> l = meta.getLore();
			l.add(" ");
			l.add("§7N"+id);
			meta.setLore(l);
			itemStack.setItemMeta(meta);
		}
	}
	
	public void Clicked(Player player, ActionType type,Object object){
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
