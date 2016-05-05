package eu.epicpvp.kcore.MysteryChest;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.MysteryChest.Templates.Building;
import eu.epicpvp.kcore.MysteryChest.TreasureItems.MysteryItem;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutBlockAction;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilBlock;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Blocks;

public class MysteryChestSession {

	@Getter
	private Player player;
	private Location location;
	private MysteryItem[] items;
	private int item=0;
	private MysteryChestState state;
	
	//CHOOSE
	private long time;
	
	//BUILDING
	private int building_int;
	private Building building;
	private ArrayList<BlockState> blocks;
	
	//DROP
	private HashMap<Item,NameTagMessage> drops;
	
	public MysteryChestSession(Player player,Building building,MysteryItem[] items){
		this.player=player;
		this.items=items;
		this.location=player.getLocation();
		this.state=MysteryChestState.BUILDING;
		this.building_int=0;
		this.building=building;
		this.blocks=new ArrayList<>();
		this.drops=new HashMap<>();
	}
	
	public void drop(Block block){
		if(item>=items.length)return;
		MysteryItem drop=items[item];
		kPacketPlayOutBlockAction packet = new kPacketPlayOutBlockAction( (block.getType()==Material.CHEST ? Blocks.CHEST : Blocks.ENDER_CHEST) , block.getLocation(), 1);
		for(Player p : UtilServer.getPlayers())UtilPlayer.sendPacket(p, packet);
		Location loc = UtilBlock.getBlockCenterUP(block.getLocation());
		
		Item it = block.getWorld().dropItem(loc, UtilItem.RenameItem(drop.clone(), "item"+UtilMath.r(100)));
		it.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
        it.setPickupDelay(1000);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), drop.getCmd().replaceAll("{player}", player.getName()));
        NameTagMessage msg = new NameTagMessage(NameTagType.PACKET, loc.clone().add(0,0.4,0), drop.getItemMeta().getDisplayName());
        msg.sendToPlayer(player);
        this.drops.put(it,msg);
        item++;
	}
	
	public void remove(){
		this.player=null;
		this.location=null;
		this.state=null;
		this.building_int=0;
		this.building=null;
		if(this.blocks!=null){
			for(BlockState state : this.blocks)state.update(true);
			this.blocks.clear();
		}
		if(this.drops!=null)this.drops.clear();
		this.drops=null;
		this.blocks=null;
	}
	
	public boolean next(){
		if(!player.isOnline()){
			return false;
		}
		
		switch(state){
		case BUILDING:
			BlockState blockstate = building.nextBlock(location, building_int);
			building_int++;
			
			if(blockstate!=null){
				blocks.add(blockstate);
			}else{
				building.nextChest(location,blocks);
				time=System.currentTimeMillis();
				state=MysteryChestState.CHOOSE;
			}
			return true;
		case CHOOSE:
			
			if((System.currentTimeMillis() - time) > TimeSpan.SECOND*30){
				state=MysteryChestState.DELETE;
			}
			
			return true;
		case DELETE:
			
			if(!this.blocks.isEmpty()){
				this.blocks.get(0).update(true);
				this.blocks.remove(0);
				return true;
			}
			
			if(!this.drops.isEmpty()){
				for(Item it : this.drops.keySet()){
					it.remove();
					this.drops.get(it).clear(player);
				}
				this.drops.clear();
			}

			return false;
		}
		return false;
	}
}
