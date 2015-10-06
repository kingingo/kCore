package me.kingingo.kcore.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.Inventory.InventoryTrade;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilDebug;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
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
		this.main=new InventoryPageBase(main_size,name);
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
			if(page!=null){
				if(page.getName().equalsIgnoreCase(inv.getName())&&page.getSize()==inv.getSize()&&isSameInventory(page,inv)){
					return page;
				}
			}else{
				UtilDebug.debug("get(Inventory)", "for-schleife InventoryPageBase == NULL");
			}
		}
		
		for(InventoryPageBase page : another){
			if(page.getName().equalsIgnoreCase(inv.getName())&&page.getSize()==inv.getSize()&&isSameInventory(page,inv)){
				return page;
			}
		}
		if(main!=null&&main.getName()!=null&&inv.getName()!=null
				&&main.getName().equalsIgnoreCase(inv.getName())&&main.getSize()==inv.getSize()&&isSameInventory(main,inv))return main;
		return null;
	}
	
	public static boolean isSameInventory(Inventory first, Inventory second){
	    return ((CraftInventory) first).getInventory().equals(((CraftInventory) second).getInventory());
	}
	
	@EventHandler
	public void CloseInv(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_005)return;
		for(int i = 0; i < another.size(); i++){
			if(another.get(i).getViewers().isEmpty()){
				another.get(i).remove();
				another.remove(i);
			}
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
				
				if(page instanceof InventoryTrade&&ev.getCurrentItem()!=null){
					InventoryTrade trade = (InventoryTrade)page;
					if(trade.putItem(p, ev.getClickedInventory(), ev.getCurrentItem(), ev.getSlot()))return;
				}
				
				if(ClickType.LEFT==ev.getClick()){
					ev.setCancelled(page.useButton(p, ActionType.L, ev.getCurrentItem(),ev.getSlot()));
				}else if(ClickType.RIGHT==ev.getClick()){
					ev.setCancelled(page.useButton(p, ActionType.R, ev.getCurrentItem(),ev.getSlot()));
				}
			}
	}
	
}
