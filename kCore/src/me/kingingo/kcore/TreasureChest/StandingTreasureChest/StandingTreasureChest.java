package me.kingingo.kcore.TreasureChest.StandingTreasureChest;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilParticle;

public class StandingTreasureChest extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private Player player; //Der Spieler der momentan dran ist
	@Getter
	private String chest_type; // Den TreasureChest Type ( UNCOMMON,RARE,MYTHICAL)
	private long time; //WAIT TIME
	@Getter
	private String status = "NULL"; // Was er als nächstes tun muss ( SET CHEST, WAIT, DELETE)
	@Getter
	private ArrayList<ItemStack> uncommen; // UNCOMMON ITEMS
	@Getter
	private ArrayList<ItemStack> rare; // RARE ITEMS
	@Getter
	private ArrayList<ItemStack> mythical; // MYTHICAL ITEMS
	@Getter
	private HashMap<Block,ItemStack> list; // CHEST LIST
	@Getter
	private Location location; // LOCATION WO DER SPIELER STEHT
	@Getter
	private Hologram hologram; // HOLOGRAMM MANAGER
	@Getter
	private InventoryBase base; // WAHL MENÜ
	private ArrayList<Entity> dropped_items;
	
	public StandingTreasureChest(JavaPlugin instance,Hologram hologram,Location location) {
		super(instance, "StandingTreasureChest");
		this.instance=instance;
		this.location=location;
		this.hologram=hologram;
		this.uncommen=new ArrayList<>();
		this.rare=new ArrayList<>();
		this.mythical=new ArrayList<>();
		this.list=new HashMap<>();
		
		//--- SUCHT ALLE CHEST ZUSAMMEN ---
		list.put(location.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST),null); 
		list.put(location.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST),null);
		
		list.put(location.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH),null); 
		list.put(location.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH),null);

		list.put(location.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH),null);
		list.put(location.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH),null); 

		list.put(location.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST),null);
		list.put(location.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST),null);
		//--- ENDE ---
		
		//ENTFERNT FALLS CHEST NOCH DA SIND
		for(Block b : list.keySet())b.setType(Material.AIR);
		
		//SETZT DIE WAHL CHEST
		this.location.getBlock().setType(Material.CHEST);
		
		//ERSTELLT DAS WAHL MENÜ
		this.base=new InventoryBase(getInstance(), InventorySize._9.getSize(), "TreasureChest: ");
		this.base.getMain().addButton(2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				chest_type="uncommon";
				start(player);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.CHEST), "Uncommon TreasureChest")));
		this.base.getMain().addButton(4, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				chest_type="rare";
				start(player);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.CHEST), "Rare TreasureChest")));
		this.base.getMain().addButton(6, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				chest_type="mythical";
				start(player);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.ENDER_CHEST), "Mythical TreasureChest")));
	}
	
	public void start(Player player){
		for(HumanEntity p : this.base.getMain().getViewers())p.closeInventory();
		this.location.getBlock().setType(Material.AIR);
		this.player=player;
		this.status="SET CHEST";
	}
	
	Block b;
	int i=0;
	@EventHandler
	public void UPDATE(UpdateEvent ev){
		if(player != null){
			if(status.equalsIgnoreCase("SET CHEST")){
				if(ev.getType() == UpdateType.SEC){
					if(i == list.size()){
						this.status="WAIT";
						this.time=System.currentTimeMillis()+(TimeSpan.MINUTE*2);
						i=0;
						return;
					}
					
					if(chest_type.equalsIgnoreCase("mythical")){
						UtilParticle.PORTAL.display(1F, 1F, 1F, 0.002F, 200, ((Block)list.keySet().toArray()[i]).getLocation() , 5.0);
						b=((Block)list.keySet().toArray()[i]);
						b.setType(Material.ENDER_CHEST);
						list.remove(i);
						list.put(b, rdmItemStack());
					}else{
						if(chest_type.equalsIgnoreCase("uncommon")){
							UtilParticle.LARGE_SMOKE.display(1F, 1F, 1F, 0.002F, 200, ((Block)list.keySet().toArray()[i]).getLocation() , 5.0);
						}else if(chest_type.equalsIgnoreCase("rare")){
							UtilParticle.LAVA.display(1F, 1F, 1F, 0.002F, 200, ((Block)list.keySet().toArray()[i]).getLocation() , 5.0);
						}
						b=((Block)list.keySet().toArray()[i]);
						b.setType(Material.CHEST);
						list.remove(i);
						list.put(b, rdmItemStack());
						i++;
					}
				}
			}else if(status.equalsIgnoreCase("WAIT")){
				if(ev.getType() == UpdateType.SEC_3){
					if(System.currentTimeMillis() > time){
						this.player.sendMessage(Text.PREFIX.getText()+Text.TREASURE_CHEST_TIME_AWAY.getText());
						reset();
					}
				}
			}
		}
	}
	
	public ItemStack rdmItemStack(){
		ItemStack item = null;
		
		if(chest_type.equalsIgnoreCase("uncommon")){
			item=this.uncommen.get( UtilMath.r(this.uncommen.size()) );
		}else if(chest_type.equalsIgnoreCase("rare")){
			item=this.rare.get( UtilMath.r(this.rare.size()) );
		}else if(chest_type.equalsIgnoreCase("mythical")){
			item=this.mythical.get( UtilMath.r(this.mythical.size()) );
		}else{
			item=new ItemStack(Material.BEDROCK);
		}
		
		return item;
	}
	
	public void reset(){
		this.time=0;
		this.status=null;
		this.chest_type=null;
		this.player=null;
		for(Block b : list.keySet())b.setType(Material.AIR);
		this.location.getBlock().setType(Material.CHEST);
		for(Entity e : dropped_items)e.remove();
		dropped_items.clear();
	}
	
	@EventHandler
	public void pickedup(PlayerPickupItemEvent ev){
		if(!this.dropped_items.isEmpty()){
			for(Entity e : dropped_items){
				if(e.getEntityId() == ev.getItem().getEntityId()){
					ev.setCancelled(true);
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void Interact(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.R_BLOCK)){
			if(ev.getClickedBlock().getLocation() == location){
				ev.getPlayer().openInventory(this.base.getMain());
			}else if(this.player!=null){
				if(this.player.getName().equalsIgnoreCase(ev.getPlayer().getName())){
					if(this.status.equalsIgnoreCase("WAIT")){
						if(ev.getClickedBlock().getType()==Material.ENDER_CHEST||ev.getClickedBlock().getType()==Material.CHEST){
							for(Block b : list.keySet()){
								if(b.getLocation() == ev.getClickedBlock().getLocation()){
									drop(b.getLocation().add(0, 2, 0), list.get(b),ev.getPlayer());
								}
							}
						}
					}
				}
			}
		}
	}

	public void drop(Location loc,ItemStack drop,Player player){
		Bukkit.getPluginManager().callEvent(new TreasureChestWinItemEvent(getPlayer(), drop));
		dropped_items.add(loc.getWorld().dropItemNaturally(loc, drop));
		if(drop.hasItemMeta()&&drop.getItemMeta().hasDisplayName())hologram.sendText(player, loc.add(0, 0.5, 0), drop.getItemMeta().getDisplayName());
	}
	
	
}
