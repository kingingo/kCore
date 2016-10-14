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
	private KategoriePage kategorie;
	
	public ItemKategorie(KategoriePage kategorie, ItemStack item) {
		this.item=item;
		this.kategorie=kategorie;
	}
	
	public boolean open(Player player){
		System.out.println("OPEN: "+offers.isEmpty()+" "+item.getType().toString()+" "+offers.size());
		if(!offers.isEmpty()){
			InventoryPageBase page = new InventoryPageBase(InventorySize._54, "");
			page.setRemoveButtons(false);
			InventorySplit._18.setLine(Material.STAINED_GLASS_PANE, ((byte)7), page);
			page.addButton(0, new ButtonBack(kategorie, UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§cZurück")));
			
			int slot = InventorySplit._27.getMin();
			System.out.println("SETUP 0 -> "+(InventorySplit._54.getMax()-InventorySplit._27.getMin()));
			for(int i = 0; i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin()); i++){
				if(offers.size() <= i){
					break;
				}
				offers.get(i).update();
				page.addButton(slot, offers.get(i));
				slot++;
			}
			
			page.addButton(7, new ButtonBase(new Click() {
				
				@Override
				public void onClick(Player player, ActionType type, Object object) {
					int page_amount = getPageNumber((ButtonBase)page.getButton(7));
					for(int i = InventorySplit._27.getMin(); i <= InventorySplit._54.getMax(); i++)page.getButtons().remove(page.getButton(i));
					
					int slot = InventorySplit._27.getMin();
					System.out.println("BACK-> "+((InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1))+" TO "+(InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount));
					for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
						if(offers.size() <= i){
							System.out.println("BACK BREAK AT "+i);
							break;
						}
						page.addButton(slot, offers.get(i));
						slot++;
					}
					
					page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
					if(page_amount!=1){
						page.setItem(7, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)) );
					}else{
						page.setItem(7, null);
					}
				}
			}, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" 1")));
			page.setItem(7, null);
			
			page.addButton(8, new ButtonBase(new Click() {
				
				@Override
				public void onClick(Player player, ActionType type, Object object) {
					int page_amount = getPageNumber((ButtonBase)page.getButton(8));
					for(int i = InventorySplit._27.getMin(); i <= InventorySplit._54.getMax(); i++)page.getButtons().remove(page.getButton(i));
					
					int slot = InventorySplit._27.getMin();
					System.out.println("FORTH-> "+(InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1)+" TO "+(InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount));
					for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
						if(offers.size() <= i){
							System.out.println("FORTH BREAK AT "+i);
							break;
						}
						System.out.println("FORTH AT "+i);
						page.addButton(slot, offers.get(i));
						slot++;
					}
					
					page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount+1)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
					page.setItem(7, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)) );
				}
			}, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+2+" "+Zeichen.DOUBLE_ARROWS_R.getIcon())));

			player.openInventory(page);
			UtilInv.getBase().addAnother(page);
			System.out.println("OPEN: "+player.getName());
			return true;
		}
		return false;
	}
	
	public int getPageNumber(ButtonBase button){
		System.out.println("N: "+button.getItemStack().getItemMeta().getDisplayName());
		return UtilNumber.toInt(button.getItemStack().getItemMeta().getDisplayName().split(" ")[1]);
	}

	public boolean addOffer(int playerId, ItemStack item, double price){
		if(item.isSimilar(getItem())){
			System.out.println("ADD OFFER playerId=["+playerId+"], price=["+price+"]  "+item.getType().toString()+" -> "+getItem().getType().toString());
			offers.add(new Offer(this,playerId,item,price, TimeSpan.DAY));
			return true;
		}
		return false;
	}
}
