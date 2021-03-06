package eu.epicpvp.kcore.GemsShop;

import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.ServerType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.GemsShop.Events.PlayerGemsBuyEvent;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryBuy;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBack;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesGroupPackageBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Listener.EntityClick.EntityClickListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilEnt;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.Setter;

public class GemsShop implements Listener{

	@Getter
	private StatsManager gems;
	@Getter
	private kConfig config;
	@Getter
	private InventoryBase base;
	@Getter
	@Setter
	private InventoryPageBase main;
	@Getter
	private PermissionManager permission;
	private ServerType type;
	@Getter
	@Setter
	private EntityType etype;
	@Getter
	private EntityClickListener listener;
	private NameTagMessage m;
	@Getter
	private String shopName;
	@Getter
	@Setter
	private Click click;
	private double sale;

	public GemsShop(ServerType type){
		this("§a§lGem-Shop",EntityType.CREEPER, type);
	}

	public GemsShop(String shopName,EntityType etype,ServerType type){
		this(shopName,etype,type,new kConfig(UtilFile.getYMLFile(UtilServer.getPermissionManager().getInstance(), "gemsshop")),null);
	}

	public GemsShop(String shopName,EntityType etype,ServerType type,kConfig config,Click click){
		this.gems=StatsManagerRepository.getStatsManager(GameType.Money);
		this.shopName=shopName;
		this.click=click;
		this.type=type;
		this.etype=etype;
		this.permission=UtilServer.getPermissionManager();
		this.config=config;
		this.sale= getConfig().getDouble("Main.Sale");
		this.base=UtilInv.getBase();
		UtilServer.getMysql().Update("CREATE TABLE IF NOT EXISTS `gems_shop`(playerId int,ip varchar(60),gems int,article varchar(30),timestamp timestamp,server varchar(30));");
		load();

		if(this.config.contains("Main.Location")){
			setCreature();

			if(sale!=0){
				Bukkit.getPluginManager().registerEvents(this, this.gems.getInstance());
			}
		}

		UtilServer.getCommandHandler().register(CommandGems.class, new CommandGems(this));
		UtilServer.getCommandHandler().register(CommandSM.class, new CommandSM());
		UtilServer.setGemsShop(this);
	}

	public void onDisable(){
		if(listener!=null)listener.getEntity().remove();
		if(m!=null)m.remove();
	}

	public void log(Player player,int gems,String article){
		UtilServer.getMysql().Update("INSERT INTO `gems_shop` (`playerId`,`ip`,`gems`,`article`,`server`) VALUES ('"+UtilPlayer.getPlayerId(player)+"','"+player.getAddress().getAddress().getHostAddress()+"','"+gems+"','"+article+"','"+type.name()+"')");
	}

	public void setCreature(){
		if(!this.config.contains("Main.Location"))return;
		setCreature(this.config.getLocation("Main.Location"));
	}

	public void setCreature(Location location){
		if(etype==null)return;
		location.getChunk().load();
		this.config.setLocation("Main.Location", location);
		this.config.save();
		Entity v = location.getWorld().spawnEntity(location, etype);
		UtilEnt.setNoAI(v, true);

		if(m!=null){
			m.move(v.getLocation().add(0, 2.1, 0));
		}else{
			m = new NameTagMessage(NameTagType.SERVER, v.getLocation().add(0, 2.1, 0), getShopName());
			m.send();
		}

		if(listener==null){
			listener=new EntityClickListener(getPermission().getInstance(), (click!=null ? click : new Click(){

				@Override
				public void onClick(Player player, ActionType type, Object object) {
					player.openInventory(main);
				}

			}), v);
		}else{
			listener.getEntity().remove();
			listener.setEntity(v);
		}

	}

