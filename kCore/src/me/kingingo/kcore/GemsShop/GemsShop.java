package me.kingingo.kcore.GemsShop;

import java.io.IOException;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Hologram.nametags.NameTagType;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryBuy;
import me.kingingo.kcore.Inventory.Inventory.InventoryCopy;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBack;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Listener.EntityClick.EntityClickListener;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilTime;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GemsShop{

	@Getter
	private Gems gems;
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
	
	public GemsShop(Hologram hm,CommandHandler cmd,InventoryBase base,PermissionManager permission,ServerType type){
		this.gems=new Gems(permission.getMysql());
		this.type=type;
		this.hm=hm;
		this.permission=permission;
		this.config=new kConfig(UtilFile.getYMLFile(permission.getInstance(), "gemsshop"));
		this.base=base;
		this.permission.getMysql().Update("CREATE TABLE IF NOT EXISTS gems_shop(player varchar(30),uuid varchar(60),ip varchar(60),gems int, article varchar(30),date varchar(30),time varchar(60),server varchar(30))");
		load();
		
		if(this.config.contains("Main.Location")){
			setCreature();
		}
		
		cmd.register(CommandGems.class, new CommandGems(this));
	}
	
	public void onDisable(){
		if(listener!=null)listener.getEntity().remove();
		if(m!=null)m.remove();
	}
	
	public void log(Player player,int gems,String article){
		getPermission().getMysql().Update("INSERT INTO gems_shop (player,uuid,ip,gems,article,date,time,server) VALUES ('"+player.getName()+"','"+me.kingingo.kcore.Util.UtilPlayer.getRealUUID(player)+"','"+player.getAddress().getAddress().getHostAddress()+"','"+gems+"','"+article+"','"+UtilTime.now()+"','"+System.currentTimeMillis()+"','"+type.name()+"')");
	}
	
	public void setCreature(){
		setCreature(this.config.getLocation("Main.Location"));
	}
	
	public void setCreature(Location location){
		this.config.setLocation("Main.Location", location);
		this.config.save();
		location.getChunk().load();
		Entity v = location.getWorld().spawnEntity(location, EntityType.CREEPER);
		UtilEnt.setNoAI(v, true);
		m = new NameTagMessage(NameTagType.SERVER, v.getLocation().add(0, 2.1, 0), "§a§lGem-Shop");
		m.send();
		
		listener=new EntityClickListener(getPermission().getInstance(), new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(base.getMain());
			}
			
		}, v);
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
			return "Category hinzugefügt";
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
	
	public String add(int cSlot, int slot,ItemStack item,int gems,String perm){
		if(kPermission.isPerm(perm)!=kPermission.NONE){
			if(getConfig().contains("Main."+cSlot)){
				if(!getConfig().contains("Main."+cSlot+"."+slot)){
					getConfig().setItemStack("Main."+cSlot+"."+slot+".Item",item);
					getConfig().set("Main."+cSlot+"."+slot+".Reward", perm);
					getConfig().set("Main."+cSlot+"."+slot+".Gems",gems);
					getConfig().save();
					return "Item hinzugefügt";
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
				return "Item hinzugefügt";
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
						
						if(kPermission.isPerm(s)!=kPermission.NONE){
							page.setCreate_new_inv(true);
						}
						
						page.addButton(a,new SalesPackageBase(new Click(){

							@Override
							public void onClick(Player player, ActionType type,Object object) {
								
								if(kPermission.isPerm(s)!=kPermission.NONE){
									if(player.hasPermission(kPermission.isPerm(s).getPermissionToString())){
										return;
									}
								}
								
								InventoryBuy buy = new InventoryBuy(new Click(){
									@Override
									public void onClick(Player player, ActionType type,Object object) {
										log(player, price, it.getItemMeta().getDisplayName());
										
										if(kPermission.isPerm(s)!=kPermission.NONE){
											getPermission().addPermission(player, kPermission.isPerm(s));
										}else{
											try {
												for(ItemStack it : UtilInv.itemStackArrayFromBase64(s))if(it!=null&&it.getType()!=Material.AIR)player.getInventory().addItem(it);
											} catch (IllegalArgumentException e) {
												e.printStackTrace();
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}
									
								},"Kaufen",getGems(),price);
								
								player.openInventory(buy);
								getBase().addAnother(buy);
							}
							
						},(kPermission.isPerm(s)!=kPermission.NONE?kPermission.isPerm(s):null), it));
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
