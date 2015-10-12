package me.kingingo.kcore.Hologram.nametags;

import java.util.Map;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.Packets.kArmorStandDataWatcher;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityTeleport;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;

public class NameTagPacketSpawner {
	private static int SHARED_ENTITY_ID = Short.MAX_VALUE;

	@Getter
	private kPacketPlayOutSpawnEntityLiving armorStand;
	@Getter
	private double y = 1;
	private int startEntityId;
	private int nameTagCount;
	private Map<Player, Vector[]> playerLocations = new MapMaker().weakKeys().makeMap();
	
	public NameTagPacketSpawner(int nameTagCount) {
		this(nameTagCount,1);
	}
	
	public NameTagPacketSpawner(int nameTagCount, double y) {
		this.nameTagCount=nameTagCount;
		this.y=y;
		this.startEntityId=SHARED_ENTITY_ID;
		SHARED_ENTITY_ID += nameTagCount * 2;
	}
	
	public void remove(){
		this.y=0;
		this.nameTagCount=0;
		this.startEntityId=0;
		this.armorStand=null;
		this.playerLocations.clear();
		this.playerLocations=null;
	}
	
	public void moveNameTag(int index, Player observer, Location location) {
		kPacketPlayOutEntityTeleport teleport = new kPacketPlayOutEntityTeleport();
		teleport.setEntityID(getArmorStandId(index));
		teleport.setX(location.getX());
		teleport.setY(location.getY()-getY());
		teleport.setZ(location.getZ());
		
		UtilPlayer.sendPacket(observer, teleport);
		getLocations(observer)[index] = location.toVector();
	}
	
	public void setNameTag(int index, Player observer, Location location, double dY, String message) {
		createArmorStand(index, location, dY, message);
		UtilPlayer.sendPacket(observer, this.armorStand);
		getLocations(observer)[index] = new Vector(location.getX(), location.getY() + dY,location.getZ());
	}
	
	public Vector[] getLocations(Player player) {
		Vector[] result = playerLocations.get(player);

		if (result == null) {
			result = new Vector[nameTagCount];
			playerLocations.put(player, result);
		}
		return result;
	}
	
	public Location getLocation(int index, Player observer) {
		Vector[] locations = playerLocations.get(observer);

		if (locations != null && locations[index] != null) {
			return locations[index].toLocation(observer.getWorld());
		}
		return null;
	}
	
	public void clearNameTags(Player observer) {
		int[] indices = new int[nameTagCount];

		for (int i = 0; i < indices.length; i++)
			indices[i] = i;
		clearNameTags(observer, indices);
	}
	
	public void clearNameTags(Player observer, int... indices) {
		kPacketPlayOutEntityDestroy destroy = new kPacketPlayOutEntityDestroy();
		int[] ids = new int[indices.length * 2];

		for (int i = 0; i < indices.length; i++) {
			Preconditions.checkPositionIndex(indices[i], nameTagCount, "indices");
			ids[i * 2 + 1] = getArmorStandId(indices[i]);
		}
		destroy.setIDs(ids);
		UtilPlayer.sendPacket(observer, destroy);
	}
	
	public void createArmorStand(int index, Location location,double dY, String message) {
		this.armorStand = new kPacketPlayOutSpawnEntityLiving(getArmorStandId(index), EntityType.ARMOR_STAND, location);
		this.armorStand.setY(location.getY() + dY - getY());
		
		kArmorStandDataWatcher watcher = new kArmorStandDataWatcher(location.getWorld());
		watcher.setCustomName(message);
		watcher.setCustomNameVisible(true);
		watcher.setVisible(false);
		watcher.setBasePlate(false);
		watcher.setSmall(true);
		this.armorStand.setDataWatcher(watcher);
	}
	
	public int getNameTagCount() {
		return nameTagCount;
	}
	
	private int getArmorStandId(int index) {
		return startEntityId + index * 2;
	}
}
