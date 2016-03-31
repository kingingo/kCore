package eu.epicpvp.kcore.GemsShop;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataserver.gamestats.ServerType;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.GemsShop.Events.PlayerGemsBuyEvent;
import eu.epicpvp.kcore.Hologram.Hologram;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryBuy;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBack;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Listener.EntityClick.EntityClickListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEnt;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class GemsShop{

	@Getter
	private StatsManager gems;
	@Getter
	private kConfig config;
	@Getter
	private InventoryBase base;
	@Getter
	private PermissionManager permission;
	private Hologram hm;
	private ServerType type;
	@Getter
	private EntityClickListener listener;
	private NameTagMessage m;
	
	public GemsShop(Hologram hm,StatsManager gems,CommandHandler cmd,InventoryBase base,PermissionManager permission,ServerType type){
		this.gems=gems;
		this.type=type;
		this.hm=hm;
		this.permission=permission;
		this.config=new kConfig(UtilFile.getYMLFile(permission.getInstance(), "gemsshop"));
		this.base=base;
		UtilServer.getMysql().Update("CREATE TABLE IF NOT EXISTS gems_shop(player varchar(30),uuid varchar(60),ip varchar(60),gems int, article varchar(30),date varchar(30),time varchar(60),server varchar(30))");
		load();
		
		if(this.config.contains("Main.Location")){
			setCreature();
		}
		
		cmd.register(CommandGems.class, new CommandGems(this));
		UtilServer.setGemsShop(this);
	}
	
	public void onDisable(){
		if(listener!=null)listener.getEntity().remove();
		if(m!=null)m.remove();
	}
	
	public void log(Player player,int gems,String article){
		UtilServer.getMysql().Update("INSERT INTO gems_shop (player,uuid,ip,gems,article,date,time,server) VALUES ('"+player.getName()+"','"+eu.epicpvp.kcore.Util.UtilPlayer.getRealUUID(player)+"','"+player.getAddress().getAddress().getHostAddress()+"','"+gems+"','"+article+"','"+UtilTime.now()+"','"+System.currentTimeMillis()+"','"+type.name()+"')");
	}
	
	public void setCreature(){
		setCreature(this.config.getLocation("Main.Location"));
	}
	
	public void setCreature(Location location){
		location.getChunk().load();
		this.config.setLocation("Main.Location", location);
		this.config.save();
		Entity v = location.getWorld().spawnEntity(location, EntityType.CREEPER);
		UtilEnt.setNoAI(v, true);
		
		if(m!=null){
			m.move(v.getLocation().add(0, 2.1, 0));
		}else{
			m = new NameTagMessage(NameTagType.SERVER, v.getLocation().add(0, 2.1, 0), "§a§lGem-Shop");
			m.send();
		}
		
		if(listener==null){
			listener=new EntityClickListener(getPermission().getInstance(), new Click(){
	
				@Override
				public void onClick(Player player, ActionType type, Object object) {
					player.openInventory(base.getMain());
				}
				
			}, v);
		}else{
			listener.getEntity().remove();
			listener.setEntity(v);
		}
			
	}
	
	public void fixInventory(InventoryPageBase page){
		page.setItem(4, UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§a§lGem-Shop"));
		page.setItem(49, UtilItem.Item(new ItemStack(Material.EXP_BOTTLE), new String[]{"","§7Shop.EpicPvP.de"}, "§aOnline-Shop: "));
		if(getBase().getMain()!=page)page.addButton(0, new ButtonBack(getBase().getMain(), UtilItem.RenameItem(new ItemStack(Material.ARROW), "§cBack")));
	}
	
	public void delCategory(int slot){
		getConfig().set("Main."+slot, null);
		getConfig().save();
	}
	
	public String addCategory(int slot,String pageName,ItemStack item){
		if(getBase().getMain().getItem(slot)==null||getBase().getMain().getItem(slot).getType()==Material.AIR){
			getConfig().setItemStack("Main."+slot+".Item", item);
			getConfig().set("Main."+slot+".PageName", pageName);
			getConfig().save();
			return "Category hinzugef§gt";
		}else{
			return "Der Slot ist besetzt!";
		}
	}
	
	public void del(int cSlot,int slot){
		if(getConfig().contains("Main."+cSlot)){
			getConfig().set("Main."+cSlot+"."+slot, null);
			getConfig().save();
		}
	}
	
	public String addcmd(int cSlot, int slot,ItemStack item,int gems,String cmd){
		if(getConfig().contains("Main."+cSlot)){
			if(!getConfig().contains("Main."+cSlot+"."+slot)){
				getConfig().setItemStack("Main."+cSlot+"."+slot+".Item",item);
				getConfig().set("Main."+cSlot+"."+slot+".Reward", "/"+cmd);
				getConfig().set("Main."+cSlot+"."+slot+".Gems",gems);
				getConfig().save();
				return "Item hinzugef§gt";
			}else{
				return "Der Slot ist besetzt!";
			}
		}else{
			return "Die Category exestiert nicht!";
		}
	}
	
	public String add(int cSlot, int slot,ItemStack item,int gems,String perm){
		if(PermissionType.isPerm(perm)!=PermissionType.NONE){
			if(getConfig().contains("Main."+cSlot)){
				if(!getConfig().contains("Main."+cSlot+"."+slot)){
					getConfig().setItemStack("Main."+cSlot+"."+slot+".Item",item);
					getConfig().set("Main."+cSlot+"."+slot+".Reward", perm);
					getConfig().set("Main."+cSlot+"."+slot+".Gems",gems);
					getConfig().save();
					return "Item hinzugef§gt";
				}else{
					return "Der Slot ist besetzt!";
				}
			}else{
				return "Die Category exestiert nicht!";
			}
		}else{
			return "Das ist keine Permission!";
		}
	}
	
	public String add(int cSlot, int slot,ItemStack item,int gems,ItemStack[] reward){
		if(getConfig().contains("Main."+cSlot)){
			if(!getConfig().contains("Main."+cSlot+"."+slot)){
				getConfig().setItemStack("Main."+cSlot+"."+slot+".Item",item);
				getConfig().setItemStackArray("Main."+cSlot+"."+slot+".Reward", reward);
				getConfig().set("Main."+cSlot+"."+slot+".Gems",gems);
				getConfig().save();
				return "Item hinzugef§gt";
			}else{
				return "Der Slot ist besetzt!";
			}
		}else{
			return "Die Category exestiert nicht!";
		}
	}
	
	public void load(){
		this.base.setMain(new InventoryPageBase(InventorySize._54, "Gems-Shop"));
		fixInventory(this.base.getMain());
		
		InventoryCopy page;
		for(int i = 9; i <= 45; i++){
			if(getConfig().contains("Main."+i)){
				page = new InventoryCopy(InventorySize._54.getSize(), "Gems-Shop/"+getConfig().getString("Main."+i+".PageName"));
				fixInventory(page);
				
				for(int a = 9; a <= 45; a++){
					if(getConfig().contains("Main."+i+"."+a)){
						int price = getConfig().getInt("Main."+i+"."+a+".Gems");
						String s = getConfig().getString("Main."+i+"."+a+".Reward");
						ItemStack it = getConfig().getItemStack("Main."+i+"."+a+".Item");
						
						if(PermissionType.isPerm(s)!=PermissionType.NONE){
							page.setCreate_new_inv(true);
						}
						
						page.addButton(a,new SalesPackageBase(new Click(){

							@Override
							public void onClick(Player player, ActionType type,Object object) {
								
								if(PermissionType.isPerm(s)!=PermissionType.NONE){
									if(player.hasPermission(PermissionType.isPerm(s).getPermissionToString())){
										return;
									}
								}
								
								InventoryBuy buy = new InventoryBuy(new Click(){
									@Override
									public void onClick(Player player, ActionType type,Object object) {
										log(player, price, it.getItemMeta().getDisplayName());

										if(s.startsWith("/")){
											if(s.contains(",")){
												for(String cmd : s.split(",")){
													System.out.println("[GemsShop]: use "+cmd.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
									  				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
												}
											}else{
												System.out.println("[GemsShop]: use "+s.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
								  				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
											}
										}else if(PermissionType.isPerm(s)!=PermissionType.NONE){
											getPermission().addPermission(player, PermissionType.isPerm(s));
										}else{
											try {
												for(ItemStack it : UtilInv.itemStackArrayFromBase64(s))if(it!=null&&it.getType()!=Material.AIR)player.getInventory().addItem(it);
											} catch (IllegalArgumentException e) {
												e.printStackTrace();
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
										
										Bukkit.getPluginManager().callEvent(new PlayerGemsBuyEvent(player, it, price));
									}
									
								},"Kaufen",getGems(),price,0);
								
								player.openInventory(buy);
								getBase().addAnother(buy);
							}
							
						},(PermissionType.isPerm(s)!=PermissionType.NONE?PermissionType.isPerm(s):null), it));
					}
				}
				
				ItemStack item = getConfig().getItemStack("Main."+i+".Item");
				
				if(item==null)item = UtilItem.RenameItem(new ItemStack(Material.BEDROCK), "§cERROR");
				
				this.base.getMain().addButton(i,new ButtonOpenInventoryCopy(page,getBase(), item));
				this.base.addPage(page);
			}
		}
	}
	
}
