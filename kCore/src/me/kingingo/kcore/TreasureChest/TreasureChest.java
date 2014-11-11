package me.kingingo.kcore.TreasureChest;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilParticle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureChest extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private TreasureChestType type;
	@Getter
	private ItemStack treasurechest;
	private HashMap<Player,TreasureChestHandler> list = new HashMap<>();
	private HashMap<Player,HashMap<Location,Material>> blocke = new HashMap<>();
	
	public interface TreasureChestHandler {
        public void onTreasureChest(Player player,Location loc);
        public Inventory getInventory();
    }
	
	public TreasureChest(JavaPlugin instance,TreasureChestType type){
		super(instance,"[TreasureChest]");
		this.instance=instance;
		this.type=type;
		this.treasurechest=UtilItem.RenameItem(new ItemStack(Material.CHEST), "§bTreasureChest");
	}
	
	public void give(Player player,TreasureChestHandler handler){
		list.put(player, handler);
		player.getInventory().addItem(getTreasurechest().clone());
	}
	
	@EventHandler
	public void CloseInv(InventoryCloseEvent ev){
		if(list.containsKey(ev.getPlayer())&&ev.getInventory()==list.get(ev.getPlayer()).getInventory()){
			list.remove(ev.getPlayer());
			for(Location loc : blocke.get(ev.getPlayer()).keySet() ){
				loc.getBlock().setType(blocke.get(ev.getPlayer()).get(loc));
			}
			blocke.remove(ev.getPlayer());
		}
	}
	
	@EventHandler
	public void BlockPlace(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.R_BLOCK)){
			if(list.containsKey(ev.getPlayer())&&UtilItem.ItemNameEquals(ev.getPlayer().getItemInHand(), getTreasurechest())){
				Location loc = ev.getClickedBlock().getLocation();
				Place(ev.getPlayer(),loc);
				list.get(ev.getPlayer()).onTreasureChest(ev.getPlayer(), loc);
				ev.getPlayer().openInventory(list.get(ev.getPlayer()).getInventory());
			}
		}
	}
	
	public void setMaterial(Player player,Location loc, int x,int y,int z,Material m){
		Location l = new Location(loc.getWorld(),loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
		if(!blocke.containsKey(player))blocke.put(player, new HashMap<Location,Material>());
		blocke.get(player).put(l,l.getBlock().getType());
		l.getBlock().setType(m);;
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(list.containsKey(ev.getPlayer())){
			UtilInv.remove(ev.getPlayer(), getTreasurechest(), 1);
			list.remove(ev.getPlayer());
			for(Location loc : blocke.get(ev.getPlayer()).keySet() ){
				loc.getBlock().setType(blocke.get(ev.getPlayer()).get(loc));
			}
			blocke.remove(ev.getPlayer());
		}
	}
	
	@EventHandler
	public void Effect(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_2)return;
		if(blocke.isEmpty())return;
		for(Player p : blocke.keySet()){
			if(!blocke.get(p).isEmpty()){
				switch(getType()){
				case NETHER:
					UtilParticle.FLAME.display(1F, 1F, 1F, -5F, 200, (Location)blocke.get(p).keySet().toArray()[blocke.get(p).keySet().size()], 5.0);
					break;
				case SNOW:
					UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, -5F, 200, (Location)blocke.get(p).keySet().toArray()[blocke.get(p).keySet().size()], 5.0);
					break;
				case END:
					UtilParticle.PORTAL.display(1F, 1F, 1F, -5F, 200, (Location)blocke.get(p).keySet().toArray()[blocke.get(p).keySet().size()], 5.0);
					break;
				case GRASS:
					UtilParticle.DRIP_LAVA.display(1F, 1F, 1F, -5F, 200, (Location)blocke.get(p).keySet().toArray()[blocke.get(p).keySet().size()], 5.0);
					UtilParticle.DRIP_WATER.display(1F, 1F, 1F, -5F, 200, (Location)blocke.get(p).keySet().toArray()[blocke.get(p).keySet().size()], 5.0);
					break;
				default:
					UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, -5F, 200, (Location)blocke.get(p).keySet().toArray()[blocke.get(p).keySet().size()], 5.0);
					break;
				}
			}
		}
	}
	
	public void Place(Player player,Location loc){
		setMaterial(player,loc, 1, 0, 0, getType().getBlockType());
		setMaterial(player,loc, -1, 0, 0, getType().getBlockType());
		setMaterial(player,loc, 0, 0, 1, getType().getBlockType());
		setMaterial(player,loc, 0, 0, -1, getType().getBlockType());
		setMaterial(player,loc, 1, 0, 1, getType().getBlockType());
		setMaterial(player,loc, 1, 0, -1, getType().getBlockType());
		setMaterial(player,loc, 1, 0, 1, getType().getBlockType());
		setMaterial(player,loc, -1, 0, -1, getType().getBlockType());
		setMaterial(player,loc, 1, 1, 1, Material.FENCE);
		setMaterial(player,loc, 1, 1, -1, Material.FENCE);
		setMaterial(player,loc, -1, 1, 1, Material.FENCE);
		setMaterial(player,loc, -1, 1, -1, Material.FENCE);
	}
}