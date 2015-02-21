package me.kingingo.kcore.TreasureChest;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilParticle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
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
	private HashMap<Player,ArrayList<BlockState>> blocke = new HashMap<>();
	
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
		if(list.containsKey(ev.getPlayer())){
			list.remove(ev.getPlayer());
			for(BlockState state : blocke.get(ev.getPlayer()) ){
				state.update(true);
			}
			switch(getType()){
			case NETHER:
				UtilParticle.FLAME.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			case SNOW:
				UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			case END:
				UtilParticle.PORTAL.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			case SKY:
				UtilParticle.DRIP_LAVA.display(1F, 1F, 1F, 0.002F,100, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				UtilParticle.DRIP_WATER.display(1F, 1F, 1F, 0.002F, 100, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			default:
				UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			}
			blocke.remove(ev.getPlayer());
		}
	}
	
	public boolean Near(Location loc){
		for(Player player : blocke.keySet()){
			for(BlockState state : blocke.get(player)){
				if(state.getLocation().distance(loc)<8){
					return false;
				}
			}
		}
		return true;
	}
	
	@EventHandler
	public void BlockPlace(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.R_BLOCK)){
			if(list.containsKey(ev.getPlayer())&&UtilItem.ItemNameEquals(ev.getPlayer().getItemInHand(), getTreasurechest())){
				Location loc = ev.getClickedBlock().getLocation();
				if(Near(loc)){
				  ev.getPlayer().sendMessage(Text.PREFIX.getText()+Text.TREASURE_CHEST_TOO_NEAR.getText());
				  ev.setCancelled(true);
				  return;
				}
				Place(ev.getPlayer(),loc);
				list.get(ev.getPlayer()).onTreasureChest(ev.getPlayer(), loc);
				UtilInv.remove(ev.getPlayer(), getTreasurechest(), 1);
				loc=loc.add(0,1,-2);
				loc.setYaw((float) (((loc.getYaw() + 90)  * Math.PI) / 180));
				loc.setPitch((float) (((loc.getPitch() + 90) * Math.PI) / 180));
				ev.getPlayer().teleport(loc);
				ev.getPlayer().openInventory(list.get(ev.getPlayer()).getInventory());
			}
		}
	}
	
	public void setMaterial(Player player,Location loc, int x,int y,int z,Material[] m){
		Location l = new Location(loc.getWorld(),loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
		if(!blocke.containsKey(player))blocke.put(player, new ArrayList<BlockState>());
		blocke.get(player).add(l.getBlock().getState());
		l.getBlock().setType(m[UtilMath.r(m.length)]);
		UtilParticle.displayBlockCrack(l.getBlock().getTypeId(), l.getBlock().getData(), 1F, 1F, 1F, 20, l, 5.0);
	}
	
	public void setMaterial(Player player,Location loc, int x,int y,int z,Material m){
		Location l = new Location(loc.getWorld(),loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
		if(!blocke.containsKey(player))blocke.put(player, new ArrayList<BlockState>());
		blocke.get(player).add(l.getBlock().getState());
		l.getBlock().setType(m);
		UtilParticle.displayBlockCrack(l.getBlock().getTypeId(), l.getBlock().getData(), 1F, 1F, 1F, 20, l, 5.0);
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(list.containsKey(ev.getPlayer())){
			UtilInv.remove(ev.getPlayer(), getTreasurechest(), 1);
			list.remove(ev.getPlayer());
			for(BlockState state : blocke.get(ev.getPlayer()) ){
				state.update(true);
			}
			switch(getType()){
			case NETHER:
				UtilParticle.FLAME.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			case SNOW:
				UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			case END:
				UtilParticle.PORTAL.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			case SKY:
				UtilParticle.DRIP_LAVA.display(1F, 1F, 1F, 0.002F,100, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				UtilParticle.DRIP_WATER.display(1F, 1F, 1F, 0.002F, 100, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
			default:
				UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(ev.getPlayer()).get( 0 )).getLocation() , 5.0);
				break;
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
					UtilParticle.FLAME.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(p).get( 0 )).getLocation() , 5.0);
					break;
				case SNOW:
					UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(p).get( 0 )).getLocation() , 5.0);
					break;
				case END:
					UtilParticle.PORTAL.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(p).get( 0 )).getLocation() , 5.0);
					break;
				case SKY:
					UtilParticle.DRIP_LAVA.display(1F, 1F, 1F, 0.002F,100, ((BlockState)blocke.get(p).get( 0 )).getLocation() , 5.0);
					UtilParticle.DRIP_WATER.display(1F, 1F, 1F, 0.002F, 100, ((BlockState)blocke.get(p).get( 0 )).getLocation() , 5.0);
					break;
				default:
					UtilParticle.FIREWORKS_SPARK.display(1F, 1F, 1F, 0.002F, 200, ((BlockState)blocke.get(p).get( 0 )).getLocation() , 5.0);
					break;
				}
			}
		}
	}
	
	public void Place(Player player,Location loc){
		setMaterial(player, loc, 0, 1, 0, getType().getItem());
		for(int x = -2; x <= 2; x++){
			for(int z = -2; z <= 2; z++){
				setMaterial(player, loc, x, 0, z, getType().getBlockType());
			}
		}

		if(UtilMath.RandomInt(6, 1) < 4){
			setMaterial(player, loc, -2, 1, -2, getType().getBlockType());
			setMaterial(player, loc, -2, 2, -2, getType().getBlockType());
		}
		setMaterial(player, loc, -1, 1, -2, getType().getBlockType());
		setMaterial(player, loc, -2, 1, -1, getType().getBlockType());
		
		if(UtilMath.RandomInt(6, 1) < 3){
		setMaterial(player, loc, 2, 1, 2, getType().getBlockType());
		setMaterial(player, loc, 2, 2, 2, getType().getBlockType());
		}
		setMaterial(player, loc, 1, 1, 2, getType().getBlockType());
		setMaterial(player, loc, 2, 1, 1, getType().getBlockType());
		
		if(UtilMath.RandomInt(6, 1) < 4){
		setMaterial(player, loc, -2, 1, 2, getType().getBlockType());
		setMaterial(player, loc, -2, 2, 2, getType().getBlockType());
		}
		setMaterial(player, loc, -1, 1, 2, getType().getBlockType());
		setMaterial(player, loc, -2, 1, 1, getType().getBlockType());
		
		if(UtilMath.RandomInt(6, 1) < 3){
		setMaterial(player, loc, 2, 1, -2, getType().getBlockType());
		setMaterial(player, loc, 2, 2, -2, getType().getBlockType());
		}
		setMaterial(player, loc, 1, 1, -2, getType().getBlockType());
		setMaterial(player, loc, 2, 1, -1, getType().getBlockType());
	
	}
}