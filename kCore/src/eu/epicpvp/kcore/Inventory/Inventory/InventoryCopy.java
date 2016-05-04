package eu.epicpvp.kcore.Inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.IButton;
import eu.epicpvp.kcore.Inventory.Item.IButtonMultiSlot;
import eu.epicpvp.kcore.Inventory.Item.IButtonOneSlot;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonForMultiButtons;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonForMultiButtonsCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonMultiCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesGroupPackageBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;

public class InventoryCopy extends InventoryPageBase{

	@Getter
	@Setter
	private boolean create_new_inv=false;
	@Getter
	@Setter
	private boolean for_with_copy_page = true;
	
	public InventoryCopy(int size, String title) {
		super(size, title);
	}

	public void open(Player player,InventoryBase base){
		if(create_new_inv){
			InventoryPageBase page = clone();
			
			base.addAnother(page);
			player.openInventory(page);
			SalesPackageBase sale;
			ButtonCopy c;
			ButtonMultiCopy cc;
			System.out.println("[open] "+player.getName());
			for(IButton b : (for_with_copy_page ? page.getButtons() : getButtons())){
				if(b instanceof IButtonOneSlot){
					if(b instanceof SalesPackageBase){
						sale = (SalesPackageBase) b;
						if(b instanceof SalesGroupPackageBase){
							SalesGroupPackageBase gsale = (SalesGroupPackageBase) sale;
							Group pgroup = UtilServer.getPermissionManager().getPermissionPlayer(player).getGroups().get(0);
							
							if(UtilServer.getPermissionManager().isBuyableRank(pgroup.getName())){
								if(gsale.getItemStack()!=null && UtilServer.getPermissionManager().getGroupPrice(pgroup.getName()) >= UtilServer.getPermissionManager().getGroupPrice(gsale.getGroup()) ){
									ItemStack item = UtilItem.addEnchantmentGlow(gsale.getItemStack());
									List<String> lores = item.getItemMeta().getLore();
									lores.remove(lores.get(lores.size()-1));
									lores.add("§aBereits gekauft!");
									UtilItem.SetDescriptions(item, lores);
									gsale.setDescription(lores.toArray(new String[]{}));
									
									gsale.setItemStack(item);
									gsale.refreshItemStack();
								}else{
									ItemStack item = gsale.getItemStack();
									List<String> lores = gsale.getItemStack().getItemMeta().getLore();
									lores.remove(lores.get(lores.size()-1));
									lores.add("§cUpgrade Preis: "+UtilServer.getPermissionManager().getUpdgradeGroupPrice(player, gsale.getGroup()));
									UtilItem.SetDescriptions(item, lores);
									gsale.setDescription(lores.toArray(new String[]{}));
									gsale.setItemStack(item);
									gsale.refreshItemStack();
								}
							}
						}else{
							if(sale.getItemStack()!=null&&sale.getPermission()!=null&&player.hasPermission(sale.getPermission().getPermissionToString())){
								sale.setItemStack(UtilItem.addEnchantmentGlow(sale.getItemStack()));
								sale.refreshItemStack();
							}
						}
					}else if(b instanceof ButtonCopy){
						c = (ButtonCopy)b;
						c.getSet().onClick(player, ActionType.AIR, page);
					}
				}else if(b instanceof IButtonMultiSlot){
					if(b instanceof ButtonMultiCopy){
						cc = (ButtonMultiCopy)b;
						cc.setRemove(false);
						cc.setInventoryPageBase(page);
						ButtonForMultiButtonsCopy ccb;
						for (ButtonForMultiButtons cb : cc.getButtons()) {
							cb.setInventoryPageBase(page);
							if(cb instanceof ButtonForMultiButtonsCopy){
								ccb=(ButtonForMultiButtonsCopy)cb;
								if(ccb.getSet()!=null)ccb.getSet().onClick(player, ActionType.AIR, page);
							}
						}
					}
				}
			}
		}else{
			player.openInventory(this);
		}
	}
	
}
