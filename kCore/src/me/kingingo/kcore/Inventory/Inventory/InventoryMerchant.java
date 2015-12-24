package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Merchant.Merchant;
import me.kingingo.kcore.Merchant.MerchantOffer;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryMerchant extends InventoryPageBase{
	
	public InventoryMerchant(Merchant merchant,InventoryPageBase backbutton){
		this(merchant);
		
		addButton(0, new ButtonOpenInventory(backbutton, UtilItem.RenameItem(new ItemStack(Material.BARRIER), "§cback")));
	}
	
	public InventoryMerchant(Merchant merchant){
		super(InventorySize.invSize(merchant.getOffers().size()+1), merchant.getTitle());
		
		int slot = InventorySplit._9.getMax();
		for(MerchantOffer offer : merchant.getOffers()){
			
			addButton(slot, new SalesPackageBase(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(type==ActionType.SHIFT_LEFT||type==ActionType.SHIFT_RIGHT){
						if(UtilInv.contains(player, offer.getFirstInput().getType(), UtilInv.GetData(offer.getFirstInput()), offer.getFirstInput().getAmount())){
							int a = UtilInv.AnzahlInInventory(player, offer.getFirstInput().getType(), UtilInv.GetData(offer.getFirstInput()));
							UtilInv.remove(player, offer.getFirstInput(), a);
							a=a*offer.getOutput().getAmount();
							ItemStack out = offer.getOutput().clone();
							out.setAmount(a);
							player.getInventory().addItem(out);
							out=null;
						}
					}else{
						if(UtilInv.contains(player, offer.getFirstInput().getType(), UtilInv.GetData(offer.getFirstInput()), offer.getFirstInput().getAmount())){
							UtilInv.remove(player, offer.getFirstInput(), offer.getFirstInput().getAmount());
							player.getInventory().addItem(offer.getOutput());
						}
					}
				}
				
			}, UtilItem.Item(offer.getOutput(), new String[]{ "§e"+offer.getFirstInput().getAmount()+" "+offer.getFirstInput().getItemMeta().getDisplayName() }, offer.getOutput().getItemMeta().getDisplayName())));
			
			slot--;
		}
		
		fill(Material.STAINED_GLASS_PANE, (byte)7);
	}

}
