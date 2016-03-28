package eu.epicpvp.kcore.SignShop;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.protection.flags.StateFlag;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import lombok.Getter;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.SignShop.Events.SignShopUseEvent;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilList;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilWorldGuard;

public class SignShop extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<Player,Long> shop = new HashMap<Player,Long>();
	@Getter
	private StatsManager statsmanager;
	private StateFlag flag;
	
	public SignShop(JavaPlugin instance,CommandHandler handle,StatsManager statsmanager){
		super(instance,"SignShop");
		this.instance=instance;
		handle.register(CommandShop.class, new CommandShop(statsmanager.getInstance()));
		this.statsmanager=statsmanager;
		this.flag=new StateFlag("shop",false);
		UtilWorldGuard.addCustomFlag(flag);
	}
	
	boolean b = false;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_64)return;
		UtilList.CleanList(shop);
	}
	
	@EventHandler
	public void onSign (SignChangeEvent ev){
		Player p = ev.getPlayer();
		if(ev.getLine(0).toLowerCase().contains("[shop-buy]")||ev.getLine(0).toLowerCase().contains("[shop-sale]")||ev.getLine(0).toLowerCase().contains("[shop]")){
			if(UtilWorldGuard.RegionFlag(ev.getBlock().getLocation(), flag)||p.hasPermission(PermissionType.SHOP_SIGN_CREATE_BYPASS.getPermissionToString())){
				if(p.hasPermission(PermissionType.SHOP_SIGN_CREATE.getPermissionToString())){
					ev.setLine(0, ChatColor.AQUA + "[Shop]");
					
					for(Player player : UtilServer.getPlayers())
						if(player.hasPermission(PermissionType.SHOP_SIGN_CREATE_MSG.getPermissionToString())){
							player.sendMessage(Language.getText(player,"PREFIX")+"Shop Schild erstellt von §c"+p.getName()+"§7: ");
							for(String line : ev.getLines()){
								player.sendMessage(Language.getText(player,"PREFIX")+line);
							}
							player.sendMessage(Language.getText(player, "PREFIX")+" Welt:"+ev.getBlock().getLocation().getWorld().getName()+" X:"+ev.getBlock().getLocation().getBlockX()+" Y:"+ev.getBlock().getLocation().getBlockY()+" Z:"+ev.getBlock().getLocation().getBlockZ());
					}
					
					p.sendMessage(Language.getText(p, "PREFIX")+"§eDie Sign wurde erstellt!");
					return;
				}
			}
			ev.setLine(0, "N§!");
			ev.setLine(1, "N§!");
			ev.setLine(2, "N§!");
			ev.setLine(3, "N§!");
		}
	}
	
	public boolean RemoveItem(Player p, int id,int anzahl){
		boolean b = false;
		
		for(ItemStack i : p.getInventory().getContents()){
			
			if(i != null){
			if(i.getTypeId() == id){
				
				if(i.getAmount() == anzahl){
					b = true;
					p.getInventory().remove(i);
					break;
				}
				
				if(i.getAmount() < anzahl){
					
					break;
				}
				
				if(i.getAmount() > anzahl){
					b = true;
					i.setAmount(i.getAmount() - anzahl);
					break;
				}
				
				break;
			}
			}
			
		}
		
		return b;
		
	}
	
	public boolean RemoveItemDoppelPunkt(Player p, String ids,int anzahl){
		boolean b = false;
		int id = ID(ids);
		String idnach = IDnach(ids);
		
		for(ItemStack i : p.getInventory().getContents()){
			
			if(i != null){
			if(i.getTypeId() == id){
				
				if(i.getDurability() == Short.parseShort(idnach)){

					if(i.getAmount() == anzahl){
						b = true;
						p.getInventory().remove(i);
						break;
					}
					
					if(i.getAmount() < anzahl){
						
						break;
					}
					
					if(i.getAmount() > anzahl){
						b = true;
						i.setAmount(i.getAmount() - anzahl);
						break;
					}
				
				break;
			}
				
			}
			}
			
		}
		
		return b;
		
	}
	
	public boolean Money(Player p,double Geld){
		boolean b = true;
		
		if(getStatsmanager().getDouble(StatsKey.MONEY, p) < Geld){
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "NOT_ENOUGH_MONEY"));
			b = false;
		}
		
		return b;
	}
	
	public Integer SellAll(Player p,int id){
		int a = 0;
		
		for(ItemStack i : p.getInventory().getContents()){
			if(i != null){
				if(i.getTypeId() == id){
					a=a+i.getAmount();
					p.getInventory().remove(i);
				}
			}
		}
		
		return a;
	}
	
	public Double Buy(String b){
		Double i = 0.0;
		
		if(b.contains(":")){
			String[] d = b.split(":");
			i = Double.valueOf(d[0]);
			
		}
		
		return i;
	}
	
	public Integer SellAllDoppelPunkt(Player p,String ids){
		int a = 0;
		int id = ID(ids);
		String idnach = IDnach(ids);
		
		for(ItemStack i : p.getInventory().getContents()){
			if(i != null){
				if(i.getTypeId() == id){
					
					if(!(i.getDurability()==Short.parseShort(idnach))){
						break;
					}
					
					a=a+i.getAmount();
					p.getInventory().remove(i);
				}
			}
		}
		
		return a;
	}
	
	public Double Sell(String s){
		Double i = 0.0;
		
		if(s.contains(":")){
			String[] d = s.split(":");
			i = Double.valueOf(d[1]);
			
		}
		
		return i;
	}
	
	public Integer ID(String b){
		int i = 0;
		
		if(b.contains(":")){
			String[] d = b.split(":");
			i = Integer.valueOf(d[0]);
		}
		
		return i;
	}
	
	public String IDnach(String b){
		String i = "";
		
		if(b.contains(":")){
			String[] d = b.split(":");
			i = d[1];
		}
		
		return i;
	}
	
		//[shop]
		//KaufPreis:VerkaufPreis
		//anzahl
		//ID:ZAHL
		Player p;
		Sign sign;
		Action a;
		SignShopUseEvent event;
		@EventHandler(priority=EventPriority.LOWEST)
		public void onInteract(PlayerInteractEvent ev){
			p = ev.getPlayer();
			a = ev.getAction();
			
			if(Action.RIGHT_CLICK_BLOCK == a && Action.LEFT_CLICK_BLOCK == a){
				ev.setCancelled(true);
				p.sendMessage(Language.getText(p, "PREFIX")+"§cNicht so Schnell ...");
				return;
			}else if(Action.RIGHT_CLICK_BLOCK == a){
				
				if(ev.getClickedBlock().getState() instanceof Sign){
					
	                sign = (Sign) ev.getClickedBlock().getState();
	                
	                if(sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop]")||sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop-buy]")||sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop-sale]")){	
	    				if(shop.containsKey(p)){
	                		
	                		if(shop.get(p) <= System.currentTimeMillis()){
	                			shop.remove(p);
	                		}else{
	                			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_DELAY",3));
	                			return;
	                		}
	                		
	                	}else{
	                		if(!p.isOp())shop.put(p, System.currentTimeMillis() + 3000);
	                	}
	                	
	                	event = new SignShopUseEvent(sign,SignShopAction.BUY, p, ev);
	    				Bukkit.getPluginManager().callEvent(event);
	                	ev.setCancelled(true);
	    				if(event.isCancelled())return;
	                		
	                		int anzahl = Integer.valueOf(sign.getLine(2));
	                		
	                		//SNEAK
	                		
	                		if(p.isSneaking()){
	                			double Preis = Double.valueOf(Buy(sign.getLine(1)) * 32);
	                			
	                			boolean b = Money(p, Preis);
	                			
	                			if(!b){
	                				return;
	                			}
	                			
	                			// : ID+SNEAK
	                			if(sign.getLine(3).contains(":")){
	                				int id = ID(sign.getLine(3));
	                				String idnach = IDnach(sign.getLine(3));

	                				ItemStack i = new ItemStack(id, 32,Short.parseShort(idnach) );
	                				p.getInventory().addItem(i);
	            					p.updateInventory(); //"§6Du hast " + 32 + " mal " + id+":"+ Integer.valueOf(idnach) + " bekommen dir wurden " + Preis + " §bEpics §6abgezogen ."
	            					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_GET_",new String[]{"32",String.valueOf(id),idnach,String.valueOf(Preis)}));
	            					getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)-Preis, StatsKey.MONEY);
	            					Log("Der Spieler "+p.getName()+" hat 32 mal "+id+":"+idnach+" gekauft und "+Preis+" Epics bezahlt.");
		            				return;
	                			}
	                			//: ID+SNEAK 
	                			
	                			int id = Integer.valueOf(sign.getLine(3));
	            				ItemStack i = new ItemStack(id, 32);
	            				p.getInventory().addItem(i);
	        					p.updateInventory();
	        					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_GET",new String[]{"32", String.valueOf(id),String.valueOf(Preis)}));
	        					getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)-Preis, StatsKey.MONEY);
		                		Log("Der Spieler "+p.getName()+" hat 32 mal "+id+" gekauft und "+Preis+" Epics bezahlt.");
	            				return;
	                		}
	                		
	                		//SNEAK
	                		
	                		double Preis = Double.valueOf(Buy(sign.getLine(1)) * anzahl);
	                		
	                		boolean b = Money(p, Preis);
	                		
	                		if(!b){
	                			return;
	                		}
	                		
	                		//:
	                		if(sign.getLine(3).contains(":")){
	            				int id = ID(sign.getLine(3));
	            				String idnach = IDnach(sign.getLine(3));
	            				ItemStack i = new ItemStack(id, anzahl,Short.parseShort(idnach) );
	            				p.getInventory().addItem(i);
	        					p.updateInventory();
	        					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_GET_",new String[]{String.valueOf(anzahl),String.valueOf(id),idnach,String.valueOf(Preis)}));
	        					getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)-Preis, StatsKey.MONEY);
		                		Log("Der Spieler "+p.getName()+" hat "+anzahl+" mal "+id+":"+idnach+" gekauft und "+Preis+" Epics bezahlt.");
	        					return;
	            			}
	                		//:
	                		
	                		int id = Integer.valueOf(sign.getLine(3));
	                		
	                		
	        				ItemStack i = new ItemStack(id, anzahl);
	        				p.getInventory().addItem(i);
	        				p.updateInventory();
	        				//"§6Du hast " + anzahl + " mal " + id+" bekommen dir wurden " + Preis + " §bEpics §6abgezogen ."
	    					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_GET",new String[]{String.valueOf(anzahl),String.valueOf(id),String.valueOf(Preis)}));
	    					getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)-Preis, StatsKey.MONEY);
	                		Log("Der Spieler "+p.getName()+" hat "+anzahl+" mal "+id+" gekauft und "+Preis+" Epics bezahlt.");
	                }
				}
				
			}else if(Action.LEFT_CLICK_BLOCK == a){
				
				if(ev.getClickedBlock().getState() instanceof Sign){
					
	                sign = (Sign) ev.getClickedBlock().getState();
	                
	                if(sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop]")||sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop-buy]")||sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop-sale]")){
	                	if(shop.containsKey(p)){
	                		
	                		if(shop.get(p) <= System.currentTimeMillis()){
	                			shop.remove(p);
	                		}else{
	                			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_DELAY",3));
	                			return;
	                		}
	                		
	                	}else{
	                		if(!p.isOp())shop.put(p, System.currentTimeMillis() + 3000);
	                	}
	                	
	                	event = new SignShopUseEvent(sign,SignShopAction.SALE, p, ev);
	    				Bukkit.getPluginManager().callEvent(event);
	                	ev.setCancelled(true);
	                	
	    				if(event.isCancelled())return;
	                	
	                	int anzahl = Integer.valueOf(sign.getLine(2));
	            		
	                	//SNEAK
	                	
	                	if(p.isSneaking()){
	                		
	                		//: ID
	                		if(sign.getLine(3).contains(":")){
	                			
	                			int id = ID(sign.getLine(3));
	            				String idnach = IDnach(sign.getLine(3));
	            				double VerkaufPreis = Double.valueOf(Sell(sign.getLine(1)));
	                			
	            				//boolean b = RemoveItemDoppelPunkt(p, sign.getLine(3),32);
	            				int b = SellAllDoppelPunkt(p, sign.getLine(3));
	            				if(b==0){
	        						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_NO_ITEM_ON_INV"));
	        						return;
	        					}
	            				p.updateInventory();
	            				VerkaufPreis=VerkaufPreis*b;
	        					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_VERKAUFT_",new String[]{String.valueOf(b),String.valueOf(id),idnach,String.valueOf(VerkaufPreis)}));
	        					getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)+VerkaufPreis, StatsKey.MONEY);
		                		Log("Der Spieler "+p.getName()+" hat 32 mal "+id+":"+idnach+" verkauft und "+VerkaufPreis+" Epics erhalten.");
	                    		return;
	                		}
	                		//: ID

	                		int id = Integer.valueOf(sign.getLine(3));
	                		double VerkaufPreis = Double.valueOf(Sell(sign.getLine(1)));

	                		//boolean b = RemoveItem(p, Integer.valueOf(sign.getLine(3)),32);
	        				int b = SellAll(p, Integer.valueOf(sign.getLine(3)));
	        				if(b==0){
	    						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_NO_ITEM_ON_INV"));
	    						return;
	    					}
	                		
	    					p.updateInventory();
	    				
	    					VerkaufPreis=VerkaufPreis*b;	//"§6Du hast " + b + " mal " + id+" Verkauft und hast " + VerkaufPreis+" Epic's erhalten."
	    					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_VERKAUFT",new String[]{String.valueOf(b),String.valueOf(id),String.valueOf(VerkaufPreis)}));
	    					
	    					getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)+VerkaufPreis, StatsKey.MONEY);
	                		Log("Der Spieler "+p.getName()+" hat 32 mal "+id+" verkauft und "+VerkaufPreis+" Epics erhalten.");
	    					return;
	                	}
	                	
	                	//SNEAK
	                	
	                	//: ID
	            		if(sign.getLine(3).contains(":")){
	            			
	            			int id = ID(sign.getLine(3));
	            			String idnach = IDnach(sign.getLine(3));
	            			double VerkaufPreis = Double.valueOf(Sell(sign.getLine(1)) * anzahl);
	            			
	        				boolean b = RemoveItemDoppelPunkt(p, sign.getLine(3), anzahl);
	        				
	        				if(!b){
	    						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_NO_ITEM_ON_INV"));
	    						return;
	    					}
	        				p.updateInventory();	//"§6Du hast " + 32 + " mal " + id+":"+idnach + " Verkauft und hast " + VerkaufPreis+" Epic's erhalten."
	        				p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_VERKAUFT_",new String[]{String.valueOf(anzahl),String.valueOf(id),idnach,String.valueOf(VerkaufPreis)}));
	        				getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)+VerkaufPreis, StatsKey.MONEY);
	                		Log("Der Spieler "+p.getName()+" hat "+anzahl+" mal "+id+":"+idnach+" verkauft und "+VerkaufPreis+" Epics erhalten.");
	        				return;
	            		}
	            		//: ID

	            		int id = Integer.valueOf(sign.getLine(3));
	            		double VerkaufPreis = Double.valueOf(Sell(sign.getLine(1)) * anzahl);

	            		boolean b = RemoveItem(p, Integer.valueOf(sign.getLine(3)),anzahl);
	    				
	    				if(!b){
							p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_NO_ITEM_ON_INV"));
							return;
						}
	            		
						p.updateInventory();
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "SIGN_SHOP_VERKAUFT",new String[]{String.valueOf(anzahl),String.valueOf(id),String.valueOf(VerkaufPreis)}));
						getStatsmanager().setDouble(p, getStatsmanager().getDouble(StatsKey.MONEY, p)+VerkaufPreis, StatsKey.MONEY);
	    				return;
	                }
				}
			}
			
		}
	
	
	
}