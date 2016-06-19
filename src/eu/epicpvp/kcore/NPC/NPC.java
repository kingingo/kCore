package eu.epicpvp.kcore.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.client.Callback;
import dev.wolveringer.skin.Skin;
import eu.epicpvp.kcore.PacketAPI.Packets.kGameProfile;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutAnimation;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutBed;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutEntityEquipment;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutEntityTeleport;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutRelEntityMoveLook;
import eu.epicpvp.kcore.PacketAPI.Packets.kPlayerInfoData;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilSkin;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

public class NPC {

	private NPCManager manager = null;
	private DataWatcher watcher;
	private Material chestplate;
	private Material leggings;
	private Location location;
	private Material inHand;
	private Material helmet;
	private Material boots;
	private String tablist;
	@Setter
	private int entityID;
	@Getter
	private kGameProfile profile;
	@Getter
	@Setter
	private ArrayList<Player> notsend;
	private kPacketPlayOutNamedEntitySpawn spawn_packet;

	public NPC(NPCManager manager, String name, String tablist, UUID uuid, int entityID, Location location,
			Material inHand) {
		this.location = location;
		this.manager = manager;
		this.tablist = tablist;
		this.entityID = entityID;
		this.inHand = inHand;
		this.profile = new kGameProfile(uuid, name);
		this.watcher = new DataWatcher(null);
		watcher.a(6, (float) 20);
		if (manager != null)
			this.manager.getNPCList().put(this.entityID, this);
	}

	public NPC(NPCManager manager, String name, Location location) {
		this(manager, name, name, UUID.randomUUID(), new Random().nextInt(10000), location, Material.AIR);
	}

	public NPC(NPCManager manager, String name, Location location, Material inHand) {
		this(manager, name, name, UUID.randomUUID(), new Random().nextInt(10000), location, inHand);
	}

	public NPC(NPCManager manager, String name, String tablist, Location location) {
		this(manager, name, tablist, UUID.randomUUID(), new Random().nextInt(10000), location, Material.AIR);
	}

	public NPC(NPCManager manager, String name, String tablist, Location location, Material inHand) {
		this(manager, name, tablist, UUID.randomUUID(), new Random().nextInt(10000), location, inHand);
	}

