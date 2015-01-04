package me.kingingo.kcore.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryBase extends kListener{
	
	@Getter
	private String name;
	@Getter
	ArrayList<InventoryPageBase> pages;
	@Getter
	ArrayList<InventoryPageBase> another;
	@Getter
	@Setter
	private InventoryPageBase main;
	@Getter
	private JavaPlugin instance;
	
	public InventoryBase(JavaPlugin instance,int main_size,String name){
		super(instance,name);
		this.name=name;
		this.pages= new ArrayList<>();
		this.another= new ArrayList<>();
		this.main=new InventoryPageBase(instance,main_size,name);
	}
	
	public InventoryBase(JavaPlugin instance,String name){
		super(instance,name);
		this.name=name;
		this.pages= new ArrayList<>();
		this.another= new ArrayList<>();
	}
	
	public void openShop(Player player){
		player.openInventory(main);
	}
	
	public InventoryPageBase addAnother(InventoryPageBase page){
		another.add(page);
		return page;
	}
	
	public InventoryPageBase addPage(InventoryPageBase page){
		pages.add(page);
		return page;
	}
	
	public InventoryPageBase get(Inventory inv){
		for(InventoryPageBase page : pages){
			if(page.getName().equalsIgnoreCase(inv.getName())&&page.getSize()==inv.getSize()){
				return page;
			}
		}
		
		for(InventoryPageBase page : another){
			if(page.getName().equalsIgnoreCase(inv.getName())&&page.getSize()==inv.getSize()){
				return page;
			}
		}
		
		if(main.getName().equalsIgnoreCase(inv.getName())&&main.getSize()==inv.getSize())return main;
		return null;
	}
	
	@EventHandler
	public void CloseInv(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_005)return;
		for(int i = 0; i < another.size(); i++){
			if(another.get(i).getViewers().isEmpty())another.remove(i);
		}
	}
	
	Player p;
	InventoryPageBase page;
	@EventHandler
	public void UseInv(InventoryClickEvent ev){
		if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;
			page=get(ev.getInventory());
			if(page!=null){
				ev.setCancelled(true);
				p=(Player)ev.getWhoClicked();
				if(ClickType.LEFT==ev.getClick())page.useButton(p, ActionType.L, ev.getCurrentItem());
				if(ClickType.RIGHT==ev.getClick())page.useButton(p, ActionType.R, ev.getCurrentItem());
			}
	}
	
}
