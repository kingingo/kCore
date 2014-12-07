package me.kingingo.kcore.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Tokens;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryBase extends kListener{
	
	@Getter
	private Tokens tokens;
	@Getter
	private Coins coins;
	@Getter
	private String name;
	@Getter
	ArrayList<InventoryPageBase> pages;
	@Getter
	private InventoryPageBase main;
	@Getter
	private JavaPlugin instance;
	
	public InventoryBase(JavaPlugin instance,String name,Tokens tokens,Coins coins){
		super(instance,name);
		this.coins=coins;
		this.name=name;
		this.tokens=tokens;
		this.pages= new ArrayList<>();
		this.main=new InventoryPageBase(instance,27,name);
	}
	
	public void openShop(Player player){
		player.openInventory(main);
	}
	
	public void addPage(InventoryPageBase page){
		pages.add(page);
	}
	
	Player p;
	InventoryPageBase page;
	@EventHandler
	public void UseInv(InventoryClickEvent ev){
		if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;
		if(ev.getInventory() instanceof InventoryPageBase){
			page=(InventoryPageBase) ev.getInventory();
			if(getPages().contains(page)||main==page){
				if(ClickType.LEFT==ev.getClick())page.useButton(p, ActionType.L, ev.getCurrentItem());
				if(ClickType.RIGHT==ev.getClick())page.useButton(p, ActionType.R, ev.getCurrentItem());
			}
			ev.setCancelled(true);
		}
	}
	
}
