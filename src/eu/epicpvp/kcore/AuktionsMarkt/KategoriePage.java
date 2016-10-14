package eu.epicpvp.kcore.AuktionsMarkt;

import java.util.HashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.kConfig.kConfig;

public class KategoriePage extends InventoryPageBase{
	
	private HashMap<Integer,ItemKategorie> list = new HashMap<>();
	
	public KategoriePage(int kategorie, int page){
		super(InventorySize._54,"");
		init(kategorie, page);
	}
	
	public boolean addOffer(int playerId, ItemStack item, double price){
		for(ItemKategorie ik : list.values()){
			if(ik.addOffer(playerId, item, price)){
				return true;
			}
		}
		return false;
	}
	
	public void init(int kategorie, int page){
		kConfig config = AuktionsMarkt.getAuktionsMarkt().getConfig();
		for(int a = 9; a <= 44; a++){
			if(config.contains(AuktionsMarkt.PATH+kategorie+"."+page+"."+a)){
				int slot = a;
				ItemStack item = toItem( config.getString(AuktionsMarkt.PATH+kategorie+"."+page+"."+slot+".item") );
				
				list.put(slot, new ItemKategorie(this, item));
				addButton(slot, new ButtonBase(new Click(){

					@Override
					public void onClick(Player player, ActionType type, Object object) {
						list.get(slot).open(player);
					}
					
				}, item));
			}
		}
		
		UtilInv.getBase().addPage(this);
	}
	
	public ItemStack toItem(String item){
		return new ItemStack(UtilNumber.toInt(item.split(":")[0]),1,UtilNumber.toByte(item.split(":")[1]));
	}
}
