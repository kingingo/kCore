package me.kingingo.kcore.Kit.Shop;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Calendar.Calendar;
import me.kingingo.kcore.Calendar.Calendar.CalendarType;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Game.Events.GameStartEvent;
import me.kingingo.kcore.Kit.Kit;
import me.kingingo.kcore.Kit.KitType;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Scoreboard.PlayerScoreboard;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.Tokens;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class KitShop implements Listener {

	@Getter
	String name;
	@Getter
	Inventory inventory;
	@Getter
	Inventory admininventory;
	@Getter
	Kit[] kits;
	@Getter
	PermissionManager permManager;
	@Getter
	Coins coins;
	@Getter
	Tokens tokens;
	HashMap<Player,Inventory> l = new HashMap<>();
	CalendarType holiday;
	
	public KitShop(JavaPlugin instance,Coins coins,Tokens tokens,PermissionManager manager,String name,InventorySize size,Kit[] kits){
		this.name=name;
		this.kits=kits;
		this.coins=coins;
		this.tokens=tokens;
		this.permManager=manager;
		this.holiday=Calendar.getHoliday();
		if(kits.length>size.getSize())size=InventorySize._45;
		this.inventory=Bukkit.createInventory(null, size.getSize(), getName());
		this.admininventory=Bukkit.createInventory(null, size.getSize(), getName());
		
		for(Kit k : kits){
			if(k.getType()!=KitType.ADMIN)getInventory().addItem( k.getItem() );
			
			getAdmininventory().addItem( k.getItem() );
		}
		
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void Start(GameStartEvent ev){
		for(Kit k : kits){
			for(Perk perk : k.getPerks()){
				Bukkit.getPluginManager().registerEvents(perk, permManager.getInstance());
			}
		}
		Bukkit.getPluginManager().callEvent(new PerkStartEvent());
		
		
	}
	
	public void getInv(Player p){
		if(l.containsKey(p)){
			p.openInventory(l.get(p));
			return;
		}
		if(permManager.hasPermission(p, Permission.ADMIN_KIT)){
			Inventory inv = Bukkit.createInventory(null, getAdmininventory().getSize(), getAdmininventory().getTitle());
			for(ItemStack i : getAdmininventory()){
				if(i==null||i.getType()==Material.AIR)continue;
				for(Kit k : getKits()){
					if(UtilItem.ItemNameEquals(i, k.getItem())){
						if(getPermManager().hasPermission(p, k.getPermission())||getPermManager().hasPermission(p, Permission.ALL_KITS)){
							inv.addItem(UtilItem.addEnchantmentGlow(i.clone()));	
						}else{
							inv.addItem(i.clone());
						}
						break;
					}
				}
			}
			l.put(p, inv);
			p.openInventory(inv);
		}else{
			Inventory inv = Bukkit.createInventory(null, getInventory().getSize(), getInventory().getTitle());
			for(ItemStack i : getInventory()){
				if(i==null||i.getType()==Material.AIR)continue;
				for(Kit k : getKits()){
					if(UtilItem.ItemNameEquals(i, k.getItem().clone())){
						if(getPermManager().hasPermission(p, k.getPermission())||getPermManager().hasPermission(p, Permission.ALL_KITS)){
							inv.addItem(UtilItem.addEnchantmentGlow(i.clone()));	
						}else{
							inv.addItem(i.clone());
						}
						break;
					}
				}
			}
			l.put(p, inv);
			p.openInventory(inv);
		}
	}
	
	@EventHandler
	public void ShopOpen(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.R)){
			if(ev.getPlayer().getItemInHand()!=null&&UtilItem.ItemNameEquals(ev.getPlayer().getItemInHand(), UtilItem.RenameItem(new ItemStack(Material.CHEST), "§bKitShop"))){
				getInv(ev.getPlayer());
			}
		}
	}
	
	public Inventory getInv(Kit kit,Player p){
		Inventory inventory=Bukkit.createInventory(null, 9, kit.getName());

		if(holiday!=null&&holiday==CalendarType.GEBURSTAG&&kit.getType()!=KitType.ADMIN){
			inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
			inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
			inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
		}else{
			if(kit.getType()==KitType.STARTER){
				inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
				inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
				inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
			}else if(kit.getType()==KitType.ADMIN){
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, Permission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.Item(new ItemStack(Material.REDSTONE),new String[]{"§7Dieses Kit ist ein §cAdmin-Kit"} ,"§4Spezial-Kit"));
				}
			}else if(kit.getType()==KitType.SPEZIAL_KIT){
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, Permission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.Item(new ItemStack(Material.REDSTONE),new String[]{"§7Dieses Kit ist ein §aSpezial-Kit","§7Nur erhältlich zu Besonderen anlässen!"} ,"§4Spezial-Kit"));
				}
			}else if(kit.getType()==KitType.PREMIUM){
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, Permission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.Item(new ItemStack(Material.REDSTONE),new String[]{"§7Dieses Kit ist ein §aPremium-Kit","§eShop.EpicPvP.de"} ,"§cPremium-Kit"));
				}
			}else{
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, Permission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.GLOWSTONE_DUST) ,"§6Kaufen"));
				}
			}
		}

		for(int i = 0 ; i < inventory.getSize(); i++){
			if(inventory.getItem(i)==null||inventory.getItem(i).getType()==Material.AIR){
				if(inventory.getItem(i)==null)inventory.setItem(i, new ItemStack(Material.FENCE));
				inventory.getItem(i).setTypeId(160);
				inventory.getItem(i).setDurability((short) 7);
				ItemMeta im = inventory.getItem(i).getItemMeta();
				im.setDisplayName(" ");
				inventory.getItem(i).setItemMeta(im);
			}
		}
		inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.IRON_DOOR), "§cZurück"));
		
		return inventory;
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		for(Kit kit : getKits()){
			kit.removePlayer(ev.getPlayer());
		}
	}
	
	public Inventory getKaufen(Kit kit){
		Inventory inventory=Bukkit.createInventory(null, 9, kit.getName()+" §aKaufen");
		switch(kit.getType()){
		case KAUFEN:
			inventory.setItem(0, UtilItem.RenameItem(new ItemStack(Material.GOLD_NUGGET), "§eCoins"));
			inventory.setItem(4, kit.getItem());
			inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.GOLD_INGOT), "§6Tokens"));
			break;
		case KAUFEN_COINS:
			inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.GOLD_NUGGET), "§eCoins"));
			inventory.setItem(0, kit.getItem());
			break;
		case KAUFEN_TOKENS:
			inventory.setItem(0, kit.getItem());
			inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.GOLD_INGOT), "§6Tokens"));
			break;
		}
		for(int i = 0 ; i < inventory.getSize(); i++){
			if(inventory.getItem(i)==null||inventory.getItem(i).getType()==Material.AIR){
				if(inventory.getItem(i)==null)inventory.setItem(i, new ItemStack(Material.FENCE));
				inventory.getItem(i).setTypeId(160);
				inventory.getItem(i).setDurability((short) 7);
				ItemMeta im = inventory.getItem(i).getItemMeta();
				im.setDisplayName(" ");
				inventory.getItem(i).setItemMeta(im);
			}
		}
		return inventory;
	}
	
	@EventHandler
	public void ClickInventory(InventoryClickEvent ev){
	if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;
		if(ev.getInventory().getName().equalsIgnoreCase(getName())){
			Player p = (Player)ev.getWhoClicked();
			ev.setCancelled(true);
			p.closeInventory();
			for(Kit kit : getKits()){
				if(UtilItem.ItemNameEquals(ev.getCurrentItem(), kit.getItem().clone())){
					p.openInventory(getInv(kit, p));
					break;
				}
			}
		}else{
			for(Kit kit : getKits()){
				if(kit.getName().equalsIgnoreCase(ev.getInventory().getName())){
					Player p = (Player)ev.getWhoClicked();
					ev.setCancelled(true);
					
					if(ev.getCurrentItem().getType()==Material.IRON_DOOR||ev.getCurrentItem().getType()==Material.REDSTONE){
						p.closeInventory();
						getInv(p);
					}else if(ev.getCurrentItem().getType()==Material.FIRE){
						kit.addPlayer(p);
						p.sendMessage(Text.PREFIX.getText()+Text.KIT_SHOP_ADD.getText(kit.getName()));
						p.closeInventory();
						UtilInv.remove(p, UtilItem.RenameItem(new ItemStack(Material.CHEST), "§bKitShop"), 1);
					}else if(ev.getCurrentItem().getType()==Material.GLOWSTONE_DUST){
						p.openInventory(getKaufen(kit));
					}
					break;
				}else if(String.valueOf(kit.getName()+" §aKaufen").equalsIgnoreCase(ev.getInventory().getName())){
					Player p = (Player)ev.getWhoClicked();
					ev.setCancelled(true);
					p.closeInventory();
					
					if(ev.getCurrentItem().getType()==Material.GOLD_NUGGET){
						int c = getCoins().getCoins(p);
						if(c>=kit.getPreis()){
							getCoins().delCoins(p, true, kit.getPreis());
							getPermManager().addPermission(p, kit.getPermission());
							p.sendMessage(Text.PREFIX.getText()+Text.KIT_SHOP_BUYED_KIT.getText(kit.getName()));
						}else{
							p.sendMessage(Text.PREFIX.getText()+Text.KIT_SHOP_NO_MONEY.getText("Coins"));
						}
					}else if(ev.getCurrentItem().getType()==Material.GOLD_INGOT){
						int c = getTokens().getTokens(p);
						if(c>=kit.getPreis()){
							getTokens().delTokens(p, true, kit.getPreis());
							getPermManager().addPermission(p, kit.getPermission());
							p.sendMessage(Text.PREFIX.getText()+Text.KIT_SHOP_BUYED_KIT.getText(kit.getName()));
						}else{
							p.sendMessage(Text.PREFIX.getText()+Text.KIT_SHOP_NO_MONEY.getText("Tokens"));
						}
					}
					break;
				}
			}
		}
	}
	
}
