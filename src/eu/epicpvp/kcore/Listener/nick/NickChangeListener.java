package eu.epicpvp.kcore.Listener.nick;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.PacketHandler;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.ReceivedPacket;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.SentPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAbilities;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.PlayerAbilities;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition.EnumPlayerTeleportFlags;

public class NickChangeListener extends PacketHandler implements Listener{

	public NickChangeListener(JavaPlugin plugin) {
		super(plugin);
		System.out.println("Nick update listener registered!");
	}

	@EventHandler
	public void a(ServerMessageEvent e) {
		if (e.getChannel().equalsIgnoreCase("nick")) {
			int action = e.getBuffer().readInt();
			if (action == 0) {
				int playerId = e.getBuffer().readInt();
				LoadedPlayer player = UtilServer.getClient().getPlayer(playerId);
				if (player != null) {
					System.out.println("Updating nickname for: " + player.getName());
					UtilServer.getClient().clearCacheForPlayer(player);
					player = UtilServer.getClient().getPlayerAndLoad(playerId);
					String name = player.getName();
					respawn(Bukkit.getPlayer(player.getName()));
					Bukkit.getScheduler().runTaskAsynchronously(UtilServer.getPluginInstance(), ()->{
						try {
							Thread.sleep(1000);
						} catch (Exception e1) {
						}
						UtilServer.ensureMainthread(()->{
							for(Player p : Bukkit.getOnlinePlayers())
								UtilScoreboard.resendScoreboard(p);
						});
					});
				}
			}
		}
	}

	private void respawn(Player player) {
		if(player == null)
			return;
		try {
			Location l = player.getLocation();
			PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { player.getEntityId() });
			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());
			PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());
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
	@Override
	public void onSend(SentPacket packet) {
		try{
			if(packet.getPacket() instanceof PacketPlayOutPlayerInfo){
				PacketPlayOutPlayerInfo pack = (PacketPlayOutPlayerInfo) packet.getPacket();
				List<PlayerInfoData> data = (List<PlayerInfoData>) UtilReflection.getPrivateValue(pack, "b");
				for(PlayerInfoData d : data){
					if(d.a() == null || d.a().getName() == null || d.a().getName().length() < 3 || d.a().getName().startsWith("{"))
						continue;
					GameProfile copied = new GameProfile(d.a().getId(), "{player_"+d.a().getName()+"}");
					for(Entry<String, Collection<Property>> e : d.a().getProperties().asMap().entrySet())
						for(Property p : e.getValue())
							copied.getProperties().put(e.getKey(), p);
					UtilReflection.setFinalValue(UtilReflection.getField(PlayerInfoData.class, "d"), d, copied);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onReceive(ReceivedPacket packet) {
		
	}
}
