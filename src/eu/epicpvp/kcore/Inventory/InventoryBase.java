package eu.epicpvp.kcore.Inventory;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Inventory.Inventory.InventoryNextPage;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryTrade;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilMath;
import lombok.Getter;

public class InventoryBase extends kListener {
	InventoryNextPage n;
	Player p;
	InventoryPageBase page;
	InventoryTrade trade;

	@Getter
	private String name;
	@Getter
	ArrayList<InventoryPageBase> pages;
	@Getter
	ArrayList<InventoryPageBase> another;
	//@Getter
	//@Setter
	//private InventoryPageBase main;

	@Getter
	private JavaPlugin instance;

	public InventoryBase(JavaPlugin instance, int main_size, String name) {
		super(instance, (name == null ? "InventoryBase" + UtilMath.randomInteger(1000) : name));
		this.name = getModuleName();
		this.pages = new ArrayList<>();
		this.another = new ArrayList<>();
		//if(main_size!=0) this.main = new InventoryPageBase(main_size,name);

		UtilInv.setBase(this);
	}

	public InventoryBase(JavaPlugin instance, InventorySize size, String name) {
		this(instance, size.getSize(), name);
	}

	public InventoryBase(JavaPlugin instance, String name) {
		this(instance, 0, name);
	}

	public InventoryBase(JavaPlugin instance, int size) {
		this(instance, size, null);
	}

	public InventoryBase(JavaPlugin instance, InventorySize size) {
		this(instance, size.getSize());
	}

	public InventoryBase(JavaPlugin instance) {
		this(instance, 0, null);
	}

	//	public void openShop(Player player){
	//		player.openInventory(main);
	//	}

	public InventoryPageBase addAnother(InventoryPageBase page) {
		another.add(page);
		return page;
	}

	public InventoryPageBase addPage(InventoryPageBase page) {
		pages.add(page);
		return page;
	}

	public InventoryPageBase get(Inventory inv) {
		for (InventoryPageBase page : pages) {
			if (page != null) {
				if (page.getName().equalsIgnoreCase(inv.getName()) && page.getSize() == inv.getSize() && isSameInventory(page, inv)) {
					return page;
				}
			} else {
				UtilDebug.debug("get(Inventory)", "for-schleife InventoryPageBase == NULL");
			}
		}

		for (InventoryPageBase page : another) {
			if (page.getName().equalsIgnoreCase(inv.getName()) && page.getSize() == inv.getSize() && isSameInventory(page, inv)) {
				return page;
			}
		}
		//		if(main!=null&&main.getName()!=null&&inv.getName() != null
		//				&&main.getName().equalsIgnoreCase(inv.getName())&&main.getSize()==inv.getSize()&&isSameInventory(main,inv))return main;
		return null;
	}

	public static boolean isSameInventory(Inventory first, Inventory second) {
		return ((CraftInventory) first).getInventory().equals(((CraftInventory) second).getInventory());
	}

	@EventHandler
	public void CloseInv(UpdateEvent ev) {
		if (ev.getType() != UpdateType.MIN_005)
			return;
		for (int i = 0; i < another.size(); i++) {
			if (another.get(i).getViewers().isEmpty()) {
				if (another.get(i) instanceof InventoryNextPage) {
					n = (InventoryNextPage) another.get(i);
					if (n.getPlayer().getOpenInventory() != null)
						continue;
				}
				another.get(i).remove();
				another.remove(i);
			}
		}
	}

	@EventHandler
	public void UseInv(InventoryClickEvent ev) {
		if (!(ev.getWhoClicked() instanceof Player) || ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)
			return;
		page = get(ev.getInventory());

		if (page != null) {
			ev.setCancelled(true);
			p = (Player) ev.getWhoClicked();

			if (page instanceof InventoryTrade && ev.getCurrentItem() != null) {
				trade = (InventoryTrade) page;
				if (trade.putItem(p, ev.getClickedInventory(), ev.getCurrentItem(), ev.getSlot()))
					return;
			}
			
			switch(ev.getClick()){
			case LEFT:
			case RIGHT:
			case SHIFT_LEFT:
			case SHIFT_RIGHT:
				ev.setCancelled(page.useButton(p, UtilEvent.getActionType(ev.getClick()), ev.getCurrentItem(), ev.getSlot()));
				break;
			}
		}
	}
}
