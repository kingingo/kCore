package eu.epicpvp.kcore.AuktionsMarkt;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBack;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.kConfig.kConfig;

public class Kategorie{
	private ArrayList<KategoriePage> pages = Lists.newArrayList();
	
	public Kategorie(int kategorie) {
		init(kategorie);
	}
	
	public boolean addOffer(int playerId, ItemStack item, double price){
		for(KategoriePage page : pages){
			if(page.addOffer(playerId, item, price)){
				return true;
			}
		}
		return false;
	}
	
	public void init(int kategorie){
		kConfig config = AuktionsMarkt.getAuktionsMarkt().getConfig();
		
		KategoriePage page = null;
		for(int p = 1; p <= 30; p++){
			if(config.contains(AuktionsMarkt.PATH+kategorie+"."+p)){
				int pagenumber = p;
				KategoriePage newpage = new KategoriePage(kategorie,pagenumber);
				
				if(page != null){
					page.addButton(InventorySplit._54.getMiddle()+1, new ButtonBack(newpage, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§7"+(pagenumber)+" "+Zeichen.DOUBLE_ARROWS_R)));
					newpage.addButton(InventorySplit._54.getMiddle()-1, new ButtonBack(page, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§7"+Zeichen.DOUBLE_ARROWS_l+" "+(pagenumber-1))));
				}else{
					newpage.addButton(InventorySplit._54.getMiddle()-1, new ButtonBack(AuktionsMarkt.getAuktionsMarkt().getMain(), UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§aZurück")));
					AuktionsMarkt.getAuktionsMarkt().getMain().addButton(kategorie, new ButtonOpenInventory(newpage, config.getItemStack(AuktionsMarkt.PATH+kategorie+".item")));
				}
				
				pages.add(newpage);
				page=newpage;
			}
		}
	}
}
