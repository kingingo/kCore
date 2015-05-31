package me.kingingo.kcore.SignShop;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.SignShop.Events.SignShopUseEvent;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilList;

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

public class SignShop extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<Player,Long> shop = new HashMap<Player,Long>();
	@Getter
	private StatsManager statsmanager;
	
	public SignShop(JavaPlugin instance,StatsManager statsmanager){
		super(instance,"[SignShop]");
		this.instance=instance;
		this.statsmanager=statsmanager;
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
		if(ev.getLine(0).toLowerCase().contains("[shop]")){
			if(!p.isOp()){
				ev.setLine(0, "Nö!");
				ev.setLine(1, "Nö!");
				ev.setLine(2, "Nö!");
				ev.setLine(3, "Nö!");
				return;
			}
			ev.setLine(0, ChatColor.AQUA + "[Shop]");
			p.sendMessage(Text.PREFIX.getText()+"§eDie Sign wurde erstellt!");
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
		
		if(getStatsmanager().getDouble(Stats.MONEY, p) < Geld){
			p.sendMessage(Text.PREFIX.getText()+"§cDu hast nicht genug Geld!");
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
				p.sendMessage(Text.PREFIX.getText()+"§cNicht so Schnell ...");
				return;
			}else if(Action.RIGHT_CLICK_BLOCK == a){
				
				if(ev.getClickedBlock().getState() instanceof Sign){
					
	                sign = (Sign) ev.getClickedBlock().getState();
	                
	                if(sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop]")){	
	    				event = new SignShopUseEvent(sign, p, ev);
	    				Bukkit.getPluginManager().callEvent(event);
	    				if(event.isCancelled())return;
	                	
	                	ev.setCancelled(true);
	                	if(shop.containsKey(p)){
	                		
	                		if(shop.get(p) <= System.currentTimeMillis()){
	                			shop.remove(p);
	                		}else{
	                			p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_DELAY.getText(3));
	                			return;
	                		}
	                		
	                	}else{
	                		shop.put(p, System.currentTimeMillis() + 3000);
	                	}
	                		
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
	            					p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_GET_.getText(new String[]{"32",String.valueOf(id),idnach,String.valueOf(Preis)}));
	            					getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)-Preis, Stats.MONEY);
	                				return;
	                			}
	                			//: ID+SNEAK 
	                			
	                			int id = Integer.valueOf(sign.getLine(3));
	            				ItemStack i = new ItemStack(id, 32);
	            				p.getInventory().addItem(i);
	        					p.updateInventory();
	        					p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_GET.getText(new String[]{"32", String.valueOf(id),String.valueOf(Preis)}));
	        					getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)-Preis, Stats.MONEY);
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
	        					p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_GET_.getText(new String[]{String.valueOf(anzahl),String.valueOf(id),idnach,String.valueOf(Preis)}));
	        					getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)-Preis, Stats.MONEY);
	        					return;
	            			}
	                		//:
	                		
	                		int id = Integer.valueOf(sign.getLine(3));
	                		
	                		
	        				ItemStack i = new ItemStack(id, anzahl);
	        				p.getInventory().addItem(i);
	        				p.updateInventory();
	        				//"§6Du hast " + anzahl + " mal " + id+" bekommen dir wurden " + Preis + " §bEpics §6abgezogen ."
	    					p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_GET.getText(new String[]{String.valueOf(anzahl),String.valueOf(id),String.valueOf(Preis)}));
	    					getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)-Preis, Stats.MONEY);
	                }
				}
				
			}else if(Action.LEFT_CLICK_BLOCK == a){
				
				if(ev.getClickedBlock().getState() instanceof Sign){
					
	                sign = (Sign) ev.getClickedBlock().getState();
	                
	                if(sign.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"[shop]")){
	                	event = new SignShopUseEvent(sign, p, ev);
	    				Bukkit.getPluginManager().callEvent(event);
	    				if(event.isCancelled())return;
	                	ev.setCancelled(true);
	            
	                	
	                	if(shop.containsKey(p)){
	                		
	                		if(shop.get(p) <= System.currentTimeMillis()){
	                			shop.remove(p);
	                		}else{
	                			p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_DELAY.getText(3));
	                			return;
	                		}
	                		
	                	}else{
	                		shop.put(p, System.currentTimeMillis() + 3000);
	                	}
	                	
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
	        						p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_NO_ITEM_ON_INV.getText());
	        						return;
	        					}
	            				p.updateInventory();
	            				VerkaufPreis=VerkaufPreis*b;
	        					p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_VERKAUFT_.getText(new String[]{String.valueOf(b),String.valueOf(id),idnach,String.valueOf(VerkaufPreis)}));
	        					getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)+VerkaufPreis, Stats.MONEY);
	                    		return;
	                		}
	                		//: ID

	                		int id = Integer.valueOf(sign.getLine(3));
	                		double VerkaufPreis = Double.valueOf(Sell(sign.getLine(1)));

	                		//boolean b = RemoveItem(p, Integer.valueOf(sign.getLine(3)),32);
	        				int b = SellAll(p, Integer.valueOf(sign.getLine(3)));
	        				if(b==0){
	    						p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_NO_ITEM_ON_INV.getText());
	    						return;
	    					}
	                		
	    					p.updateInventory();
	    				
	    					VerkaufPreis=VerkaufPreis*b;	//"§6Du hast " + b + " mal " + id+" Verkauft und hast " + VerkaufPreis+" Epic's erhalten."
	    					p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_VERKAUFT.getText(new String[]{String.valueOf(b),String.valueOf(id),String.valueOf(VerkaufPreis)}));
	    					
	    					getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)+VerkaufPreis, Stats.MONEY);
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
	    						p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_NO_ITEM_ON_INV.getText());
	    						return;
	    					}
	        				p.updateInventory();	//"§6Du hast " + 32 + " mal " + id+":"+idnach + " Verkauft und hast " + VerkaufPreis+" Epic's erhalten."
	        				p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_VERKAUFT_.getText(new String[]{String.valueOf(anzahl),String.valueOf(id),idnach,String.valueOf(VerkaufPreis)}));
	        				getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)+VerkaufPreis, Stats.MONEY);
	                		return;
	            		}
	            		//: ID

	            		int id = Integer.valueOf(sign.getLine(3));
	            		double VerkaufPreis = Double.valueOf(Sell(sign.getLine(1)) * anzahl);

	            		boolean b = RemoveItem(p, Integer.valueOf(sign.getLine(3)),anzahl);
	    				
	    				if(!b){
							p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_NO_ITEM_ON_INV.getText());
							return;
						}
	            		
						p.updateInventory();
						p.sendMessage(Text.PREFIX.getText()+Text.SIGN_SHOP_VERKAUFT.getText(new String[]{String.valueOf(anzahl),String.valueOf(id),String.valueOf(VerkaufPreis)}));
						getStatsmanager().setDouble(p, getStatsmanager().getDouble(Stats.MONEY, p)+VerkaufPreis, Stats.MONEY);
	    				return;
	                }
				}
			}
			
		}
	
	
	
}