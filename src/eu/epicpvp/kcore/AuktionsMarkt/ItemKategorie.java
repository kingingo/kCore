package eu.epicpvp.kcore.AuktionsMarkt;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBack;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import lombok.Getter;

public class ItemKategorie{

	@Getter
	private ItemStack item;
	@Getter
	private ArrayList<Offer> offers = new ArrayList<>();
	private Kategorie kategorie;
	
	public ItemKategorie(Kategorie kategorie, ItemStack item) {
		this.item=item;
		this.kategorie=kategorie;
	}
	
	public void open(Player player){
		InventoryPageBase page = new InventoryPageBase(InventorySize._54, "");
		InventorySplit._18.setLine(Material.STAINED_GLASS_PANE, ((byte)7), page);
		page.addButton(0, new ButtonBack(kategorie, UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§cZurück")));
		
		int slot = InventorySplit._27.getMin();
		for(int i = 0; i < (InventorySplit._54.getMax()-InventorySplit._27.getMin()); i++){
			offers.get(i).update();
			page.addButton(slot, offers.get(i));
		}
		
		page.addButton(7, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				int page_amount = getPageNumber((ButtonBase)page.getButton(7));
				
				int slot = InventorySplit._27.getMin();
				for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i < (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
					page.addButton(slot, offers.get(i));
				}
				
				page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
				if(page_amount!=1){
					page.setItem(7, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)) );
				}else{
					page.setItem(7, null);
				}
			}
		}, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon())));
		page.setItem(7, null);
		
		page.addButton(8, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				int page_amount = getPageNumber((ButtonBase)page.getButton(8));
				
				int slot = InventorySplit._27.getMin();
				for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i < (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
					page.addButton(slot, offers.get(i));
				}
				
				page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount+1)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
				page.setItem(7, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)) );
			}
		}, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+2+" "+Zeichen.DOUBLE_ARROWS_R.getIcon())));
		
		UtilInv.getBase().addAnother(page);
		player.openInventory(page);
	}
	
	public int getPageNumber(ButtonBase button){
		return UtilNumber.toInt(button.getItemStack().getItemMeta().getDisplayName().split(" ")[1]);
	}

	public boolean addOffer(int playerId, ItemStack item, double price){
		if(item.isSimilar(item)){
			offers.add(new Offer(this,playerId,item,price, TimeSpan.DAY));
			return true;
		}
		return false;
	}
}
