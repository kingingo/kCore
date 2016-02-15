package me.kingingo.kcore.ItemShop;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
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
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Listener.EntityClick.EntityClickListener;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilNumber;
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
	private StatsManager statsManager;
	@Getter
	private EntityClickListener listener;
	private NameTagMessage m;
	
	public ItemShop(StatsManager statsManager,CommandHandler cmd){
		this.config=new kConfig(UtilFile.getYMLFile(cmd.getPlugin(), "itemshop"));
		UtilInv.getBase(cmd.getPlugin());
		this.statsManager=statsManager;
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
			m = new NameTagMessage(NameTagType.SERVER, v.getLocation().add(0, 2.1, 0), "§a§lItem-Shop");
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
		page.setItem(4, UtilItem.RenameItem(new ItemStack(Material.CHEST), "§a§lItem-Shop"));
		page.setItem(49, UtilItem.Item(new ItemStack(Material.EXP_BOTTLE), new String[]{"   §7Shop.EpicPvP.de   "}, "     §aOnline-Shop     "));
		if(shop!=page)page.addButton(0, new ButtonBack(shop, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)14), "§cZurück / Back")));
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
			return "Category hinzugefügt";
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
				return "Item hinzugefügt";
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
								UtilItem.SetDescriptions(it, new String[]{"§7» §cBuy","§7-","§e1 » §a"+bprice+" Epics","§e10 » §a"+(bprice*10)+" Epics","§e64 » §a"+(bprice*64)+" Epics","§7----------","§7» §cSell","§7-","§e1 » §a"+sprice+" Epics","§e10 » §a"+(sprice*10)+" Epics","§e64 » §a"+(sprice*64)+" Epics"});
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
		UtilInv.getBase().addPage(shop);
		fixInventory(shop);

		InventoryPageBase page_back = null;
		InventoryPageBase page = null;
		InventoryPageBase page_one = null;
		for(int i = 9; i <= 45; i++){
			if(getConfig().contains("Main."+i)){
				for(int p = 1; p <= 30; p++){
					if(getConfig().contains("Main."+i+"."+p)){
						page = new InventoryPageBase(InventorySize._54.getSize(), "Item-Shop/"+getConfig().getString("Main."+i+".PageName")+" "+p);
						fixInventory(page);
						
						for(int a = 9; a <= 45; a++){
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
							}
						}
						
						if(page_back!=null){
							page.addButton(48, new ButtonOpenInventory(page_back, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§c« "+(p-1))));
							page_back.addButton(50, new ButtonOpenInventory(page, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§a"+(p)+" »")));
						}
						
						page_back=page;
						if(p==1)page_one=page;
						UtilInv.getBase().addPage(page);
					}else{
						break;
					}
				}
				
				if(page_back!=null){
					page_back.delButton(50);
				}
				
				ItemStack item = getConfig().getItemStack("Main."+i+".Item");
				
				if(item==null)item = UtilItem.RenameItem(new ItemStack(Material.BEDROCK), "§cERROR");
				
				shop.addButton(i,new ButtonOpenInventory(page_one, item));
			}
			 page_back = null;
			 page = null;
			 page_one = null;
		}
	}
	
}
