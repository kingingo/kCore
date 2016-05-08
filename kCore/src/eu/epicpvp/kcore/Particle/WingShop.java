package eu.epicpvp.kcore.Particle;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;

public class WingShop extends InventoryCopy implements Listener{

	@Getter
	private HashMap<Player,PlayerParticle> players;
	@Getter
	private JavaPlugin instance;
	
	public WingShop(JavaPlugin instance) {
		super(InventorySize._54.getSize(), "WingShop");
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.players=new HashMap<>();
		this.instance=instance;
		WingShape[] wings = new WingShape[]{
			new WingShape(UtilItem.RenameItem(new ItemStack(Material.SUGAR), "§7White Wings"),PermissionType.WINGS_WHITE,true, Color.WHITE, Color.WHITE, Color.WHITE)
		};
		
		int next = 0;
		for(int slot : UtilInv.getSlotsBorder(InventorySize.invSize(getSize()), InventorySplit._18)){
			if(wings.length<=next){
				setItem(slot, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " "));
			}else{
				addButton(slot, new WingShapeButton(slot, this, wings[next]));
			}
			next++;
		}
		
		fill(Material.STAINED_GLASS_PANE,7);
		UtilInv.getBase().addPage(this);
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev){
		players.remove(ev.getPlayer());
	}
}
