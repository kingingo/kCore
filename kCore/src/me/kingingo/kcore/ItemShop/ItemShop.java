package me.kingingo.kcore.ItemShop;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.karcade.Game.Single.SingleGame;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Hologram.nametags.NameTagType;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryBuy;
import me.kingingo.kcore.Inventory.Inventory.InventoryCopy;
import me.kingingo.kcore.Inventory.Inventory.InventoryShopBuy;
import me.kingingo.kcore.Inventory.Inventory.InventoryShopSell;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBack;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonItemShopMove;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.EntityClick.EntityClickListener;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.AnvilGUI;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilTime;
import me.kingingo.kcore.kConfig.kConfig;
import net.minecraft.server.v1_8_R3.Item;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class ItemShop{

	@Getter
	private kConfig config;
	@Getter
	@Setter
	private InventoryPageBase shop;
	@Getter
	@Setter
	private InventoryPageBase move;
	@Getter
	@Setter
	private InventoryPageBase edit;
	@Getter
	private StatsManager statsManager;
	@Getter
	private CommandHandler cmd;
	@Getter
	private EntityClickListener listener;
	private NameTagMessage m;
	
	public ItemShop(StatsManager statsManager,CommandHandler cmd){
		this.config=new kConfig(UtilFile.getYMLFile(cmd.getPlugin(), "itemshop"));
		UtilInv.getBase(cmd.getPlugin());
		this.statsManager=statsManager;
		this.cmd=cmd;
		load();
		
		if(this.config.contains("Main.Location")){
			setCreature();
		}
		
		cmd.register(CommandItemShop.class, new CommandItemShop(this));
	}
	
	public void onDisable(){
		if(listener!=null)listener.getEntity().remove();
		if(m!=null)m.remove();
	}
	
	public void setCreature(){
		setCreature(this.config.getLocation("Main.Location"));
	}
	
	public void setCreature(Location location){
		location.getChunk().load();
		this.config.setLocation("Main.Location", location);
		this.config.save();
		Entity v = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		UtilEnt.setNoAI(v, true);
		
		if(m!=null){
			m.move(v.getLocation().add(0, 2.1, 0));
		}else{
			m = new NameTagMessage(NameTagType.SERVER, v.getLocation().add(0, 2.1, 0), "�a�lItem-Shop");
			m.send();
		}
		
		if(listener==null){
			listener=new EntityClickListener(UtilServer.getCommandHandler().getPlugin(), new Click(){
	
				@Override
				public void onClick(Player player, ActionType type, Object object) {
					player.openInventory(shop);
				}
				
			}, v);
		}else{
			listener.getEntity().remove();
			listener.setEntity(v);
		}
			
	}
	
	public void fixInventory(InventoryPageBase page){
		page.setItem(4, UtilItem.RenameItem(new ItemStack(Material.CHEST), "�a�lItem-Shop"));
		page.setItem(49, UtilItem.Item(new ItemStack(Material.EXP_BOTTLE), new String[]{"   �7Shop.EpicPvP.de   "}, "     �aOnline-Shop     "));
	}
	
	public void delCategory(int slot){
		getConfig().set("Main."+slot, null);
		getConfig().save();
	}
	
	public String addCategory(int slot,String pageName,ItemStack item){
		pageName=pageName.replaceAll("_", " ");
		if(shop.getItem(slot)==null||shop.getItem(slot).getType()==Material.AIR){
			getConfig().setItemStack("Main."+slot+".Item", item);
			getConfig().set("Main."+slot+".PageName", pageName);
			getConfig().save();
			return "Category hinzugef�gt";
		}else{
			return "Der Slot ist besetzt!";
		}
	}
	
	public void del(int cSlot,int slot,int page){
		if(getConfig().contains("Main."+cSlot)){
			if(getConfig().contains("Main."+cSlot+"."+page)){
				getConfig().set("Main."+cSlot+"."+page+"."+slot, null);
				getConfig().save();
			}
		}
	}
	
	public String add(int cSlot, int slot,ItemStack item,int buy,int sell, int page){
		if(getConfig().contains("Main."+cSlot)){
			if(slot==-1){
				for(int i = 9; i <= 45; i++){
					if(!getConfig().contains("Main."+cSlot+"."+page+"."+i)){
						slot=i;
						break;
					}
				}
				
				if(slot==-1){
					
					return "Die Page ist voll!";
				}
			}
			
			if(!getConfig().contains("Main."+cSlot+"."+page+"."+slot)){
				getConfig().setItemStack("Main."+cSlot+"."+page+"."+slot+".Item",item);
				getConfig().set("Main."+cSlot+"."+page+"."+slot+".buy",buy);
				getConfig().set("Main."+cSlot+"."+page+"."+slot+".sell",sell);
				getConfig().save();
				return "Item hinzugef�gt";
			}else{
				return "Der Slot ist besetzt!";
			}
		}else{
			return "Die Category exestiert nicht!";
		}
	}
	
	public void fix(){
		for(int i = 9; i <= 45; i++){
			if(getConfig().contains("Main."+i)){
				for(int p = 1; p <= 30; p++){
					if(getConfig().contains("Main."+i+"."+p)){
						for(int a = 9; a <= 45; a++){
							if(getConfig().contains("Main."+i+"."+p+"."+a)){
								int bprice = getConfig().getInt("Main."+i+"."+p+"."+a+".buy");
								int sprice = getConfig().getInt("Main."+i+"."+p+"."+a+".sell");
								ItemStack it = getConfig().getItemStack("Main."+i+"."+p+"."+a+".Item");
								UtilItem.SetDescriptions(it, new String[]{"�7� �cBuy","�7-","�e1 � �a"+bprice+" Epics","�e10 � �a"+(bprice*10)+" Epics","�e64 � �a"+(bprice*64)+" Epics","�7----------","�7� �cSell","�7-","�e1 � �a"+sprice+" Epics","�e10 � �a"+(sprice*10)+" Epics","�e64 � �a"+(sprice*64)+" Epics"});
								getConfig().setItemStack("Main."+i+"."+p+"."+a+".Item", it);
							}
						}
					}else{
						break;
					}
				}
			}
		}
		getConfig().save();
	}
	
	public void load(){
		shop=new InventoryPageBase(InventorySize._54, "Item-Shop");
		move=new InventoryPageBase(InventorySize._54, "Item-Shop �c[Move]");
		edit=new InventoryPageBase(InventorySize._54, "Item-Shop �c[Edit]");
		UtilInv.getBase().addPage(shop);
		fixInventory(shop);
		UtilInv.getBase().addPage(move);
		fixInventory(move);
		UtilInv.getBase().addPage(edit);
		fixInventory(edit);

		InventoryPageBase page_back_edit = null;
		InventoryPageBase page_edit = null;
		InventoryPageBase page_one_edit = null;

		InventoryPageBase page_back_move = null;
		InventoryPageBase page_move = null;
		InventoryPageBase page_one_move = null;
		
		InventoryPageBase page_back = null;
		InventoryPageBase page = null;
		InventoryPageBase page_one = null;
		for(int i = 9; i <= 45; i++){
			if(getConfig().contains("Main."+i)){
				for(int p = 1; p <= 30; p++){
					if(getConfig().contains("Main."+i+"."+p)){
						page = new InventoryPageBase(InventorySize._54.getSize(), "Item-Shop/"+getConfig().getString("Main."+i+".PageName")+" "+p);
						page.addButton(0, new ButtonBack(shop, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)14), "�cZur�ck / Back")));
						page_move = new InventoryPageBase(InventorySize._54.getSize(), "Item-Shop/"+getConfig().getString("Main."+i+".PageName")+" "+p);
						page_move.addButton(0, new ButtonBack(move, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)14), "�cZur�ck / Back")));
						page_edit = new InventoryPageBase(InventorySize._54.getSize(), "Item-Shop/"+getConfig().getString("Main."+i+".PageName")+" "+p);
						page_edit.addButton(0, new ButtonBack(edit, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)14), "�cZur�ck / Back")));
						fixInventory(page);
						fixInventory(page_move);
						fixInventory(page_edit);
						
						for(int a = 9; a <= 44; a++){
							if(getConfig().contains("Main."+i+"."+p+"."+a)){
								int bprice = getConfig().getInt("Main."+i+"."+p+"."+a+".buy");
								int sprice = getConfig().getInt("Main."+i+"."+p+"."+a+".sell");
								ItemStack it = getConfig().getItemStack("Main."+i+"."+p+"."+a+".Item");
								
								page.addButton(a,new SalesPackageBase(new Click(){

									@Override
									public void onClick(Player player, ActionType type,Object object) {
										if(type==ActionType.L){
											InventoryShopBuy buy = new InventoryShopBuy(shop,player, it, statsManager, bprice);
											UtilInv.getBase().addAnother(buy);
											player.openInventory(buy);
										}else{
											InventoryShopSell sell = new InventoryShopSell(shop,player, it, statsManager, sprice);
											UtilInv.getBase().addAnother(sell);
											player.openInventory(sell);
										}
									}
									
								},null, it));
								
								page_move.addButton(a, new ButtonItemShopMove(getConfig(), i, p, a, page_move, it));
								
								int ii = i;
								int aa = a;
								int pp = p;
								InventoryPageBase bbpp=page_edit;
								page_edit.addButton(a,new ButtonBase(new Click(){

									@Override
									public void onClick(Player player,ActionType type, Object object) {
										InventoryPageBase editinv = new InventoryPageBase(InventorySize._54, "�cEdit");
										editinv.setItem(22, it);
										editinv.addButton(0, new ButtonBack(bbpp, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)14), "�cZur�ck / Back")));
										editinv.addButton(33, new ButtonBase(new Click(){

											@Override
											public void onClick(Player player,
													ActionType type,
													Object object) {
												getConfig().set("Main."+ii+"."+pp+"."+aa, null);
												getConfig().save();
												bbpp.setItem(aa, null);
												player.openInventory(bbpp);
											}
											
										}, UtilItem.RenameItem(new ItemStack(Material.BARRIER), "�cItem l�schen")));
										
										editinv.addButton(31, new ButtonBase(new Click(){

											@Override
											public void onClick(Player player,
													ActionType type,
													Object object) {
												AnvilGUI gui = new AnvilGUI(player,getCmd().getPlugin(), new AnvilGUI.AnvilClickEventHandler(){

													@Override
													public void onAnvilClick(AnvilClickEvent ev) {
														if(ev.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
													        try{
													        	Integer i = Integer.valueOf(ev.getName());
																getConfig().set("Main."+ii+"."+pp+"."+aa+".sell", i);
																int bprice = getConfig().getInt("Main."+ii+"."+pp+"."+aa+".buy");
																ItemStack it = getConfig().getItemStack("Main."+ii+"."+pp+"."+aa+".Item");
																UtilItem.SetDescriptions(it, new String[]{"�7� �cBuy","�7-","�e1 � �a"+bprice+" Epics","�e10 � �a"+(bprice*10)+" Epics","�e64 � �a"+(bprice*64)+" Epics","�7----------","�7� �cSell","�7-","�e1 � �a"+i+" Epics","�e10 � �a"+(i*10)+" Epics","�e64 � �a"+(i*64)+" Epics"});
																getConfig().setItemStack("Main."+ii+"."+pp+"."+aa+".Item", it);
																getConfig().save();
													        }catch(NumberFormatException e){
													        	e.printStackTrace();
													        }
															ev.setWillClose(false);
															player.openInventory(bbpp);
														}
													}
													
									    		});
									    		
												ItemStack renamed = UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "Zahl");
												gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, renamed);
												gui.setSlot(AnvilGUI.AnvilSlot.OUTPUT, UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "�aFertig"));
												gui.open();
											}
											
										}, UtilItem.RenameItem(new ItemStack(Material.SIGN), "�cVerkaufpreis")));
										
										editinv.addButton(29, new ButtonBase(new Click(){

											@Override
											public void onClick(Player player,
													ActionType type,
													Object object) {
												AnvilGUI gui = new AnvilGUI(player,getCmd().getPlugin(), new AnvilGUI.AnvilClickEventHandler(){

													@Override
													public void onAnvilClick(AnvilClickEvent ev) {
														if(ev.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
															try{
													        	Integer i = Integer.valueOf(ev.getName());
																getConfig().set("Main."+ii+"."+pp+"."+aa+".buy", i);
																int sprice = getConfig().getInt("Main."+ii+"."+pp+"."+aa+".sell");
																ItemStack it = getConfig().getItemStack("Main."+ii+"."+pp+"."+aa+".Item");
																UtilItem.SetDescriptions(it, new String[]{"�7� �cBuy","�7-","�e1 � �a"+i+" Epics","�e10 � �a"+(i*10)+" Epics","�e64 � �a"+(i*64)+" Epics","�7----------","�7� �cSell","�7-","�e1 � �a"+sprice+" Epics","�e10 � �a"+(sprice*10)+" Epics","�e64 � �a"+(sprice*64)+" Epics"});
																getConfig().setItemStack("Main."+ii+"."+pp+"."+aa+".Item", it);
																getConfig().save();
													        }catch(NumberFormatException e){
													        	e.printStackTrace();
													        }
															ev.setWillClose(false);
															player.openInventory(bbpp);
														}
													}
													
									    		});
									    		
												ItemStack renamed = UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "Zahl");
												gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, renamed);
												gui.setSlot(AnvilGUI.AnvilSlot.OUTPUT, UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "�aFertig"));
												gui.open();
											}
											
										}, UtilItem.RenameItem(new ItemStack(Material.SIGN), "�cKaufpreis")));

										editinv.fill(Material.STAINED_GLASS_PANE, 15);
										UtilInv.getBase().addAnother(editinv);
										player.openInventory(editinv);
									}
									
								}, it));
							}else{
								page_move.addButton(a, new ButtonItemShopMove(getConfig(), i, p, a, page_move, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)8), "LEER")));
							}
						}
						
						if(page_back!=null){
							page.addButton(48, new ButtonOpenInventory(page_back, UtilItem.RenameItem(new ItemStack(Material.ARROW), "�c� "+(p-1))));
							page_back.addButton(50, new ButtonOpenInventory(page, UtilItem.RenameItem(new ItemStack(Material.ARROW), "�a"+(p)+" �")));
						}
						
						if(page_back_move!=null){
							page_move.addButton(48, new ButtonOpenInventory(page_back_move, UtilItem.RenameItem(new ItemStack(Material.ARROW), "�c� "+(p-1))));
							page_back_move.addButton(50, new ButtonOpenInventory(page_move, UtilItem.RenameItem(new ItemStack(Material.ARROW), "�a"+(p)+" �")));
						}
						
						if(page_back_edit!=null){
							page_edit.addButton(48, new ButtonOpenInventory(page_back_edit, UtilItem.RenameItem(new ItemStack(Material.ARROW), "�c� "+(p-1))));
							page_back_edit.addButton(50, new ButtonOpenInventory(page_edit, UtilItem.RenameItem(new ItemStack(Material.ARROW), "�a"+(p)+" �")));
						}

						page_back=page;
						if(p==1)page_one=page;
						UtilInv.getBase().addPage(page);
						
						page_back_move=page_move;
						if(p==1)page_one_move=page_move;
						UtilInv.getBase().addPage(page_move);
						
						page_back_edit=page_edit;
						if(p==1)page_one_edit=page_edit;
						UtilInv.getBase().addPage(page_edit);
					}else{
						break;
					}
				}
				
				if(page_back_move!=null){
					page_back_move.delButton(50);
				}
				
				if(page_back_edit!=null){
					page_back_edit.delButton(50);
				}
				
				if(page_back!=null){
					page_back.delButton(50);
				}
				
				ItemStack item = getConfig().getItemStack("Main."+i+".Item");
				
				if(item==null)item = UtilItem.RenameItem(new ItemStack(Material.BEDROCK), "�cERROR");

				move.addButton(i,new ButtonOpenInventory(page_one_move, item));
				edit.addButton(i,new ButtonOpenInventory(page_one_edit, item));
				shop.addButton(i,new ButtonOpenInventory(page_one, item));
			}
			 page_back = null;
			 page = null;
			 page_one = null;

			 page_back_move = null;
			 page_move = null;
			 page_one_move = null;
			 

			 page_back_edit = null;
			 page_edit = null;
			 page_one_edit = null;
		}
	}
	
}
