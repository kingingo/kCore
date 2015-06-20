package me.kingingo.kcore.Hologram.nametags;

import java.util.Map;

import lombok.Getter;
import me.kingingo.kcore.Hologram.nametags.Events.HologramCreateEvent;
import me.kingingo.kcore.PacketAPI.Packets.kDataWatcher;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityLiving;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityTeleport;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
// These can be found in the following project:
//  https://github.com/aadnk/PacketWrapper

/**
 * Represents a spawner of name tags.
 * 
 * @author Kristian
 */
public class NameTagSpawner {
	private static final Byte ENTITY_INVISIBLE = Byte.valueOf((byte) 32);
	// Shared entity ID allocator
	private static int SHARED_ENTITY_ID = Short.MAX_VALUE;

	// The starting entity ID
	private int startEntityId;
	private int nameTagCount;
	
	@Getter
	private kPacketPlayOutEntityLiving ArmorStand;

	// Previous locations
	private Map<Player, Vector[]> playerLocations = new MapMaker().weakKeys().makeMap();

	/**
	 * Construct a new name tag spawner.
	 * <p>
	 * Specify a number of name tags to spawn.
	 * 
	 * @param nameTags - the maximum number of name tags we will spawn at any
	 *            given time.
	 */
	public NameTagSpawner(int nameTagCount) {
		this.startEntityId = SHARED_ENTITY_ID;
		this.nameTagCount = nameTagCount;

		// We need to reserve two entity IDs per name tag
		SHARED_ENTITY_ID += nameTagCount * 2;
	}

	/**
	 * Retrieve the maximum number of name tags we can spawn.
	 * 
	 * @return The maximum number.
	 */
	public int getNameTagCount() {
		return nameTagCount;
	}

	/**
	 * Clear every name tag this spawner can create.
	 * 
	 * @param observer - the observer.
	 */
	public void clearNameTags(Player observer) {
		int[] indices = new int[nameTagCount];

		for (int i = 0; i < indices.length; i++)
			indices[i] = i;
		clearNameTags(observer, indices);
	}

	/**
	 * Remove a name tag for a given observer.
	 * 
	 * @param indices - the indices.
	 * @param observer - the observer.
	 */
	public void clearNameTags(Player observer, int... indices) {
		kPacketPlayOutEntityDestroy destroy = new kPacketPlayOutEntityDestroy();
		int[] ids = new int[indices.length * 2];

		// The entities to remove
		for (int i = 0; i < indices.length; i++) {
			Preconditions.checkPositionIndex(indices[i], nameTagCount, "indices");
			ids[i * 2] = getArmorStandId(indices[i]);
			//ids[i * 2 + 1] = getSkullId(indices[i] * 2);
		}
		destroy.setIDs(ids);
		UtilPlayer.sendPacket(observer, destroy);
	}

	/**
	 * Retrieve the location of a given name tag.
	 * 
	 * @param index - the index.
	 * @param observer - the observing player.
	 * @return The location, or NULL if the name tag has not been sent.
	 */
	public Location getLocation(int index, Player observer) {
		Vector[] locations = playerLocations.get(observer);

		if (locations != null && locations[index] != null) {
			return locations[index].toLocation(observer.getWorld());
		}
		return null;
	}

	/**
	 * Set the location and message of a name tag.
	 * 
	 * @param index - index of the name tag. Cannot exceeed
	 *            {@link #getNameTagCount()}.
	 * @param observer - the observing player.
	 * @param location - the location in the same world as the player.
	 * @param dY - Y value to add to the final location.
	 * @param message - the message to display.
	 */
	public void setNameTag(int index, Player observer, Location location, double dY, String message) {
			 ArmorStand = createArmorStandPacket(index, location, dY, message);
			
			Bukkit.getPluginManager().callEvent(new HologramCreateEvent(this));
			
			UtilPlayer.sendPacket(observer, ArmorStand);

			// Save location
			getLocations(observer)[index] = new Vector(location.getX(), location.getY() + dY,location.getZ());
	}

	/**
	 * Move a name tag to a given location.
	 * 
	 * @param index - the index of the tag.
	 * @param location - the new location.
	 */
	public void moveNameTag(int index, Player observer, Location location) {
//		WrapperPlayServerEntityTeleport teleportArmorStand = new WrapperPlayServerEntityTeleport();
//		teleportArmorStand.setEntityID(getArmorStandId(index));
//		teleportArmorStand.setX(location.getX());
//		teleportArmorStand.setY(location.getY()-2);
//		teleportArmorStand.setZ(location.getZ());
//		
//		teleportArmorStand.sendPacket(observer);
		kPacketPlayOutEntityTeleport teleport = new kPacketPlayOutEntityTeleport();
		teleport.setEntityID(getArmorStandId(index));
		teleport.setX(location.getX());
		teleport.setY(location.getY()-2);
		teleport.setZ(location.getZ());
		
		UtilPlayer.sendPacket(observer, teleport);
		getLocations(observer)[index] = location.toVector();
	}

	/**
	 * Retrieve the current vector of name tag locations.
	 * 
	 * @param player - the player.
	 * @return The locations.
	 */
	private Vector[] getLocations(Player player) {
		Vector[] result = playerLocations.get(player);

		if (result == null) {
			result = new Vector[nameTagCount];
			playerLocations.put(player, result);
		}
		return result;
	}
	
	// Construct the invisible ArmorStand packet
	private kPacketPlayOutEntityLiving createArmorStandPacket(int index, Location location,double dY, String message) {
//		WrapperPlayServerSpawnEntityLiving ArmorStand = new WrapperPlayServerSpawnEntityLiving();
//		ArmorStand.setEntityID(getArmorStandId(index));
//		ArmorStand.setType(EntityType.ARMOR_STAND);
//		ArmorStand.setX(location.getX());
//		ArmorStand.setY(location.getY() + dY - 2);
//		ArmorStand.setZ(location.getZ());
//		
//		WrappedDataWatcher wdw = new WrappedDataWatcher();	
//		wdw.setObject(0, ENTITY_INVISIBLE);
//		wdw.setObject(2,message);
//		wdw.setObject(3, (byte) 1);
//		ArmorStand.setMetadata(wdw);
		kPacketPlayOutEntityLiving ArmorStand = new kPacketPlayOutEntityLiving(UtilMath.r(1000), EntityType.ARMOR_STAND, location);
		ArmorStand.setY(location.getY() + dY - 2);
		
		kDataWatcher watcher = new kDataWatcher();
		watcher.setCustomName(message);
		watcher.setCustomNameVisible(true);
		watcher.setVisible(false);
		
		ArmorStand.setDataWatcher(watcher);
		
		return ArmorStand;
	}

	private int getArmorStandId(int index) {
		return startEntityId + index * 2;
	}
}
