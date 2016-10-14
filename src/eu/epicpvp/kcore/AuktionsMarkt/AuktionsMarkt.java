package eu.epicpvp.kcore.AuktionsMarkt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryShopBuy;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryShopSell;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.IButton;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBack;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonItemShopMove;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.AnvilGUI;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.AnvilGUI.AnvilClickEvent;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class AuktionsMarkt {
	
	private static AuktionsMarkt instance;
	public final static String PATH = "Main.";
	
	public static AuktionsMarkt getAuktionsMarkt() {
		return instance;
	}
	
	@Getter
	public kConfig config;
	@Getter
	public ArrayList<Offer> offers = new ArrayList<>();
	
	public AuktionsMarkt(){
		AuktionsMarkt.instance=this;
		UtilServer.getCommandHandler().register(CommandMarkt.class, new CommandMarkt());
		this.config=new kConfig(UtilFile.getYMLFile(UtilServer.getPluginInstance(), "auktions_markt.yml"));
		
		for(int i=1; i<256; i++){
			addOffer(UtilServer.getClient().getPlayerAndLoad("kingingo").getPlayerId(), Material.values()[i], ((byte)0), i, i);
		}
		
		loadBackup();
	}
	
	public void loadBackup(){
		if(new File(UtilFile.getYMLFile(UtilServer.getPluginInstance(), "auktions_markt_backup.yml")).exists()){
			kConfig backup = new kConfig(UtilFile.getYMLFile(UtilServer.getPluginInstance(), "auktions_markt_backup.yml"));
			
			for(String uuid : backup.getKeys(false)){
				int playerId = backup.getInt(uuid+".playerId");
				int id = backup.getInt(uuid+".id");
				byte data = UtilNumber.toByte(backup.getInt(uuid+".data"));
				long end = backup.getLong(uuid+".end");
				int amount = backup.getInt(uuid+".amount");
				double price = backup.getDouble(uuid+".price");
				
				offers.add(new Offer(playerId,UtilServer.getClient().getPlayerAndLoad(playerId).getName(), Material.getMaterial(id), data, amount, price, end));
			}
			
			backup=null;
			new File(UtilFile.getYMLFile(UtilServer.getPluginInstance(), "auktions_markt_backup.yml")).delete();
		}
	}
	
	public void saveBackup(){
		kConfig backup = new kConfig(UtilFile.getYMLFile(UtilServer.getPluginInstance(), "auktions_markt_backup.yml"));
		
		for(Offer offer : offers){
			UUID uuid = UUID.randomUUID();
			backup.set(uuid+".playerId", offer.getPlayerId());
			backup.set(uuid+".id", offer.getMaterial().getId());
			backup.set(uuid+".data", offer.getData());
			backup.set(uuid+".end", (offer.getEnd()-System.currentTimeMillis()));
			backup.set(uuid+".price", offer.getPrice());
		}
		
		backup.save();
	}
	
	public List<Offer> getPlayerOffers(int playerId){
		return offers.stream().filter(offer -> offer.getPlayerId() == playerId).collect(Collectors.toList());
	}
	
	public void addOffer(int playerId, Material material, byte data, int amount, double price){
		offers.add(new Offer(playerId,UtilServer.getClient().getPlayerAndLoad(playerId).getName(), material, data, amount, price, TimeSpan.DAY));
	}
	
	public void open(Player player){
		InventoryPageBase page = new InventoryPageBase(InventorySize._54, "");
		InventorySplit._18.setLine(Material.STAINED_GLASS_PANE, ((byte)7), page);
		page.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				InventoryPageBase user_page = new InventoryPageBase(InventorySize._54, "");
				InventorySplit._18.setLine(Material.STAINED_GLASS_PANE, ((byte)7), user_page);
				user_page.addButton(0, new ButtonBase(new Click(){

					@Override
					public void onClick(Player player, ActionType type, Object object) {
						AuktionsMarkt.getAuktionsMarkt().open(player);
					}
					
				}, UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§aZurück")));
				
				List<Offer> offers = getPlayerOffers(UtilPlayer.getPlayerId(player));
				int slot = InventorySplit._27.getMin();
				for(int i = 0; i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin()); i++){
					if(offers.size() <= i){
						break;
					}
					offers.get(i).update();
					user_page.addButton(slot, offers.get(i));
					slot++;
				}
				
				user_page.addButton(7, new ButtonBase(new Click() {
					
					@Override
					public void onClick(Player player, ActionType type, Object object) {
						if(user_page.getItem(7)==null)return;
						int page_amount = getPageNumber(user_page.getItem(7));
						for(int i = InventorySplit._27.getMin(); i <= InventorySplit._54.getMax(); i++)user_page.getButtons().remove(user_page.getButton(i));
						
						int slot = InventorySplit._27.getMin();
						for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
							if(offers.size() <= i){
								break;
							}
							offers.get(i).update();
							user_page.addButton(slot, offers.get(i));
							slot++;
						}
						
						user_page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount+1)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
						if(page_amount!=1){
							user_page.setItem(7, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)) );
						}else{
							user_page.setItem(7, null);
						}
					}
				}, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" 1")));
				user_page.setItem(7, null);
				
				user_page.addButton(8, new ButtonBase(new Click() {
					
					@Override
					public void onClick(Player player, ActionType type, Object object) {
						if(user_page.getItem(8)==null)return;
						int page_amount = getPageNumber(user_page.getItem(8));
						for(int i = InventorySplit._27.getMin(); i <= InventorySplit._54.getMax(); i++)user_page.getButtons().remove(user_page.getButton(i));
						
						int slot = InventorySplit._27.getMin();
						for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
							if(offers.size() <= i){
								break;
							}
							offers.get(i).update();
							user_page.addButton(slot, offers.get(i));
							slot++;
						}
						
						if(offers.size() < ((InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount))){
							user_page.setItem(8, null);
						}else{
							user_page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount+1)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
						}
						user_page.setItem(7, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)) );
					}
				}, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+2+" "+Zeichen.DOUBLE_ARROWS_R.getIcon())));
				if(offers.size()<(InventorySplit._54.getMax()-InventorySplit._27.getMin()))user_page.setItem(8, null);
				player.openInventory(user_page);
				user_page.setRemoveButtons(false);
				UtilInv.getBase().addAnother(user_page);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.BOOK), "§6Deine Verkäufe")));
		
		int slot = InventorySplit._27.getMin();
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
				if(page.getItem(7)==null)return;
				int page_amount = getPageNumber(page.getItem(7));
				for(int i = InventorySplit._27.getMin(); i <= InventorySplit._54.getMax(); i++)page.getButtons().remove(page.getButton(i));
				
				int slot = InventorySplit._27.getMin();
				System.out.println("BACK->  page=["+page_amount+"] "+((InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1))+" TO "+(InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount));
				for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
					if(offers.size() <= i){
						break;
					}
					offers.get(i).update();
					page.addButton(slot, offers.get(i));
					slot++;
				}
				
				page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount+1)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
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
				if(page.getItem(8)==null)return;
				int page_amount = getPageNumber(page.getItem(8));
				for(int i = InventorySplit._27.getMin(); i <= InventorySplit._54.getMax(); i++)page.getButtons().remove(page.getButton(i));
				
				int slot = InventorySplit._27.getMin();
				for(int i = (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount-1); i <= (InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount); i++){
					if(offers.size() <= i){
						break;
					}
					offers.get(i).update();
					page.addButton(slot, offers.get(i));
					slot++;
				}
				
				if(offers.size() < ((InventorySplit._54.getMax()-InventorySplit._27.getMin())*(page_amount))){
					page.setItem(8, null);
				}else{
					page.setItem(8, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount+1)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()) );
				}
				page.setItem(7, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)) );
			}
		}, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+2+" "+Zeichen.DOUBLE_ARROWS_R.getIcon())));
		if(this.offers.size()<(InventorySplit._54.getMax()-InventorySplit._27.getMin()))page.setItem(8, null);
		player.openInventory(page);
		page.setRemoveButtons(false);
		UtilInv.getBase().addAnother(page);
	}
	
	public int getPageNumber(ItemStack item){
		return UtilNumber.toInt(item.getItemMeta().getDisplayName().split(" ")[1]);
	}
}
