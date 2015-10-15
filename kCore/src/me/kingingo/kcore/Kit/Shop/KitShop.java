package me.kingingo.kcore.Kit.Shop;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Calendar.Calendar;
import me.kingingo.kcore.Calendar.Calendar.CalendarType;
import me.kingingo.kcore.Game.Events.GameStartEvent;
import me.kingingo.kcore.Kit.Kit;
import me.kingingo.kcore.Kit.KitType;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;
import me.kingingo.kcore.Kit.Shop.Events.KitShopPlayerDeleteEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class KitShop implements Listener {

	@Getter
	private String name;
	@Getter
	private Inventory inventory;
	@Getter
	private Inventory admininventory;
	@Getter
	private Kit[] kits;
	@Getter
	private PermissionManager permManager;
	@Getter
	private Coins coins;
	@Getter
	private Gems gems;
	HashMap<Player,Inventory> l = new HashMap<>();
	private CalendarType holiday;
	
	public KitShop(JavaPlugin instance,Gems gems,Coins coins,PermissionManager manager,String name,InventorySize size,Kit[] kits){
		this.name=name;
		this.kits=kits;
		this.gems=gems;
		this.coins=coins;
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
		if(permManager.hasPermission(p, kPermission.ADMIN_KIT)){
			Inventory inv = Bukkit.createInventory(null, getAdmininventory().getSize(), getAdmininventory().getTitle());
			for(ItemStack i : getAdmininventory()){
				if(i==null||i.getType()==Material.AIR)continue;
				for(Kit k : getKits()){
					if(UtilItem.ItemNameEquals(i, k.getItem())){
						if(getPermManager().hasPermission(p, k.getPermission())||getPermManager().hasPermission(p, kPermission.ALL_KITS)){
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
						if(getPermManager().hasPermission(p, k.getPermission())||getPermManager().hasPermission(p, kPermission.ALL_KITS)){
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
	public void Delete(KitShopPlayerDeleteEvent ev){
		for(Kit kit : getKits()){
			kit.removePlayer(ev.getPlayer());
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
			inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.EMERALD), Language.getText(p, "KIT_SHOP_CHOOSE")));
		}else{
			if(kit.getType()==KitType.STARTER){
				inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
				inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
				inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.EMERALD), Language.getText(p, "KIT_SHOP_CHOOSE")));
			}else if(kit.getType()==KitType.ADMIN){
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, kPermission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.EMERALD), Language.getText(p, "KIT_SHOP_CHOOSE")));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.Item(new ItemStack(Material.REDSTONE),new String[]{Language.getText(p, "KIT_SHOP_ADMIN")} ,"§4Spezial-Kit"));
				}
			}else if(kit.getType()==KitType.SPEZIAL_KIT){
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, kPermission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.EMERALD), Language.getText(p, "KIT_SHOP_CHOOSE")));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.Item(new ItemStack(Material.REDSTONE),new String[]{Language.getText(p, "KIT_SHOP_SPEZIAL1"),Language.getText(p, "KIT_SHOP_SPEZIAL2")} ,"§4Spezial-Kit"));
				}
			}else if(kit.getType()==KitType.PREMIUM){
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, kPermission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.EMERALD), Language.getText(p, "KIT_SHOP_CHOOSE")));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.Item(new ItemStack(Material.REDSTONE),new String[]{Language.getText(p, "KIT_SHOP_PREMIUM"),"§eShop.EpicPvP.de"} ,"§cPremium-Kit"));
				}
			}else{
				if(getPermManager().hasPermission(p, kit.getPermission())||getPermManager().hasPermission(p, kPermission.ALL_KITS)){
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.EMERALD), Language.getText(p, "KIT_SHOP_CHOOSE")));
				}else{
					inventory.setItem(0, UtilItem.RenameItem(kit.getItem().clone(), getName()));
					inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
					inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.GLOWSTONE_DUST) ,Language.getText(p,"KIT_SHOP_BUY")));
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
		inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.IRON_DOOR), Language.getText(p, "KIT_BACK")));
		
		return inventory;
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		for(Kit kit : getKits()){
			kit.removePlayer(ev.getPlayer());
		}
	}
	
	public Inventory getKaufen(Kit kit,Player p){
		Inventory inventory=Bukkit.createInventory(null, 9, kit.getName()+" "+Language.getText(p,"KIT_SHOP_BUY"));
		switch(kit.getType()){
			case KAUFEN:
				inventory.setItem(3, UtilItem.RenameItem(new ItemStack(Material.GOLD_NUGGET), "§eCoins"));
				inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aGems"));
				inventory.setItem(1, kit.getItem());
				break;
			case KAUFEN_COINS:
				inventory.setItem(5, UtilItem.RenameItem(new ItemStack(Material.GOLD_NUGGET), "§eCoins"));
				inventory.setItem(1, kit.getItem());
				break;
			case KAUFEN_GEMS:
				inventory.setItem(5, UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aGems"));
				inventory.setItem(1, kit.getItem());
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
	Player p = (Player)ev.getWhoClicked();
		if(ev.getInventory().getName().equalsIgnoreCase(getName())){
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
					ev.setCancelled(true);
					
					if(ev.getCurrentItem().getType()==Material.IRON_DOOR||ev.getCurrentItem().getType()==Material.REDSTONE){
						p.closeInventory();
						getInv(p);
					}else if(ev.getCurrentItem().getType()==Material.EMERALD){
						kit.addPlayer(p);
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "KIT_SHOP_ADD",kit.getName()));
						p.closeInventory();
						UtilInv.remove(p, UtilItem.RenameItem(new ItemStack(Material.CHEST), "§bKitShop"), 1);
					}else if(ev.getCurrentItem().getType()==Material.GLOWSTONE_DUST){
						p.openInventory(getKaufen(kit,p));
					}
					break;
				}else if(String.valueOf(kit.getName()+" "+Language.getText(p,"KIT_SHOP_BUY")).equalsIgnoreCase(ev.getInventory().getName())){
					ev.setCancelled(true);
					p.closeInventory();
					
					if(ev.getCurrentItem().getType()==Material.GOLD_NUGGET){
						int c = getCoins().getCoins(p);
						if(c>=kit.getCoins_preis()){
							getCoins().delCoins(p, true, kit.getCoins_preis());
							getPermManager().addPermission(p, kit.getPermission());
							p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "KIT_SHOP_BUYED_KIT",kit.getName()));
						}else{
							p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "KIT_SHOP_NO_MONEY","Coins"));
						}
					}else if(ev.getCurrentItem().getType()==Material.EMERALD){
						int c = getGems().getGems(p);
						if(c>=kit.getGems_preis()){
							getGems().delGems(p, true, kit.getGems_preis());
							getPermManager().addPermission(p, kit.getPermission());
							p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "KIT_SHOP_BUYED_KIT",kit.getName()));
						}else{
							p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "KIT_SHOP_NO_MONEY","Gems"));
						}
					}
					break;
				}
			}
		}
	}
	
}
