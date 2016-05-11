package eu.epicpvp.kcore.MysteryChest;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.MysteryChest.Items.MysteryItem;
import eu.epicpvp.kcore.MysteryChest.Templates.Building;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutBlockAction;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilBlock;
import eu.epicpvp.kcore.Util.UtilFirework;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilLocation;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Blocks;

public class MysteryChestSession {

	@Getter
	private Player player;
	private ItemStack[] playeritems;
	@Getter
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

	//OPENED CHESTS
	private ArrayList<Block> chests;
	
	public MysteryChestSession(Player player,Building building,MysteryItem[] items){
		this.player=player;
		this.items=items;
		this.location=player.getLocation();
		this.state=MysteryChestState.BUILDING;
		this.building_int=0;
		this.building=building;
		this.playeritems=player.getInventory().getContents().clone();
		this.player.getInventory().clear();
		
		this.blocks=new ArrayList<>();
		this.chests=new ArrayList<>();
		this.drops=new HashMap<>();
	}
	
	public void drop(Block block){
		if(item>=items.length)return;
		if(chests.contains(block))return;
		chests.add(block);
		MysteryItem drop=items[item];
		UtilFirework.start(block.getLocation(), Color.RED, Type.BURST);
		kPacketPlayOutBlockAction packet = new kPacketPlayOutBlockAction( (block.getType()==Material.CHEST ? Blocks.CHEST : Blocks.ENDER_CHEST) , block.getLocation(), 1);
		for(Player p : UtilServer.getPlayers())UtilPlayer.sendPacket(p, packet);
		Location loc = block.getLocation().clone().add(0.5D, 1.0D, 0.5D);
		
		Item it = block.getWorld().dropItem(loc, UtilItem.RenameItem(drop.clone(), "item"+UtilMath.r(100)));
		it.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
        it.setPickupDelay(1000*20);
        NameTagMessage msg = new NameTagMessage(NameTagType.PACKET, loc.clone().add(0,0.4,0), drop.getItemMeta().getDisplayName());
        msg.send();
        this.drops.put(it,msg);
        item++;
        
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rdm(drop.getCmd().replaceAll("[p]", player.getName())));
		
		if(item>=items.length){
			time=System.currentTimeMillis()-TimeSpan.SECOND*25;
			UtilPlayer.setMove(player, true);
		}
	}
	
	public String rdm(String cmd){
		if(cmd.contains("R,")){
			String[] split = cmd.split(" ");
			
			for(String s : split){
				if(s.startsWith("R,")){
					String[] ssplit = s.split(",");
				
					int min = UtilNumber.toInt(ssplit[1]);
					int max = UtilNumber.toInt(ssplit[2]);
					
					cmd.replaceAll("R,"+min+","+max, ""+UtilMath.RandomInt(max, min));
					break;
				}
			}
		}
		return cmd;
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
		if(this.drops!=null){
			if(!this.drops.isEmpty()){
				for(Item it : this.drops.keySet()){
					it.remove();
					this.drops.get(it).clear();
				}
				this.drops.clear();
			}
			this.drops=null;
		}
		if(chests!=null){
			chests.clear();
		}
		this.chests=null;
		this.blocks=null;
		this.playeritems=null;
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
				UtilPlayer.setMove(player, true);
				state=MysteryChestState.DELETE;
			}
			
			return true;
		case DELETE:
			
			if(!this.drops.isEmpty()){
				player.getInventory().setContents(this.playeritems);
				player.updateInventory();
				for(Item it : this.drops.keySet()){
					it.remove();
					this.drops.get(it).clear();
				}
				this.drops.clear();
			}
			
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
