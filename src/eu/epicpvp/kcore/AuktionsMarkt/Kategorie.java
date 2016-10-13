package eu.epicpvp.kcore.AuktionsMarkt;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.kConfig.kConfig;

public class Kategorie extends InventoryPageBase{

	private HashMap<String,ItemKategorie> allowed = new HashMap<>();
	
	public Kategorie(int slot,ItemStack item) {
		super(InventorySize._54,item.getItemMeta().getDisplayName());
		init(slot,item);
	}
	
	public boolean addOffer(int playerId, ItemStack item, double price){
		if(allowed.containsKey(item.getTypeId()+":"+UtilInv.GetData(item))){
			allowed.get(item.getTypeId()+":"+UtilInv.GetData(item)).addOffer(playerId, item, price);
			return true;
		}
		return false;
	}
	
	public void init(int slot,ItemStack item){
		AuktionsMarkt instance = AuktionsMarkt.getAuktionsMarkt();
		instance.getMain().addButton(slot, new ButtonOpenInventory(this, item));
		
		kConfig config = instance.getConfig();
		ConfigurationSection section = config.getConfigurationSection(AuktionsMarkt.PATH+slot+".allowed");
		for(String key : section.getKeys(false)){
			allowed.put(key,new ItemKategorie(this, toItem(key)));
			addButton(section.getInt(AuktionsMarkt.PATH+slot+".allowed."+key), new ButtonBase(new Click() {
				
				@Override
				public void onClick(Player player, ActionType type, Object object) {
					allowed.get(key).open(player);
				}
			},UtilItem.setLore(toItem(key), new String[]{""})));
		}
	}
	
	public ItemStack toItem(String item){
		return new ItemStack(UtilNumber.toInt(item.split(":")[0]),1,UtilNumber.toByte(item.split(":")[1]));
	}

}
