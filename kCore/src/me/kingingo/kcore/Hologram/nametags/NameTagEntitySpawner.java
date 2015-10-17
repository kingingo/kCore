package me.kingingo.kcore.Hologram.nametags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.Packets.kArmorStandDataWatcher;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityTeleport;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;

public class NameTagEntitySpawner {

	@Getter
	private HashMap<Integer,ArmorStand> armorStands;
	@Getter
	private double y = 1;
	private int nameTagCount;
	private Vector[] locations;
	
	public NameTagEntitySpawner(int nameTagCount) {
		this(nameTagCount,1);
	}
	
	public NameTagEntitySpawner(int nameTagCount, double y) {
		this.y=y;
		this.nameTagCount=nameTagCount;
		this.armorStands=new HashMap<>();
	}
	
	public void remove(){
		this.y=0;
		this.armorStands.clear();
		this.armorStands=null;
		this.locations=null;
	}
	
	public void moveNameTag(int index, Location location) {
		if(this.armorStands.containsKey(index)){
			location.setY(location.getY()-getY());
			this.armorStands.get(index).teleport( location );
			getLocations()[index] = location.toVector();
		}
	}
	
	public void setNameTag(int index, Location location, double dY, String message) {
		if(!armorStands.containsKey(index)){
			this.armorStands.put(index, createArmorStand(index, location, dY, message));
			getLocations()[index] = new Vector(location.getX(), location.getY() + dY,location.getZ());
		}
	}
	
	public Vector[] getLocations() {
		if (this.locations == null) {
			this.locations = new Vector[nameTagCount];
		}
		return this.locations;
	}
	
	public Location getLocation(int index,World world) {
		if (this.locations != null && this.locations[index] != null) {
			return this.locations[index].toLocation(world);
		}
		return null;
	}
	
	public void clearNameTags() {
		int[] indices = new int[nameTagCount];
		for (int i = 0; i < indices.length; i++) indices[i] = i;
		
		clearNameTags(indices);
	}
	
	public void clearNameTags(int... indices) {
		for(int i : indices){
			if(this.armorStands.containsKey(i)){
				this.armorStands.get(i).remove();
				this.armorStands.remove(i);
			}
		}
	}
	
	public ArmorStand createArmorStand(int index, Location location,double dY, String message) {
		location.setY(location.getY() + dY - getY());
		ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		armorStand.setCustomName(message);
		armorStand.setCustomNameVisible(true);
		armorStand.setVisible(false);
		armorStand.setGravity(false);
		armorStand.setArms(false);
		armorStand.setBasePlate(false);
		armorStand.setSmall(true);
		
		return armorStand;
	}
	
	public int getNameTagCount() {
		return nameTagCount;
	}
}
