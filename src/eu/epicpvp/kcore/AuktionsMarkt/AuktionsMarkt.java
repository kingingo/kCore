package eu.epicpvp.kcore.AuktionsMarkt;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryShopBuy;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryShopSell;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.IButton;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBack;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonItemShopMove;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Util.AnvilGUI;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
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
	public InventoryPageBase main;
	@Getter
	private ArrayList<Kategorie> kategorieList = new ArrayList<>();
	
	public AuktionsMarkt(){
		AuktionsMarkt.instance=this;
		UtilServer.getCommandHandler().register(CommandMarkt.class, new CommandMarkt());
		initMarkt();
	}
	
	public boolean addOffer(int playerId, ItemStack item, double price){
		for(Kategorie page : kategorieList){
			if(page.addOffer(playerId, item, price)){
				return true;
			}
		}
		return false;
	}
	
	public void initMarkt(){
		this.config=new kConfig(UtilFile.getYMLFile(UtilServer.getPluginInstance(), "auktions_markt.yml"));
		this.main=new InventoryPageBase(InventorySize._54, "Auktions Markt");
		InventorySplit._18.setLine(Material.STAINED_GLASS_PANE, ((byte)7), main);
		
//		main.addButton(new ButtonBase(new Click(){
//
//			@Override
//			public void onClick(Player player, ActionType type, Object object) {
//				InventoryPageBase own = new InventoryPageBase(InventorySize._54, "Deine Verkäufe:");
//				own.setItem(0, UtilItem.Item(new ItemStack(Material.BOOK), new String[]{"","§eHier werden dir alle, Items angezeigt,","§ewelche du aktuell verkauft."}, "§6Meine Verkäufe"));
//				own.addButton(1, new ButtonOpenInventory(getMain(), UtilItem.Item(new ItemStack(Material.SLIME_BALL), new String[]{"","§eKlicke, um zurück ins Menü zu gelangen"}, "§6Markt")));
//				
//				InventorySplit._18.setLine(Material.STAINED_GLASS_PANE, ((byte)7), own);
//				InventorySplit._54.setLine(Material.STAINED_GLASS_PANE, ((byte)14), own);
//				
//				int slot = InventorySplit._27.getMin();
//				for(Kategorie kategorie : getKategorieList()){
//					for(IButton button : kategorie.getButtons()){
//						if(button instanceof ItemKategorie){
//							for(Offer offer : ((ItemKategorie)button).getOffers()){
//								if(offer.getPlayerId()==UtilPlayer.getPlayerId(player)){
//									own.addButton(slot, new ButtonBase(new Click(){
//
//										@Override
//										public void onClick(Player player, ActionType type, Object object) {
//											offer.closeOffer();
//										}
//										
//									}, offer.getItemWithDescription()));
//									
//									slot++;
//									
//									if(slot > InventorySplit._45.getMax()){
//										//TODO next page
//										break;
//									}
//								}
//							}
//							
//							if(slot > InventorySplit._45.getMax()){
//								//TODO next page
//								break;
//							}
//						}
//					}
//					
//					if(slot > InventorySplit._45.getMax()){
//						//TODO next page
//						break;
//					}
//				}
//			}
//			
//		}, UtilItem.Item(new ItemStack(Material.BOOK), new String[]{"","§eHier werden dir alle, Items angezeigt,","§ewelche du aktuell verkauft."}, "§6Meine Verkäufe")));
		
		ConfigurationSection section = config.getConfigurationSection(PATH);
		for(String key : section.getKeys(false)){
			kategorieList.add(new Kategorie( UtilNumber.toInt(key) ));
		}
		
		for(int i = 0; i < 50; i++)
		addOffer(UtilServer.getClient().getPlayerAndLoad("kingingo").getPlayerId(), new ItemStack(Material.STONE), 10);
		
		UtilInv.getBase().addPage(main);
	}
}