	int color = 1;
	NameTagMessage salep;
	@EventHandler
	public void sale(UpdateEvent ev){
		if(ev.getType()==UpdateType.FAST && !UtilServer.getPlayers().isEmpty() && listener!=null && listener.getEntity()!=null){
			if(salep!=null){
				salep.remove();
			}

			salep=new NameTagMessage(NameTagType.PACKET, listener.getEntity().getLocation().add(0, 2.4, 0),(color==1 ? "§f" : "§c")+"§l"+sale+"% SALE!");

			for(Player player : UtilServer.getPlayers()){
				if(((UtilServer.getPermissionManager().getPermissionPlayer(player)!=null
						&& !UtilServer.getPermissionManager().getPermissionPlayer(player).getGroups().isEmpty()
						&& !UtilServer.getPermissionManager().getPermissionPlayer(player).getGroups().get(0).getName().equalsIgnoreCase("default"))
						||
						StatsManagerRepository.getStatsManager(GameType.TIME).isLoaded(player) && StatsManagerRepository.getStatsManager(GameType.TIME).getTotalInteger(player, new StatsKey[]{StatsKey.SKY_TIME,StatsKey.PVP_TIME,StatsKey.GUNGAME_TIME,StatsKey.GAME_TIME}) > TimeSpan.MINUTE * 30)){

					salep.sendToPlayer(player);
				}
			}

			if(color==1)color=0;
			else color=1;
		}
	}

