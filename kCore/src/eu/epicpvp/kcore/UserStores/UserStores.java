package eu.epicpvp.kcore.UserStores;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryNextPage;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Scheduler.kScheduler;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigConvertEvent;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigLoadEvent;
import eu.epicpvp.kcore.UserStores.Events.PlayerCreateUserStoreEvent;
import eu.epicpvp.kcore.Util.AnvilGUI;
import eu.epicpvp.kcore.Util.AnvilGUI.AnvilClickEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilBlock;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilLocation;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.TileEntityHopper;

public class UserStores extends kListener{
	@Getter
	private HashMap<Player,Long> timer = new HashMap<Player,Long>();
	@Getter
	private HashMap<Player,Sign> open_chest = new HashMap<Player,Sign>();
	@Getter
	private HashMap<Player,Sign> change_price = new HashMap<Player,Sign>();
	@Getter
	private StatsManager statsManager;
	private InventoryCopy page;
	private String prefix = " §b[UserStore]  ";
	
	public UserStores(StatsManager statsManager){
		super(statsManager.getInstance(),"UserStores");
		this.statsManager=statsManager;
		
		page = new InventoryCopy(InventorySize._54.getSize(), "Stores");
		
		page.addButton(4, new ButtonCopy(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				kConfig c = UtilServer.getUserData().getConfig(player);
				((InventoryPageBase)object).setItem(4, UtilItem.SetDescriptions(((InventoryPageBase)object).getItem(4), new String[]{"§e"+c.getPathList("UserStores").size()+" Stores"}));

				ItemStack free = UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "§aFreier Platz");
				int slot=18;
				InventoryPageBase cp = ((InventoryPageBase)object);
				for(String loc : c.getPathList("UserStores").keySet()){
					cp.addButton(slot, new ButtonBase(new Click(){

						@Override
						public void onClick(Player player, ActionType type,Object o) {
							InventoryPageBase page = new InventoryPageBase(InventorySize._54, "Shop");
							page.setItem(4, ((ItemStack)o));
							
							page.addButton(0, new ButtonOpenInventory(((InventoryPageBase)object), UtilItem.RenameItem(new ItemStack(Material.BARRIER), "§cZurück")));
							
							page.addButton(33, new ButtonBase(new Click(){

								@Override
								public void onClick(Player player,
										ActionType type, Object object) {
									Location l = getStringLoc(loc);
									
									if(l.getBlock().getState() instanceof Sign){
										Sign s = (Sign)l.getBlock().getState();
										BlockFace f = UtilBlock.getSignFace(s);
										Location ll = l.getBlock().getRelative(f).getRelative(f).getLocation();
										ll=UtilLocation.setBlockFaceDirection(ll, UtilBlock.getSignAttachedFace(s));
										player.teleport(ll);
										player.closeInventory();
									}
								}
								
							}, UtilItem.RenameItem(new ItemStack(Material.ENDER_PEARL), "§eTeleport")));
							
							page.addButton(31, new ButtonBase(new Click(){

								@Override
								public void onClick(Player player,
										ActionType type, Object object) {
									Location l = getStringLoc(loc);
									l.getBlock().setType(Material.AIR);
									c.set("UserStores."+loc, null);
									c.save();
									player.closeInventory();
								}
								
							}, UtilItem.RenameItem(new ItemStack(Material.BARRIER), "§cStore löschen")));
							
							page.addButton(29, new ButtonBase(new Click(){

								@Override
								public void onClick(Player player,
										ActionType type, Object object) {
									Location l = getStringLoc(loc);
									if(l.getBlock().getState() instanceof Sign){
										Sign s = (Sign)l.getBlock().getState();
										
										AnvilGUI gui = new AnvilGUI(player,getStatsManager().getInstance(), new AnvilGUI.AnvilClickEventHandler(){

											@Override
											public void onAnvilClick(AnvilClickEvent ev) {
												if(ev.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
											        double i = UtilNumber.toDouble(ev.getName());
											        
											        if(i>0){
											        	changePrice(player, i, s, loc);
														player.closeInventory();
											        }
												}
											}
											
							    		});
							    		
										ItemStack renamed = UtilItem.RenameItem(new ItemStack(Material.SIGN), "Zahl");
										gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, renamed);
										gui.setSlot(AnvilGUI.AnvilSlot.OUTPUT, UtilItem.RenameItem(new ItemStack(Material.SIGN), "§aFertig"));
										gui.open(); 
									}
								}
								
							}, UtilItem.RenameItem(new ItemStack(Material.SIGN), "§aPreis ändern")));
							
							
							page.fill(Material.STAINED_GLASS_PANE,15);
							UtilInv.getBase().addAnother(page);
							player.openInventory(page);
						}
						
					}, Material.getMaterial(c.getInt("UserStores."+loc+".id")), Byte.valueOf(c.getString("UserStores."+loc+".data")), "§aStore", new String[]{"§aPreis §7"+Zeichen.DOUBLE_ARROWS_R.getIcon()+"§e "+c.getString("UserStores."+loc+".preis"),"§aAnzahl §7"+Zeichen.DOUBLE_ARROWS_R.getIcon()+"§e "+c.getString("UserStores."+loc+".anzahl")}));
					slot++;
					if(slot==52){
						for(int i = slot; i<=51; i++)cp.setItem(i, free);
						InventoryPageBase newpage =new InventoryNextPage(player,InventorySize._54, "Stores");
						cp.addButton(53, new ButtonOpenInventory(newpage, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§a1 "+Zeichen.DOUBLE_ARROWS_R.getIcon())));
						newpage.addButton(52, new ButtonOpenInventory(cp, UtilItem.RenameItem(new ItemStack(Material.ARROW), "§c"+Zeichen.DOUBLE_ARROWS_l.getIcon()+" 1")));
						UtilInv.getBase().addAnother(newpage);
						newpage.fill(Material.STAINED_GLASS_PANE,15);
						cp=newpage;
						slot=0;
					}
				}
				
				if(slot<51){
					for(int i = slot; i<=51; i++){
						cp.setItem(i, free);
					}
				}
			}
		}, null, UtilItem.RenameItem(new ItemStack(Material.CHEST), "§a Deine aktuellen Stores")));
		page.fill(Material.STAINED_GLASS_PANE,15);
		page.setCreate_new_inv(true);
		page.setFor_with_copy_page(false);
		UtilInv.getBase(statsManager.getInstance()).addPage(page);
		UtilServer.getCommandHandler().register(CommandUserStore.class, new CommandUserStore(this));
	}
	
	@EventHandler
	public void convert(UserDataConfigConvertEvent ev){
		if(ev.getConfig().contains("UserStores")){
			Bukkit.getScheduler().runTask(getStatsManager().getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Location l;
					Sign s;
					for(String loc : ev.getConfig().getPathList("UserStores").keySet()){
						l=getStringLoc(loc);
						
						l.getChunk().load();
						
						if(l.getBlock().getState() instanceof Sign){
							if(l.getBlock().getRelative(BlockFace.DOWN).getState() instanceof Chest){
								s = (Sign)l.getBlock().getState();
								s.setLine(0,prefix+"/"+ev.getPlayerId());
								s.update(true);
								
								ev.getConfig().set("UserStores."+loc+".anzahl", getAnzahl(s));
								logMessage("Shop from "+ev.getPlayerId()+" changed. "+UtilLocation.getLocString(s.getLocation()));
								continue;
							}
						}
						
						l.getBlock().setType(Material.AIR);
						l.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
						l.getChunk().unload();
					}
				}
			});
		}
	}
	
	public void openInv(Player player){
		if(UtilServer.getUserData().getConfig(player).contains("UserStores")){
			((InventoryCopy)page).open(player, UtilInv.getBase());
		}else{
			logMessage("I cannot find any UserStores!");
		}
	}
	
	public void loadPlayerStores(Player player){
		kConfig config = UtilServer.getUserData().getConfig(player);
		Location l;
		Sign s;
		for(String loc : config.getPathList("UserStores").keySet()){
			l=getStringLoc(loc);
			
			l.getChunk().load();
			
			if(l.getBlock().getState() instanceof Sign){
				if(l.getBlock().getRelative(BlockFace.DOWN).getState() instanceof Chest){
					s = (Sign)l.getBlock().getState();
					
					config.set("UserStores."+loc+".anzahl", getAnzahl(s));
					continue;
				}
			}
			
			l.getBlock().setType(Material.AIR);
			l.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
			l.getChunk().unload();
		}
	}
	
	public Location getStringLoc(String s)
	  {
	    if (s == null) return null;
	    String[] split = s.split(",");
	    return new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
	  }

	public String getLocString(Location l)
	  {
	    if (l == null) return "null";
	    StringBuilder sb = new StringBuilder();
	    sb.append(l.getWorld().getName()).append(",");
	    sb.append(l.getBlockX()).append(",");
	    sb.append(l.getBlockY()).append(",");
	    sb.append(l.getBlockZ());
	    return sb.toString();
	  }
	
	Player p;
	Action a;
	Sign sign;
	Chest chest;
	double preis;
	int anzahl;
	ItemStack item;
	@EventHandler(priority=EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.BLOCK)){
			if(ev.getClickedBlock().getState() instanceof Sign){
				sign = (Sign)ev.getClickedBlock().getState();
				
				if(sign.getLine(0).startsWith(prefix)){
					if(sign.getBlock().getRelative(BlockFace.DOWN).getState() instanceof Chest){
						chest = (Chest)ev.getClickedBlock().getRelative(BlockFace.DOWN).getState();
						p=ev.getPlayer();
						a=ev.getAction();
						
						if(Action.RIGHT_CLICK_BLOCK == a && Action.LEFT_CLICK_BLOCK == a){
							ev.setCancelled(true);
							p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§cNicht so Schnell ...");
							return;
						}else if(Action.RIGHT_CLICK_BLOCK == a){
							if(this.timer.containsKey(p)){
		                		if(this.timer.get(p) <= System.currentTimeMillis()){
		                			this.timer.remove(p);
		                		}else{
		                			p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "SIGN_SHOP_DELAY",3));
		                			return;
		                		}
		                	}
							
							preis = UtilNumber.toDouble(getPreis(sign));
							anzahl = 1;
							
							if(p.isSneaking()){
								anzahl=32;
							}
							
							if( (preis*anzahl) <= getStatsManager().getDouble(StatsKey.MONEY, p)){
								if(Integer.valueOf(getAnzahl(sign))>0){
									if(isUUID(sign)){
										p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"Dieser Shop wurde noch nicht convertiert!");
										return;
									}
									int playerId = getPlayerId(sign);
									if(playerId != UtilPlayer.getPlayerId(p)){
										if(ev.getPlayer().getItemInHand() == null || ev.getPlayer().getItemInHand().getType()==Material.AIR){
											item=UtilInv.getFirstItem(chest.getInventory());
											if(Integer.valueOf(getAnzahl(sign)) >= (anzahl)){
												logMessage("Der Spieler "+p.getName()+" hat "+anzahl+" mal "+item.getTypeId()+" gekauft und "+(preis*anzahl)+" Epics bezahlt. "+UtilLocation.getLocString(sign.getLocation())+" "+sign.getLocation().getWorld());
												getStatsManager().addDouble(p, -(preis*anzahl), StatsKey.MONEY);
												
						            			if( UtilPlayer.isOnline(playerId) ){
													getStatsManager().addDouble(UtilPlayer.searchExact(playerId), (preis*anzahl), StatsKey.MONEY);
												}else{
													getStatsManager().loadPlayer(playerId, new Callback<Integer>() {
														
														@Override
														public void call(Integer obj) {
															getStatsManager().add(obj, StatsKey.MONEY, (preis*anzahl));
														}
													});
												}
												
												setAnzahl(sign, (Integer.valueOf(getAnzahl(sign))-anzahl));
												
												ArrayList<ItemStack> items = UtilInv.getItems(chest.getInventory(), anzahl);
												
												for(ItemStack item : items)p.getInventory().addItem(item);
												items.clear();
												p.updateInventory();
												ev.setCancelled(true);
					        					p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "SIGN_SHOP_GET",new String[]{String.valueOf(anzahl), String.valueOf(item.getTypeId()),String.valueOf((preis*anzahl))}));
					        				}else{
												p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "SHOP_AMOUNT_NOT_ENOUGH"));
											}
										}else{
											p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "SHOP_HAND"));
										}
									}else{
										p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "SHOP_SELF"));
									}
								}else{
									p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "SHOP_EMPTY"));
								}
							}else{
								p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "NOT_ENOUGH_MONEY"));
							}
						}
					}else{
						ev.getClickedBlock().setType(Material.AIR);
					}
				}
			}
		}
	}
	
	public void changePrice(Player player,double preis,Sign s,String loc){
		if(change_price.containsKey(player))return;
		change_price.put(player, s);
		NameTagMessage msg = new NameTagMessage(NameTagType.PACKET, s.getLocation().add(0.5, 0, 0.5), new String[]{"§cPreisänderung in","§l 16 sekunden"});
		new kScheduler(getStatsManager().getInstance(), new kScheduler.kSchedulerHandler() {
			
			@Override
			public void onRun(kScheduler scheduler) {

				int sec = (UtilNumber.toInt( msg.getLines()[1].split(" ")[1] )-1);
				msg.getLines()[1]="§l "+sec+" sekunden";
				
				if(sec == -1){
					kConfig c = UtilServer.getUserData().getConfig(player);
					setPreis(s, preis);
					c.set("UserStores."+loc+".preis", preis);
					c.save();
					change_price.remove(player);
					msg.clear();
					scheduler.close();
				}else{
					for(Player player : UtilPlayer.getInRadius(s.getLocation(), 20).keySet()){
						msg.clear(player);
						msg.sendToPlayer(player);
					}
				}
			}
		}, UpdateType.SEC);
	}
	
	Location invmoveitem_chest;
	Sign invmove_sign;
	IInventory i;
	TileEntityHopper hopper;
	BlockPosition srcChest;
	@EventHandler
    public void a(InventoryMoveItemEvent e){
		
        if(((CraftInventory)e.getSource()).getInventory() != null){
            i = ((CraftInventory)e.getSource()).getInventory();
            if(i instanceof TileEntityChest){
            	srcChest = ((TileEntityChest) i).getPosition();
                
                if(invmoveitem_chest==null){
                	invmoveitem_chest=new Location(((TileEntityChest) i).getWorld().getWorld(), srcChest.getX(), srcChest.getY(), srcChest.getZ());
                }else{
                	invmoveitem_chest.setWorld(((TileEntityChest) i).getWorld().getWorld());
                	invmoveitem_chest.setX(srcChest.getX());
                	invmoveitem_chest.setY(srcChest.getY());
                	invmoveitem_chest.setZ(srcChest.getZ());
                }
                
                if(invmoveitem_chest.getBlock().getRelative(BlockFace.UP).getState() instanceof Sign){
                	invmove_sign = (Sign)invmoveitem_chest.getBlock().getRelative(BlockFace.UP).getState();
                	
                	if(invmove_sign.getLine(0).startsWith(prefix) ){
                		if(e.getDestination().getType()==InventoryType.HOPPER){
               			 i = ((CraftInventory)e.getDestination()).getInventory();
               			 if(i instanceof TileEntityHopper){
               				e.setCancelled(true);
               			 }
               		}
                	}
                }
            }
        } 
        
        if(((CraftInventory)e.getDestination()).getInventory() != null){
            i = ((CraftInventory)e.getDestination()).getInventory();
            if(i instanceof TileEntityChest){
                srcChest = ((TileEntityChest) i).getPosition();
                
                if(invmoveitem_chest==null){
                	invmoveitem_chest=new Location(((TileEntityChest) i).getWorld().getWorld(), srcChest.getX(), srcChest.getY(), srcChest.getZ());
                }else{
                	invmoveitem_chest.setWorld(((TileEntityChest) i).getWorld().getWorld());
                	invmoveitem_chest.setX(srcChest.getX());
                	invmoveitem_chest.setY(srcChest.getY());
                	invmoveitem_chest.setZ(srcChest.getZ());
                }
                if(invmoveitem_chest.getBlock().getRelative(BlockFace.UP).getState() instanceof Sign){
                	invmove_sign = (Sign)invmoveitem_chest.getBlock().getRelative(BlockFace.UP).getState();

                	if(invmove_sign.getLine(0).startsWith(prefix) ){
                		if(e.getSource().getType()==InventoryType.HOPPER){
                			 i = ((CraftInventory)e.getSource()).getInventory();
                			 if(i instanceof TileEntityHopper){
                          		e.setCancelled(true);
                			 }
                		}
                	}
                }
            }
        }
    }
	
	Player click_p;
	kConfig click_config;
	int click_id;
	byte click_data;
	@EventHandler
	public void UseInv(InventoryClickEvent ev){
		if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;

		click_p=(Player)ev.getWhoClicked();
		if(open_chest.containsKey(click_p)){
			ev.setCancelled(true);
			
			click_config=UtilServer.getUserData().getConfig(click_p);
			click_id = UtilNumber.toInt(click_config.getString("UserStores."+getLocString(open_chest.get(click_p).getLocation())+".id"));
			click_data = UtilNumber.toByte(click_config.getString("UserStores."+getLocString(open_chest.get(click_p).getLocation())+".data"));
			
			if(ev.getCurrentItem().getTypeId()==click_id&&ev.getCurrentItem().getData().getData()==click_data){
				if(ev.getClickedInventory().getName().equalsIgnoreCase("container.chest")&&ev.getInventory().getName().equalsIgnoreCase("container.chest")){
					click_p.getInventory().addItem(ev.getCurrentItem());
					ev.getInventory().setItem(ev.getSlot(), null);
				}else{
					ev.getInventory().addItem(ev.getCurrentItem());
					click_p.getInventory().setItem(ev.getSlot(), null);
				}
			}
		}
	}
	
	@EventHandler
	public void createConfig(UserDataConfigLoadEvent ev){
		if(!ev.getConfig().contains("Stores")){
			ev.getConfig().set("Stores", 5);
			ev.getConfig().save();
		}
	}
	
	@EventHandler
	public void BlockPhysics(BlockPhysicsEvent ev){
		if(ev.getBlock().getState() instanceof Sign){
			ev.setCancelled(true);
		}
	}

	Sign bbreak_sign;
	kConfig bbreak_config;
	@EventHandler
	public void bbreak(BlockBreakEvent ev){
		if(ev.getBlock().getState() instanceof Sign){
			bbreak_sign=(Sign)ev.getBlock().getState();
			
			if(bbreak_sign.getLine(0).startsWith(prefix) ){
				bbreak_config=UtilServer.getUserData().getConfig(ev.getPlayer());
				
				if(bbreak_config.contains("UserStores."+getLocString(bbreak_sign.getLocation()))){
					bbreak_config.set("UserStores."+getLocString(bbreak_sign.getLocation()), null);
					bbreak_sign.setType(Material.AIR);
				}else{
					ev.setCancelled(true);
				}
			}
		}else if(ev.getBlock().getState() instanceof Chest){
			if(ev.getBlock().getRelative(BlockFace.UP).getState() instanceof Sign){
				bbreak_sign=(Sign)ev.getBlock().getRelative(BlockFace.UP).getState();
				
				if(bbreak_sign.getLine(0).startsWith(prefix) ){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	Chest close_chest;
	kConfig close_config;
	@EventHandler
	public void closeInv(InventoryCloseEvent ev){
		if(ev.getPlayer() instanceof Player && open_chest.containsKey(ev.getPlayer())){
			if(open_chest.get(ev.getPlayer()).getLocation().getBlock().getRelative(BlockFace.DOWN).getState() instanceof Chest){
				close_chest=(Chest)open_chest.get(ev.getPlayer()).getLocation().getBlock().getRelative(BlockFace.DOWN).getState();
				setAnzahl(open_chest.get(ev.getPlayer()), UtilInv.AnzahlInInventory(close_chest.getInventory(), 0, (byte)0));
				
				UtilServer.getUserData().getConfig(((Player)ev.getPlayer())).set("UserStores."+getLocString(open_chest.get(ev.getPlayer()).getLocation())+".anzahl",getAnzahl(open_chest.get(ev.getPlayer())));
			}
			open_chest.remove(ev.getPlayer());
		}
	}
	
	BlockFace[] faces = new BlockFace[]{BlockFace.EAST,BlockFace.WEST,BlockFace.NORTH,BlockFace.SOUTH};
	@EventHandler
	public void blockDoubleChest(BlockPlaceEvent ev){
		if(ev.getItemInHand().getType() == Material.CHEST){
			for(BlockFace face : faces){
				if(ev.getBlock().getLocation().getBlock().getRelative(face).getState() instanceof Chest){
					if(ev.getBlock().getLocation().getBlock().getRelative(face).getRelative(BlockFace.UP).getState() instanceof Sign){
						if( ((Sign)ev.getBlock().getLocation().getBlock().getRelative(face).getRelative(BlockFace.UP).getState()).getLine(0).startsWith(prefix) ){
							ev.setCancelled(true);
							break;
						}
					}
				}
			}
		}
	}
	
	Sign open_sign;
	kConfig open_config;
	@EventHandler
	public void openChest(PlayerInteractEvent ev){
		if(ev.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(ev.getClickedBlock().getState() instanceof Chest){
				if(ev.getClickedBlock().getRelative(BlockFace.UP).getState() instanceof Sign){
					open_sign=(Sign)ev.getClickedBlock().getRelative(BlockFace.UP).getState();
					
					if(open_sign.getLine(0).startsWith(prefix)){
						open_config=UtilServer.getUserData().getConfig(ev.getPlayer());
						
						if(!ev.getPlayer().isOp()){
							if(!open_config.contains("UserStores."+getLocString(open_sign.getLocation()))){
								ev.setCancelled(true);
								return;
							}
						}
						
						open_chest.put(ev.getPlayer(), open_sign);
					}
				}
			}
		}
	}
	//[UserStore]
	//PREIS
	
	double store_preis;
	Chest store_chest;
	int store_anzahl;
	kConfig store_config;
	String ll;
	@EventHandler
	public void createSign(SignChangeEvent ev){
		if(ev.getLine(0).equalsIgnoreCase("[UserStore]")||ev.getLine(0).equalsIgnoreCase("[UserShop]")){
			if(!ev.getPlayer().isOp()){
				store_anzahl=UtilServer.getUserData().getConfig(ev.getPlayer()).getInt("Stores");
				
				if(store_anzahl<=0){
					ev.setLine(0, "§4ERROR: ");
					ev.setLine(1, "§4Du hast keine");
					ev.setLine(2, "§4freien Stores");
					ev.setLine(3, "§4mehr zum setzten!");
					return;
				}
			}
			if(!ev.getLine(1).isEmpty()){
				try{
					store_preis = getPreis(ev);
					if(store_preis>0){
						if(ev.getBlock().getRelative(BlockFace.DOWN).getType()==Material.CHEST){
							store_chest = (Chest)ev.getBlock().getRelative(BlockFace.DOWN).getState();
							if(store_chest.getInventory().getSize() == InventorySize._27.getSize()){
								if(!UtilInv.isInventoryEmpty(store_chest.getInventory())){
									if(UtilInv.itemsAllSame(store_chest.getInventory())){
										ItemStack i = UtilInv.getFirstItem(store_chest.getInventory());
										
										if(i.getData().getData()==0){
											setID(ev, i.getTypeId());
										}else{
											setIDundData(ev, i.getTypeId(),i.getData().getData());
										}
										ev.setLine(0,this.prefix+"/"+UtilPlayer.getPlayerId(ev.getPlayer()));
										setPreis(ev, store_preis);
										store_config = UtilServer.getUserData().getConfig(ev.getPlayer());
										store_config.set("Stores", (store_anzahl-1));
										store_anzahl = UtilInv.AnzahlInInventory(store_chest.getInventory(), i.getTypeId(), i.getData().getData());
										setAnzahl(ev, store_anzahl);
										
										ll = getLocString(ev.getBlock().getLocation());
										store_config.set("UserStores."+ll+".preis", store_preis);
										store_config.set("UserStores."+ll+".id", i.getTypeId());
										store_config.set("UserStores."+ll+".data", i.getData().getData());
										store_config.set("UserStores."+ll+".anzahl", store_anzahl);
										store_config.save();
										
										Bukkit.getPluginManager().callEvent(new PlayerCreateUserStoreEvent(ev.getPlayer(), (Sign)ev.getBlock().getState()));
										return;
									}else{
										ev.setLine(0, "§4ERROR: ");
										ev.setLine(1, "§4Keine gleichen");
										ev.setLine(2, "§4Items in der");
										ev.setLine(3, "§4Chest");
									}
								}else{
									ev.setLine(0, "§4ERROR: ");
									ev.setLine(1, "§4Chest ist leer");
								}
							}else{
								ev.setLine(0, "§4ERROR: ");
								ev.setLine(1, "§4Double Chests");
								ev.setLine(2, "§4sind nicht");
								ev.setLine(3, "§4erlaubt§l!");
							}
						}else{
							ev.setLine(0, "§4ERROR: ");
							ev.setLine(1, "§4Chest fehlt");
						}
					}else{
						ev.setLine(0, "§4ERROR: ");
						ev.setLine(1, "§4Der Preis muss");
						ev.setLine(2, "§4über 0.0 sein!");
					}
				}catch(NumberFormatException e){
					ev.setLine(0, "§4ERROR: ");
					ev.setLine(1, "§4Das '"+ev.getLine(1)+"' ist");
					ev.setLine(2, "§4keine Zahl!");
					ev.setLine(3, "§a1.0 <-");
				}
			}else{
				ev.setLine(0, "§4ERROR: ");
				ev.setLine(1, "§4Der Preis fehlt");
			}
		}
	}
	
	public boolean isUUID(Sign sign){
		return sign.getLine(0).split("/")[1].matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
	}
	
	public int getPlayerId(Sign sign){
		return UtilNumber.toInt(sign.getLine(0).split("/")[1]);
	}
	
	public void setIDundData(SignChangeEvent sign,int id,byte data){
		sign.setLine(2, "Item ID: "+id+":"+data);
	}
	
	public void setID(SignChangeEvent sign,int id){
		sign.setLine(2, "Item ID: "+id);
	}
	
	public int getAnzahl(Sign sign){
		return UtilNumber.toInt(sign.getLine(3).split(" ")[0]);
	}
	
	public void setAnzahl(SignChangeEvent sign,int anzahl){
		sign.setLine(3, anzahl +" Stück");
	}
	
	public void setAnzahl(Sign sign,int anzahl){
		sign.setLine(3, anzahl +" Stück");
		sign.update();
	}
	
	public double getPreis(SignChangeEvent sign){
		return UtilNumber.toDouble(sign.getLine(1).split(" ")[0]);
	}
	
	public double getPreis(Sign sign){
		return UtilNumber.toDouble(sign.getLine(1).split(" ")[0]);
	}
	
	public void setPreis(SignChangeEvent sign, double preis){
		sign.setLine(1, preis +" $");
	}
	
	public void setPreis(Sign sign, double preis){
		sign.setLine(1, preis +" $");
		sign.update();
	}
}
