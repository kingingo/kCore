package me.kingingo.kcore.TreasureChest.NEW;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutBlockAction;
import me.kingingo.kcore.TreasureChest.NEW.Templates.Building;
import me.kingingo.kcore.TreasureChest.NEW.TreasureItems.TreasureItem;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilBlock;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.Blocks;

public class TreasureChestSession {

	@Getter
	private Player player;
	private Location location;
	private TreasureItem[] items;
	private int item=0;
	private TreasureChestState state;
	
	//CHOOSE
	private long time;
	
	//BUILDING
	private int building_int;
	private Building building;
	private ArrayList<BlockState> blocks;
	
	public TreasureChestSession(Player player,Building building,TreasureItem[] items){
		this.player=player;
		this.items=items;
		this.location=player.getLocation();
		this.state=TreasureChestState.BUILDING;
		this.building_int=0;
		this.building=building;
		this.blocks=new ArrayList<>();
	}
	
	public void drop(Block block){
		if(item==4)return;
		TreasureItem drop=items[item];
		kPacketPlayOutBlockAction packet = new kPacketPlayOutBlockAction( (block.getType()==Material.CHEST ? Blocks.CHEST : Blocks.ENDER_CHEST) , block.getLocation(), 1);
		for(Player p : UtilServer.getPlayers())UtilPlayer.sendPacket(p, packet);
		Location loc = UtilBlock.getBlockCenterUP(block.getLocation());
		
		Item it = block.getWorld().dropItem(loc, UtilItem.RenameItem(drop.clone(), "item"+UtilMath.r(100)));
		it.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
        it.setPickupDelay(1000);
        player.getInventory().addItem(drop);
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
		this.blocks=null;
		System.out.println("REMOVE");
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
				state=TreasureChestState.CHOOSE;
			}
			return true;
		case CHOOSE:
			
			if((System.currentTimeMillis() - time) > TimeSpan.SECOND*30){
				state=TreasureChestState.DELETE;
			}
			
			return true;
		case DELETE:
			
			if(!this.blocks.isEmpty()){
				this.blocks.get(0).update(true);
				this.blocks.remove(0);
				return true;
			}

			return false;
		}
		return false;
	}
}