	public void fixInventory(InventoryPageBase page){
		page.setItem(4, UtilItem.RenameItem(new ItemStack(Material.NETHER_STAR), getShopName()));
		page.setItem(49, UtilItem.Item(new ItemStack(Material.EXP_BOTTLE), new String[]{"","§7Shop.EpicPvP.eu"}, "§aOnline-Shop: "));
		if(this.main!=page)page.addButton(0, new ButtonBack(this.main, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§cBack")));
	}

	public void delCategory(int slot){
		getConfig().set("Main."+slot, null);
		getConfig().save();
	}

	public String addCategory(int slot,String pageName,ItemStack item){
		if(this.main.getItem(slot)==null||this.main.getItem(slot).getType()==Material.AIR){
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

	public String addcmd(int cSlot, int slot,ItemStack item,int gems,String cmd){
		if(getConfig().contains("Main."+cSlot)){
			if(!getConfig().contains("Main."+cSlot+"."+slot)){
				getConfig().setItemStack("Main."+cSlot+"."+slot+".Item",item);
				getConfig().set("Main."+cSlot+"."+slot+".Reward", "/"+cmd);
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

	public String add(int cSlot, int slot,ItemStack item,int gems,String perm){
		if(PermissionType.isPerm(perm)!=PermissionType.NONE){
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
				return "Item hinzugef§gt";
			}else{
				return "Der Slot ist besetzt!";
			}
		}else{
			return "Die Category exestiert nicht!";
		}
	}

	public void openInv(Player player){
		player.openInventory(this.main);
	}

	public void load(){
		this.main=new InventoryPageBase(InventorySize._54, shopName);
		base.addPage(this.main);
		fixInventory(this.main);
		double sale = getConfig().getDouble("Main.Sale");
		InventoryCopy page;
		for(int i = 9; i <= 45; i++){
			if(getConfig().contains("Main."+i)){
				page = new InventoryCopy(InventorySize._54.getSize(), shopName+"/"+getConfig().getString("Main."+i+".PageName"));
				fixInventory(page);

				for(int a = 9; a <= 45; a++){
					if(getConfig().contains("Main."+i+"."+a)){
						int price = getConfig().getInt("Main."+i+"."+a+".Gems");
						String s = getConfig().getString("Main."+i+"."+a+".Reward");
						ItemStack it = getConfig().getItemStack("Main."+i+"."+a+".Item");

						if(sale != 0 && it.hasItemMeta() && it.getItemMeta().hasLore()){
							List<String> lore = it.getItemMeta().getLore();
							String preisLine = lore.get(lore.size()-1);

							if(preisLine.contains("Preis")){
								ItemMeta im = it.getItemMeta();
								double preis = UtilNumber.toDouble(preisLine.split(" ")[1].replaceAll(",", "."));
								double preisWithSale = UtilMath.trim(2, preis - ( (sale/100)*preis ));
								lore.remove(preisLine);
								lore.add("§c§mPreis: "+preis+" Euro");
								lore.add("§bPreis: §7"+preisWithSale+" Euro");
								im.setLore(lore);
								it.setItemMeta(im);
							}
						}

						if(PermissionType.isPerm(s)!=PermissionType.NONE){
							page.setCreate_new_inv(true);
						}

						if(s.equalsIgnoreCase("/-")){
							page.setItem(a, it);
						}else{
							if(getConfig().isSet("Main."+i+".RankCheck") && getConfig().getBoolean("Main."+i+".RankCheck")){
								if(UtilServer.getPermissionManager().isBuyableRank(s)){
					  				page.setCreate_new_inv(true);

									page.addButton(a,new SalesGroupPackageBase(new Click(){

										@Override
										public void onClick(Player player, ActionType type,Object object) {

											if( UtilServer.getPermissionManager().checkHigherRank(player, s) ){
												return;
											}

											InventoryBuy buy = new InventoryBuy(new Click(){
												@Override
												public void onClick(Player player, ActionType type,Object object) {
													log(player, price, it.getItemMeta().getDisplayName());

													UtilServer.getPermissionManager().setGroup(player, s);

													Bukkit.getPluginManager().callEvent(new PlayerGemsBuyEvent(player, it, price));
												}

											},"Kaufen",getGems(), UtilServer.getPermissionManager().getUpdgradeGroupPrice(player, s),0);

											player.openInventory(buy);
											getBase().addAnother(buy);
										}

									},s, it));
								}else if(UtilServer.getPermissionManager().isBuyableTimeRank(s)){
									page.addButton(a,new SalesGroupPackageBase(new Click(){

										@Override
										public void onClick(Player player, ActionType type,Object object) {
											InventoryBuy buy = new InventoryBuy(new Click(){
												@Override
												public void onClick(Player player, ActionType type,Object object) {
													log(player, price, it.getItemMeta().getDisplayName());

													UtilServer.getPermissionManager().setGroup(player, s);

													Bukkit.getPluginManager().callEvent(new PlayerGemsBuyEvent(player, it, price));
												}

											},"Kaufen",getGems(), price,0);

											player.openInventory(buy);
											getBase().addAnother(buy);
										}

									},s, it));
								}
							}else{
								page.addButton(a,new SalesPackageBase(new Click(){

									@Override
									public void onClick(Player player, ActionType type,Object object) {

										if(PermissionType.isPerm(s)!=PermissionType.NONE){
											if(player.hasPermission(PermissionType.isPerm(s).getPermissionToString())){
												return;
											}
										}


										if(price != -1){
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
										}else{
											if(s.startsWith("/")){
												if(s.contains(",")){
													for(String cmd : s.split(",")){
														System.out.println("[GemsShop]: use "+cmd.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
										  				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
										  				player.closeInventory();
													}
												}else{
													System.out.println("[GemsShop]: use "+s.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
									  				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("/", "").replaceAll("_", " ").replaceAll("%p%", player.getName()));
									  				player.closeInventory();
												}
											}
										}
									}

								},(PermissionType.isPerm(s)!=PermissionType.NONE?PermissionType.isPerm(s):null), it));
							}
						}
					}
				}

				ItemStack item = getConfig().getItemStack("Main."+i+".Item");

				if(item==null)item = UtilItem.RenameItem(new ItemStack(Material.BEDROCK), "§cERROR");

				this.main.addButton(i,new ButtonOpenInventoryCopy(page,getBase(), item));
				this.base.addPage(page);
				getConfig().save();
			}
		}
	}

}