	public kPacketPlayOutNamedEntitySpawn getSpawnPacket() {
		if (this.spawn_packet != null)
			return this.spawn_packet;
		try {
			spawn_packet = new kPacketPlayOutNamedEntitySpawn();

			spawn_packet.setEntityID(entityID);
			spawn_packet.setUUID(profile.getId());
			spawn_packet.setLocation(location);
			spawn_packet.setItemInHand(inHand);
			spawn_packet.setDataWatcher(watcher);
			return this.spawn_packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void spawn() {
		try {
			spawn_packet = getSpawnPacket();
			this.addToTablist();
			
			for (Player online : UtilServer.getPlayers()) {
				if (notsend != null && notsend.contains(online))
					continue;
				UtilPlayer.sendPacket(online, spawn_packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadSkin(UUID uuid) {
		UtilSkin.loadSkin(new Callback<Skin>() {
			@Override
			public void call(Skin obj, Throwable exception) {
				loadSkin(obj);
			}
		}, uuid);
	}

	public void loadSkin(String playerName) {
		UtilSkin.loadSkin(new Callback<Skin>() {
			@Override
			public void call(Skin obj, Throwable exception) {
				loadSkin(obj);
			}
		}, playerName);
	}

	public void loadSkin(Skin data) {
		this.profile.loadSkin(data);
	}

	public void despawn() {
		kPacketPlayOutEntityDestroy packet = new kPacketPlayOutEntityDestroy(new int[] { this.entityID });
		this.removeFromTablist();
		if (manager != null)
			this.manager.getNPCList().remove(this.entityID);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public void walk(Location newLoc) {
		walk(newLoc.getBlockX(), newLoc.getBlockY(), newLoc.getBlockZ(), getLocation().getYaw(),
				getLocation().getPitch());
	}

	public void walk(Location newLoc, float yaw, float pitch) {
		walk(newLoc.getBlockX(), newLoc.getBlockY(), newLoc.getBlockZ(), yaw, pitch);
	}

	public byte getCompressedAngle(float value) {
		return (byte) ((value * 256.0F) / 360.0F);
	}

	public void walk(int xs, int ys, int zs, float yaw, float pitch) {
		byte x = (byte) ((xs - getLocation().getBlockX()) * 32);
		byte y = (byte) ((ys - getLocation().getBlockY()) * 32);
		byte z = (byte) ((zs - getLocation().getBlockZ()) * 32);
		this.location.add((xs - getLocation().getBlockX()), (ys - getLocation().getBlockY()),
				(zs - getLocation().getBlockZ()));
		kPacketPlayOutRelEntityMoveLook packet = new kPacketPlayOutRelEntityMoveLook(this.entityID, x, y, z, getCompressedAngle(yaw), getCompressedAngle(pitch), true);

		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public void sleep() {
		try {
			kPacketPlayOutBed packet = new kPacketPlayOutBed();

			packet.setEntityID(entityID);
			packet.setPosition(location);

			for (Player online : UtilServer.getPlayers()) {
				if (notsend != null && notsend.contains(online))
					continue;
				UtilPlayer.sendPacket(online, packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changePlayerlistName(String name) {
		kPacketPlayOutPlayerInfo packet = getPacketChangePlayerlistName(name);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutPlayerInfo getPacketChangePlayerlistName(String name) {
		try {
			kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();

			PlayerInfoData data = new kPlayerInfoData(packet, profile, name);
			List<PlayerInfoData> players = packet.getList();
			players.add(data);

			packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
			packet.setList(players);
			this.tablist = name;

			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addToTablist() {
		kPacketPlayOutPlayerInfo packet = getAddTablist();
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutPlayerInfo getAddTablist() {
		try {
			kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
			PlayerInfoData data = new kPlayerInfoData(packet, profile, tablist);
			packet.getList().add(data);
			packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void removeFromTablist() {
		kPacketPlayOutPlayerInfo packet = getRemoveTablist();
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutPlayerInfo getRemoveTablist() {
		try {
			kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
			PlayerInfoData data = new kPlayerInfoData(packet, profile, "");
			packet.getList().add(data);
			packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void teleport(Location location) {
		kPacketPlayOutEntityTeleport packet = getTeleportPacket(location);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutEntityTeleport getTeleportPacket(Location location) {
		try {
			kPacketPlayOutEntityTeleport packet = new kPacketPlayOutEntityTeleport();

			packet.setEntityID(entityID);
			packet.setLocation(location);
			packet.setOnGround(this.location.getBlock().getType() == Material.AIR ? false : true);
			this.location = location;

			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setItemInHand(Material material) {
		setItemInHand(new ItemStack(material));
	}

	public void setItemInHand(ItemStack item) {
		kPacketPlayOutEntityEquipment packet = getItemInHandPacket(item);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutEntityEquipment getItemInHandPacket(ItemStack item) {
		try {
			kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(this.entityID, 0, item.getType());
			packet.setItemStack(item);

			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Material getItemInHand() {
		return this.inHand;
	}

	private void setAnimation(NPCAnimation animation) {
		kPacketPlayOutAnimation packet = getPacketAnimation(animation);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutAnimation getPacketAnimation(NPCAnimation animation) {
		try {
			kPacketPlayOutAnimation packet = new kPacketPlayOutAnimation();
			packet.setEntityID(entityID);
			packet.setNPCAnimation(animation);

			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setHelmet(Material material) {
		kPacketPlayOutEntityEquipment packet = getPacketHelm(material);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutEntityEquipment getPacketHelm(Material material) {
		try {
			kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(this.entityID, 4, material);
			this.helmet = material;
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Material getHelmet() {
		return this.helmet;
	}

	public void setChestplate(Material material) {
		kPacketPlayOutEntityEquipment packet = getPacketChestplate(material);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutEntityEquipment getPacketChestplate(Material material) {
		try {
			kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID, 3, material);

			this.chestplate = material;
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Material getChestplate() {
		return this.chestplate;
	}

	public void setName(String s) {
		this.watcher.a(0, (Object) (byte) 0);
		this.watcher.a(1, (Object) (short) 300);
		this.watcher.a(3, (Object) (byte) 0);
		this.watcher.a(2, (Object) (String) s);
		this.watcher.a(4, (Object) (byte) 0);
		this.profile.setName(s);
		kPacketPlayOutEntityMetadata packet = new kPacketPlayOutEntityMetadata(this.entityID, this.watcher);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public void setLeggings(Material material) {
		kPacketPlayOutEntityEquipment packet = getPacketLeggings(material);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutEntityEquipment getPacketLeggings(Material material) {
		try {
			kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID, 2, material);

			this.leggings = material;
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Material getLeggings() {
		return this.leggings;
	}

	public void setBoots(Material material) {
		kPacketPlayOutEntityEquipment packet = getPacketBoots(material);
		for (Player online : UtilServer.getPlayers()) {
			if (notsend != null && notsend.contains(online))
				continue;
			UtilPlayer.sendPacket(online, packet);
		}
	}

	public kPacketPlayOutEntityEquipment getPacketBoots(Material material) {
		try {
			kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID, 1, material);
			this.boots = material;
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Material getBoots() {
		return this.boots;
	}

	public int getEntityID() {
		return this.entityID;
	}

	public UUID getUUID() {
		return this.profile.getId();
	}

	public Location getLocation() {
		return this.location;
	}

	public String getName() {
		return this.profile.getName();
	}

	public String getPlayerlistName() {
		return this.tablist;
	}
	
	
	public void show(Player player){
		UtilPlayer.sendPacket(player, getAddTablist());
		UtilPlayer.sendPacket(player, getSpawnPacket());
		Bukkit.getScheduler().runTaskLater(manager.getInstance(), new Runnable() {
			@Override
			public void run() {
				UtilPlayer.sendPacket(player, getRemoveTablist());
			}
		}, 20*4);
	}
}