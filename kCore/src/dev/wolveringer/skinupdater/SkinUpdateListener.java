package dev.wolveringer.skinupdater;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.wolveringer.client.Callback;
import dev.wolveringer.skin.Skin;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAbilities;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.PlayerAbilities;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class SkinUpdateListener implements Listener {
	public SkinUpdateListener() {
		System.out.println("Skin listener registered!");
	}
	
	@EventHandler
	public void a(ServerMessageEvent e) {
		if (e.getChannel().equalsIgnoreCase("skin")) {
			if (e.getBuffer().readByte() == 0) {
				Player player = Bukkit.getPlayer(e.getBuffer().readString());
				if(player == null || !player.isOnline()){
					System.out.println("Cant update player skin!");
					return;
				}
				System.out.println("Updating skin for "+player.getName());
				UtilServer.getClient().getPlayerAndLoad(player.getUniqueId()).getOwnSkin().getAsync(new Callback<Skin>() {
					@Override
					public void call(Skin obj) {
						updateSkin(player,obj);
						System.out.println("Updated the skin of "+player.getName());
					}
				});
			}
		}
	}

	public void updateSkin(Player player, Skin skin) {
		EntityPlayer ep = ((CraftPlayer) player).getHandle();
		com.mojang.authlib.GameProfile eplayer = ep.getProfile();
		com.mojang.authlib.properties.Property prop = new com.mojang.authlib.properties.Property("textures", skin.getRawData(), skin.getSignature());

		// Clear the current textures (skin & cape).
		eplayer.getProperties().get("textures").clear();
		// Putting the new one.
		eplayer.getProperties().put(prop.getName(), prop);
		// Updating skin.
		sendSkinUpdate(player);
	}

	private void sendSkinUpdate(Player player) {
		try {
			Location l = player.getLocation();
			PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { player.getEntityId() });
			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());
			PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());
			PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(((CraftPlayer) player).getHandle().getWorld().worldProvider.getDimension(), ((CraftPlayer) player).getHandle().getWorld().getDifficulty(), ((CraftPlayer) player).getHandle().getWorld().G(), EnumGamemode.getById(player.getGameMode().getValue()));
			PacketPlayOutPosition pos = new PacketPlayOutPosition(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), new HashSet<EnumPlayerTeleportFlags>());
			PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
			PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
			PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
			PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
			PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
			PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());
			PlayerAbilities abilities = ((CraftPlayer) player).getHandle().abilities;
			PacketPlayOutAbilities packetAbilities = new PacketPlayOutAbilities(abilities);
			for (Player online : Bukkit.getOnlinePlayers()) {
				CraftPlayer craftOnline = (CraftPlayer) online;
				PlayerConnection playerCon = craftOnline.getHandle().playerConnection;
				if (online.equals(player)) {

					playerCon.sendPacket(removeInfo);
					playerCon.sendPacket(addInfo);
					playerCon.sendPacket(respawn);
					playerCon.sendPacket(packetAbilities);
					playerCon.sendPacket(pos);
					playerCon.sendPacket(slot);
					craftOnline.updateScaledHealth();
					craftOnline.getHandle().triggerHealthUpdate();
					craftOnline.updateInventory();
					continue;
				}
				playerCon.sendPacket(removeEntity);
				playerCon.sendPacket(removeInfo);
				playerCon.sendPacket(addInfo);
				playerCon.sendPacket(addNamed);
				playerCon.sendPacket(itemhand);
				playerCon.sendPacket(helmet);
				playerCon.sendPacket(chestplate);
				playerCon.sendPacket(leggings);
				playerCon.sendPacket(boots);
			}
		} catch (Exception e) {
			// Player logging in isnt finished and the method will not be used.
			// Player skin is already applied.
			e.printStackTrace();
		}
	}
}
