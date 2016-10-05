package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Merchant.Merchant;
import eu.epicpvp.kcore.Merchant.MerchantOffer;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;

public class InventoryMerchant extends InventoryPageBase{
	
	public InventoryMerchant(Merchant merchant,InventoryPageBase backbutton){
		this(merchant);
		
		addButton(0, new ButtonOpenInventory(backbutton, UtilItem.RenameItem(new ItemStack(Material.BARRIER), "§cback")));
	}
	
	public InventoryMerchant(Merchant merchant){
		super(InventorySize.invSize(merchant.getOffers().size()+1), merchant.getTitle());
		
		int slot = InventorySize.invSize(merchant.getOffers().size()+1).getSize()-1;
		for(MerchantOffer offer : merchant.getOffers()){
			if(offer.getOutput()!=null&&offer.getOutput().getType()==Material.AIR){
				throw new NullPointerException("offer.getOutput().getType()==Material.AIR");
			}
			
			addButton(slot, new SalesPackageBase(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(type==ActionType.SHIFT_LEFT||type==ActionType.SHIFT_RIGHT){
						if(UtilInv.contains(player, offer.getFirstInput().getType(), UtilInv.GetData(offer.getFirstInput()), offer.getFirstInput().getAmount())){
							int a = UtilInv.AnzahlInInventory(player, offer.getFirstInput().getType(), UtilInv.GetData(offer.getFirstInput()));
							a = a / offer.getFirstInput().getAmount();
							
							UtilInv.remove(player, offer.getFirstInput(), a*offer.getFirstInput().getAmount());
							ItemStack out = offer.getOutput().clone();
							out.setAmount(a*offer.getOutput().getAmount());
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
