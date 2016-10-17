package eu.epicpvp.kcore.AuktionsMarkt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.datenserver.definitions.dataserver.player.LanguageType;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;
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
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class AuktionsMarkt extends kListener{
	
	static{
		TranslationHandler.registerFallback(LanguageType.GERMAN, "markt.offer.added", "§aDas Angebot wurde hinzugefügt");
		TranslationHandler.registerFallback(LanguageType.GERMAN, "markt.offer.closed", "§aEin Angebot wurde von dir geschlossen.");

		TranslationHandler.registerFallback(LanguageType.ENGLISH, "markt.offer.closed", "§aAn offer of you was closed.");
		TranslationHandler.registerFallback(LanguageType.ENGLISH, "markt.offer.added", "§aYou have added the offer!");
	}
	
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
		super(UtilServer.getPluginInstance(),"AuktionsMarkt");
		AuktionsMarkt.instance=this;
		UtilServer.getCommandHandler().register(CommandMarkt.class, new CommandMarkt());
		UtilServer.getCommandHandler().register(CommandSell.class, new CommandSell());
		this.config=new kConfig(UtilFile.getYMLFile(UtilServer.getPluginInstance(), "auktions_markt.yml"));
		
		addOffer(UtilServer.getClient().getPlayerAndLoad("kingingo").getPlayerId(), Material.STONE, ((byte)0), 4000, 1000);
		
		loadBackup();
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getView().getTopInventory().getHolder() instanceof AuktionsInventoryHolder)) {
			return;
		}
		if(event.getCurrentItem()!=null&&event.getCurrentItem().getType()==Material.ARROW 
				&& event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()
				&& event.getClickedInventory().getHolder() instanceof AuktionsInventoryHolder){
			if(event.getCurrentItem().getItemMeta().getDisplayName().contains(Zeichen.DOUBLE_ARROWS_R.getIcon()) 
						^ event.getCurrentItem().getItemMeta().getDisplayName().contains(Zeichen.DOUBLE_ARROWS_l.getIcon())){
				AuktionsInventoryHolder holder = (AuktionsInventoryHolder) event.getClickedInventory().getHolder();
				kConfig config = holder.getConfig();
				ItemStack[] items = config.getItemStackArray("auktionsMartk.items");
				int page_amount = getPageNumber( event.getClickedInventory().getItem(event.getSlot()) );
				event.getClickedInventory().clear();
				
				if(page_amount!=1){
					event.getClickedInventory().setItem(InventorySplit._54.getMiddle()-1, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" "+(page_amount-1)));
				}else{
					event.getClickedInventory().setItem(InventorySplit._54.getMiddle()-1, UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§aZurück"));
				}
				
				event.getClickedInventory().setItem(InventorySplit._54.getMiddle()+1, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+(page_amount+1)+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()));
				int slot=0;
				for(int i = (InventorySize._45.getSize())*(page_amount-1); i < (InventorySize._45.getSize())*(page_amount); i++){
					if(items.length <= i){
						event.getClickedInventory().setItem(InventorySplit._54.getMiddle()+1, null);
						break;
					}
					event.getClickedInventory().setItem(slot, items[i]);
					slot++;
				}
				
				holder.setPage(page_amount);
				event.setCancelled(true);
				return;
			}
		}else if(event.getCurrentItem()!=null&&event.getCurrentItem().getType()==Material.SLIME_BALL 
				&& event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()
				&& event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aZurück")
				&& event.getClickedInventory().getHolder() instanceof AuktionsInventoryHolder){
			open((Player)event.getWhoClicked());
			event.setCancelled(true);
			return;
		}
		
		switch (event.getAction()) {
			case HOTBAR_MOVE_AND_READD:
			case COLLECT_TO_CURSOR:
			case CLONE_STACK:
			case HOTBAR_SWAP:
			case DROP_ONE_SLOT:
			case DROP_ALL_SLOT:
			case DROP_ALL_CURSOR:
			case DROP_ONE_CURSOR:
			case NOTHING:
			case UNKNOWN:
				event.setCancelled(true);
				break;
			case PICKUP_ALL:
			case PICKUP_HALF:
			case PICKUP_ONE:
			case PICKUP_SOME:
				changeInv(event.getClickedInventory(), event.getSlot());
				break;
			case MOVE_TO_OTHER_INVENTORY:
				if (!(event.getClickedInventory().getHolder() instanceof AuktionsInventoryHolder)) {
					event.setCancelled(true);
				}else{
					changeInv(event.getClickedInventory(), event.getSlot());
				}
				break;
			case PLACE_ALL:
			case PLACE_ONE:
			case PLACE_SOME:
				//that is a xor operation, exactly one of both has to be true, the other one false
				if (event.isShiftClick() ^ event.getClickedInventory().getHolder() instanceof AuktionsInventoryHolder) {
					event.setCancelled(true);
				}
				break;
			case SWAP_WITH_CURSOR:
				if (event.getClickedInventory().getHolder() instanceof AuktionsInventoryHolder && (event.getCursor() != null && event.getCursor().getType() != Material.AIR)) {
					event.setCancelled(true);
				}
				break;
		}
		if (event.isCancelled()) {
			getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> ((Player) event.getWhoClicked()).updateInventory());
		}
	}
	
	public void changeInv(Inventory inventory, int slot){
		if(inventory.getHolder() instanceof AuktionsInventoryHolder){
			AuktionsInventoryHolder holder = (AuktionsInventoryHolder)inventory.getHolder();
			kConfig config = holder.getConfig();
			ItemStack[] items = config.getItemStackArray("auktionsMartk.items");
			items[ ((holder.getPage()-1)*InventorySize._45.getSize())+(slot) ] = null;
			config.setItemStackArray("auktionsMartk.items", items);
		}
	}
	
	public boolean addOffer(Player plr,double price){
		ItemStack item = plr.getItemInHand();
		
		if(item!=null){
			plr.setItemInHand(null);
			addOffer(UtilPlayer.getPlayerId(plr), item.getType(), UtilInv.GetData(item), item.getAmount(), price);
			plr.sendMessage(TranslationHandler.getPrefixAndText(plr, "markt.offer.added"));
			return true;
		}
		return false;
	}
	
	public boolean openPlayerInventory(Player plr){
		kConfig config = UtilServer.getUserData().getConfig(plr);
		ItemStack[] items = config.getItemStackArray("auktionsMartk.items");
		
		if(items!=null){
			AuktionsInventoryHolder holder = new AuktionsInventoryHolder(config);
			Inventory inv = Bukkit.createInventory(holder, InventorySize._54.getSize(), plr.getName()+"'s Inv");
			holder.setInventory(inv);
			
			for(int i = 0; i <= (items.length<=InventorySize._45.getSize()? items.length : InventorySplit._45.getMax()); i++){
				inv.setItem(i, items[i]);
			}
			
			if(items.length>InventorySize._45.getSize()){
				inv.setItem(InventorySplit._54.getMiddle()+1, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§e "+2+" "+Zeichen.DOUBLE_ARROWS_R.getIcon()));
			}
			inv.setItem(InventorySplit._54.getMiddle()-1, UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§aZurück"));
			
			plr.openInventory(inv);
			return true;
		}
		return false;
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
		page.addButton(1, new ButtonBase( (Player plr, ActionType type,Object object) -> openPlayerInventory(plr) , UtilItem.RenameItem(new ItemStack(Material.CHEST), "§6Deposit")));
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
