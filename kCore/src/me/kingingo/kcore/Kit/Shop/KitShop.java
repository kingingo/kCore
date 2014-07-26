package me.kingingo.kcore.Kit.Shop;

import lombok.Getter;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Kit;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.PlayerStats.StatsManager;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.Tokens;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
	Kit[] kits;
	@Getter
	PermissionManager permManager;
	@Getter
	Coins coins;
	@Getter
	Tokens tokens;
	
	public KitShop(JavaPlugin instance,Coins coins,Tokens tokens,PermissionManager manager,String name,InventorySize size,Kit[] kits){
		this.name=name;
		this.kits=kits;
		this.coins=coins;
		this.tokens=tokens;
		this.permManager=manager;
		if(kits.length>size.getSize())size=InventorySize._45;
		this.inventory=Bukkit.createInventory(null, size.getSize(), getName());
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public Inventory getInv(Kit kit,Player p){
	Inventory inventory=Bukkit.createInventory(null, 9, kit.getName());
		switch(kit.getType()){
		case PREMIUM:
			if(getPermManager().hasPermission(p, kit.getPermission())){
				inventory.setItem(0, UtilItem.RenameItem(kit.getItem(), getName()));
				inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
				inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
				inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.IRON_DOOR_BLOCK), "§cZurück"));
			}else{
				inventory.setItem(0, UtilItem.RenameItem(kit.getItem(), getName()));
				inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
				inventory.setItem(7, UtilItem.Item(new ItemStack(Material.REDSTONE),new String[]{"§7Dieses Kit ist ein §aPremium-Kit","§eShop.EpicPvP.de"} ,"§cPremium-Kit"));
				inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.IRON_DOOR_BLOCK), "§cZurück"));
			}
			break;
		case STARTER:
			inventory.setItem(0, UtilItem.RenameItem(kit.getItem(), getName()));
			inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
			inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
			inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.IRON_DOOR_BLOCK), "§cZurück"));
			break;
		default:
			if(getPermManager().hasPermission(p, kit.getPermission())){
				inventory.setItem(0, UtilItem.RenameItem(kit.getItem(), getName()));
				inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
				inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.FIRE), "§aAuswählen"));
				inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.IRON_DOOR_BLOCK), "§cZurück"));
			}else{
				inventory.setItem(0, UtilItem.RenameItem(kit.getItem(), getName()));
				inventory.setItem(1, UtilItem.Item(new ItemStack(340), kit.getDescription(), getName()));
				inventory.setItem(7, UtilItem.RenameItem(new ItemStack(Material.GLOWSTONE_DUST) ,"§6Kaufen"));
				inventory.setItem(8, UtilItem.RenameItem(new ItemStack(Material.IRON_DOOR), "§cZurück"));
			}
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
				if(UtilItem.ItemNameEquals(ev.getCurrentItem(), kit.getItem())){
					getInv(kit, p);
				}
			}
		}else{
			for(Kit kit : getKits()){
				if(kit.getName().equalsIgnoreCase(ev.getInventory().getName())){
					Player p = (Player)ev.getWhoClicked();
					ev.setCancelled(true);
					
					if(ev.getCurrentItem().getType()==Material.IRON_DOOR||ev.getCurrentItem().getType()==Material.REDSTONE){
						p.closeInventory();
						p.openInventory(getInventory());
					}else if(ev.getCurrentItem().getType()==Material.FIRE){
						kit.addPlayer(p);
						p.sendMessage(Text.PREFIX.getText()+Text.KIT_SHOP_ADD.getText(kit.getName()));
						p.closeInventory();
						p.getInventory().remove(p.getItemInHand());
					}else if(ev.getCurrentItem().getType()==Material.GLOWSTONE_DUST){
						
					}
					break;
				}else if(String.valueOf(kit.getName()+" §aKaufen").equalsIgnoreCase(ev.getInventory().getName())){
					Player p = (Player)ev.getWhoClicked();
					ev.setCancelled(true);
					p.closeInventory();
					
					if(ev.getCurrentItem().getType()==Material.GOLD_NUGGET){
						int c = getCoins().getCoins(p);
						if(c<=kit.getPreis()){
							getCoins().delCoins(p, true, c);
							getPermManager().addPermission(p, kit.getPermission());
							p.sendMessage(Text.PREFIX+Text.KIT_SHOP_BUYED_KIT.getText(kit.getName()));
						}else{
							p.sendMessage(Text.PREFIX.getText()+Text.KIT_SHOP_NO_MONEY.getText("Coins"));
						}
					}else if(ev.getCurrentItem().getType()==Material.GOLD_INGOT){
						int c = getTokens().getTokens(p);
						if(c<=kit.getPreis()){
							getTokens().delTokens(p, true, c);
							getPermManager().addPermission(p, kit.getPermission());
							p.sendMessage(Text.PREFIX+Text.KIT_SHOP_BUYED_KIT.getText(kit.getName()));
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
