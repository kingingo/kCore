package eu.epicpvp.kcore.TreasureChest.StandingTreasureChest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Hologram.Hologram;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutBlockAction;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilBlock;
import eu.epicpvp.kcore.Util.UtilDirection;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilLocation;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilWorld;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Blocks;

public class StandingTreasureChest extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private Player player; //Der Spieler der momentan dran ist
	@Getter
	private TreasureChestType chest_type = TreasureChestType.UNCOMMON; // Den TreasureChest Type ( UNCOMMON,RARE,MYTHICAL)
	private long time; //WAIT TIME
	@Getter
	private String status = "NULL"; // Was er als n§chstes tun muss ( SET CHEST, WAIT, DELETE)
	@Getter
	private HashMap<TreasureChestType,ArrayList<TreasureChestPackage>> itemList; // ITEM LIST
	@Getter
	private HashMap<Block,TreasureChestPackage> list; // CHEST LIST
	@Getter
	private HashMap<Block,BlockFace> blockFaces; // CHEST LIST
	@Getter
	private Location location; // LOCATION WO DER SPIELER STEHT
	@Getter
	private Hologram hologram; // HOLOGRAMM MANAGER
	@Getter
	private InventoryBase base; // WAHL MEN§
	private HashMap<Entity,NameTagMessage> dropped_items;
	
	public StandingTreasureChest(JavaPlugin instance,Hologram hologram,Location location) {
		super(instance, "StandingTreasureChest");
		this.instance=instance;
		this.location=location;
		this.hologram=hologram;
		this.dropped_items=new HashMap<>();
		this.list=new HashMap<>();
		this.blockFaces=new HashMap<>();
		
		//--- SUCHT ALLE CHEST ZUSAMMEN ---
		list.put(location.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST),null);
		blockFaces.put(location.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST), UtilDirection.SOUTH.getBlockFace());
		list.put(location.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST),null);
		blockFaces.put(location.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST), UtilDirection.SOUTH.getBlockFace());
		
		list.put(location.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH),null); 
		blockFaces.put(location.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH), UtilDirection.WEST.getBlockFace());
		list.put(location.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH),null);
		blockFaces.put(location.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH), UtilDirection.WEST.getBlockFace());

		list.put(location.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH),null);
		blockFaces.put(location.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH), UtilDirection.EAST.getBlockFace());
		list.put(location.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH),null); 
		blockFaces.put(location.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH), UtilDirection.EAST.getBlockFace());

		list.put(location.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST),null);
		blockFaces.put(location.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST), UtilDirection.NORTH.getBlockFace());
		list.put(location.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST),null);
		blockFaces.put(location.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST), UtilDirection.NORTH.getBlockFace());
		//--- ENDE ---
		
		//ENTFERNT FALLS CHEST NOCH DA SIND
		for(Block b : list.keySet())b.setType(Material.AIR);
		
		//SETZT DIE WAHL CHEST
		this.location.getBlock().setType(Material.CHEST);
		
		//ERSTELLT DAS WAHL MEN§
		this.base=new InventoryBase(getInstance(), InventorySize._9.getSize(), "TreasureChest: ");
		this.base.getMain().addButton(2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				chest_type=TreasureChestType.UNCOMMON;
				start(player);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.CHEST), "Uncommon TreasureChest")));
		this.base.getMain().addButton(4, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				chest_type=TreasureChestType.RARE;
				start(player);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.CHEST), "Rare TreasureChest")));
		this.base.getMain().addButton(6, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				chest_type=TreasureChestType.MYTHICAL;
				start(player);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.ENDER_CHEST), "Mythical TreasureChest")));
		this.base.getMain().fill(Material.STAINED_GLASS_PANE,7);
	}
	
	public void start(Player player){
		player.closeInventory();
		if(!this.base.getMain().getViewers().isEmpty())for(HumanEntity p : this.base.getMain().getViewers())if(!p.getName().equalsIgnoreCase(player.getName()))p.closeInventory();
		this.location.getBlock().setType(Material.AIR);
		player.teleport(location);
		this.player=player;
		filter();
		this.status="SET CHEST";
		UtilWorld.setWorldBorderCenter(player, location.getX(), location.getZ(), location.getZ());
		UtilWorld.setWorldBorderSize(player, 8);
		UtilWorld.setWorldBorderWarningTime(player, 60*60);
		
	}
	
	public void filter(){
//		this.itemList=(HashMap<TreasureChestType,ArrayList<TreasureChestPackage>>)UtilItem.treasureChestItemList().clone();
//		for(TreasureChestType type : UtilItem.treasureChestItemList().keySet()){
//			for(TreasureChestPackage p : UtilItem.treasureChestItemList().get(type)){
//				if(p.hasPlayer(getPlayer())){
//					this.itemList.get(type).remove(p);
//				}
//			}
//		}
	}
	
	Block b;
	int i=0;
	List<Player> l;
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
					
					if(chest_type==TreasureChestType.MYTHICAL){
						UtilParticle.PORTAL.display(0F, 0F, 0F, 0.002F, 200, ((Block)list.keySet().toArray()[i]).getLocation() , 5.0);
						b=((Block)list.keySet().toArray()[i]);
						b.setType(Material.ENDER_CHEST);
						UtilBlock.setChestFace(b, blockFaces.get(b));
						list.remove(i);
						list.put(b, rdmItemStack());
						i++;
					}else{
						if(chest_type==TreasureChestType.UNCOMMON){
							UtilParticle.LARGE_SMOKE.display(0F, 0F, 0F, 0.002F, 200, ((Block)list.keySet().toArray()[i]).getLocation() , 5.0);
						}else if(chest_type==TreasureChestType.RARE){
							UtilParticle.LAVA.display(0F, 0F, 0F, 0.002F, 200, ((Block)list.keySet().toArray()[i]).getLocation() , 5.0);
						}
						b=((Block)list.keySet().toArray()[i]);
						b.setType(Material.CHEST);
						UtilBlock.setChestFace(b, blockFaces.get(b));
						
						list.remove(i);
						list.put(b, rdmItemStack());
						i++;
					}
				}
			}else if(status.equalsIgnoreCase("WAIT")){
				if(ev.getType() == UpdateType.SEC_3){
					if(System.currentTimeMillis() > time){
						this.player.sendMessage(Language.getText(this.player, "PREFIX")+Language.getText(this.player, "TREASURE_CHEST_TIME_AWAY"));
						reset();
					}
				}
			}else{
				if(ev.getType()==UpdateType.SEC_2){
					if(i==0){
						reset();
					}else{
						i--;

						if(i==4){
							for(Block b : list.keySet()){
								if(list.get(b)!=null)b.setType(Material.AIR);
							}
						}
					}
				}
			}
		}
	}
	
	Location from;
	Location to;
	double x;
	double z;
	@EventHandler
	public void Update(PlayerMoveEvent ev){
		if(this.player!=null&&ev.getPlayer().getName().equalsIgnoreCase(this.player.getName())){
			from = ev.getFrom();
			to = ev.getTo();
			x = Math.floor(from.getX());
			z = Math.floor(from.getZ());
			if(Math.floor(to.getX())!=x||Math.floor(to.getZ())!=z){
			    x+=.5;
			    z+=.5;
			    ev.getPlayer().teleport(new Location(from.getWorld(),x,from.getY(),z,from.getYaw(),from.getPitch()));
			}
		}
	}
	
	public TreasureChestPackage rdmItemStack(){
		TreasureChestType r = TreasureChestType.rdm(chest_type);
		return itemList.get(r).get( UtilMath.r(itemList.get(r).size()) );
	}
	
	public void reset(){
		this.time=0;
		this.status=null;
		this.chest_type=null;
		for(Block b : list.keySet())b.setType(Material.AIR);
		this.location.getBlock().setType(Material.CHEST);
		for(Entity e : dropped_items.keySet()){
			e.remove();
			if(dropped_items.get(e)!=null)dropped_items.get(e).clear(this.player);
		}
		dropped_items.clear();
		UtilWorld.resetWorldBoarder(player);
		for(TreasureChestType type : itemList.keySet())itemList.get(type).clear();
		itemList.clear();
		itemList=null;
		this.player=null;
	}
	
	@EventHandler
	public void pickedup(PlayerPickupItemEvent ev){
		if(dropped_items!=null&&!this.dropped_items.isEmpty()){
			for(Entity e : dropped_items.keySet()){
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
			if(UtilLocation.isSameLocation(ev.getClickedBlock().getLocation(), location)){
				ev.getPlayer().openInventory(this.base.getMain());
				ev.setCancelled(true);
			}else if(this.player!=null){
				if(this.player.getName().equalsIgnoreCase(ev.getPlayer().getName())){
						if(ev.getClickedBlock().getType()==Material.ENDER_CHEST||ev.getClickedBlock().getType()==Material.CHEST){
							for(int i = 0 ; i<list.size() ; i++){
								b=(Block)list.keySet().toArray()[i];
								if(ev.getClickedBlock().getLocation().getBlockX()==b.getLocation().getBlockX()&&ev.getClickedBlock().getLocation().getBlockY()==b.getLocation().getBlockY()&&ev.getClickedBlock().getLocation().getBlockZ()==b.getLocation().getBlockZ()){
									if(this.status.equalsIgnoreCase("WAIT")&&list.get(b)!=null){
										drop(b, list.get(b).getItemStack(),ev.getPlayer());
										list.get(b).click(getPlayer());
									}
									ev.setCancelled(true);
								}
							}
						}
				}
			}
		}
	}

	public void drop(Block block,ItemStack drop,Player player){
		Bukkit.getPluginManager().callEvent(new TreasureChestWinItemEvent(getPlayer(), drop));
		kPacketPlayOutBlockAction packet = new kPacketPlayOutBlockAction( (block.getType()==Material.CHEST ? Blocks.CHEST : Blocks.ENDER_CHEST) , block.getLocation(), 1);
		for(Player p : UtilServer.getPlayers())UtilPlayer.sendPacket(p, packet);
		Location loc = UtilBlock.getBlockCenterUP(block.getLocation());
		
		Item it = block.getWorld().dropItem(loc, UtilItem.RenameItem(drop.clone(), "item"+UtilMath.r(100)));
		it.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
        it.setPickupDelay(1000);
		
		if(hologram!=null&&drop.hasItemMeta()&&drop.getItemMeta().hasDisplayName()){
			dropped_items.put(it,hologram.sendText(player, loc, drop.getItemMeta().getDisplayName()));
		}else{
			dropped_items.put(it,null);
		}
		if(dropped_items.size()==4){
			i=5;
			status="END";
		}
		list.remove(block);
		list.put(block, null);
	}
	
	
}
