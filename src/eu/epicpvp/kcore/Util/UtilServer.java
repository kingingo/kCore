package eu.epicpvp.kcore.Util;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.AsyncCatcher;

import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.connection.Client;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.client.connection.ServerInformations;
import dev.wolveringer.client.connection.State;
import dev.wolveringer.client.external.ServerActionListener;
import dev.wolveringer.client.threadfactory.ThreadFactory;
import dev.wolveringer.dataserver.gamestats.GameState;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.player.Setting;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketInServerStatus;
import dev.wolveringer.translation.TranslationManager;
import eu.epicpvp.kcore.Achievements.Handler.AchievementsHandler;
import eu.epicpvp.kcore.Arena.TabManager;
import eu.epicpvp.kcore.Arena.BestOf.BestOf;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.DeliveryPet.DeliveryPet;
import eu.epicpvp.kcore.Disguise.DisguiseManager;
import eu.epicpvp.kcore.Events.ClientConnectedEvent;
import eu.epicpvp.kcore.Events.ClientDisconnectedEvent;
import eu.epicpvp.kcore.Events.ServerChangeGameTypeEvent;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Events.ServerStatusUpdateEvent;
import eu.epicpvp.kcore.GagdetShop.GadgetHandler;
import eu.epicpvp.kcore.GemsShop.GemsShop;
import eu.epicpvp.kcore.Hologram.Hologram;
import eu.epicpvp.kcore.ItemShop.ItemShop;
import eu.epicpvp.kcore.Kit.PerkManager;
import eu.epicpvp.kcore.LagMeter.LagMeter;
import eu.epicpvp.kcore.LaunchItem.LaunchItemManager;
import eu.epicpvp.kcore.Listener.ClientListener.ClientListener;
import eu.epicpvp.kcore.Listener.EntityClick.EntityClickListener;
import eu.epicpvp.kcore.Listener.MoneyListener.MoneyListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.MysteryBox.MysteryBoxManager;
import eu.epicpvp.kcore.PacketAPI.packetlistener.kPacketListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.Updater;
import eu.epicpvp.kcore.UpdateAsync.UpdaterAsync;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldServer;

public class UtilServer {
	@Setter
	private static Plugin pluginInstance = null;
	@Getter
	@Setter
	private static MoneyListener moneyListener;
	@Getter
	@Setter
	private static AchievementsHandler achievementsHandler;
	@Getter
	@Setter
	private static MysteryBoxManager mysteryBoxManager;
	@Getter
	private static boolean loghandleradded = false;
	@Getter
	@Setter
	private static kPacketListener packetListener;
	@Getter
	@Setter
	private static PermissionManager permissionManager;
	@Getter
	@Setter
	private static DisguiseManager disguiseManager;
	@Getter
	@Setter
	private static LagMeter lagMeter;
	@Getter
	@Setter
	private static DeliveryPet deliveryPet;
	@Getter
	@Setter
	private static GemsShop gemsShop;
	@Getter
	@Setter
	private static GadgetHandler gadgetHandler;
	@Getter
	@Setter
	private static Updater updater;
	@Getter
	@Setter
	private static UserDataConfig userData;
	@Getter
	@Setter
	private static ItemShop itemShop;
	@Getter
	@Setter
	private static UpdaterAsync updaterAsync;
	@Getter
	@Setter
	private static LaunchItemManager launchItemManager;
	@Getter
	@Setter
	private static CommandHandler commandHandler;
	@Getter
	@Setter
	private static ClientWrapper client;
	@Getter
	@Setter
	private static MySQL mysql;
	@Getter
	@Setter
	private static Hologram hologram;
	@Getter
	@Setter
	private static PerkManager perkManager;
	@Getter
	@Setter
	private static TabManager tabManager;
	@Getter
	@Setter
	private static BestOf bestOf;
	@Getter
	@Setter
	private static TeleportManager teleportManager;

	public static void disable() {
		for (Player player : UtilServer.getPlayers())
			player.closeInventory();
		if (userData != null)
			userData.saveAllConfigs();
		for (Entity e : EntityClickListener.getEntities())
			e.remove();
		if (hologram != null)
			hologram.RemoveText();
		if (deliveryPet != null)
			deliveryPet.onDisable();
		if (itemShop != null && itemShop.getListener() != null && itemShop.getListener().getEntity() != null)
			itemShop.getListener().getEntity().remove();
		if (gemsShop != null)
			gemsShop.onDisable();
		if (perkManager != null && perkManager.getEntity() != null)
			perkManager.getEntity().remove();
		if (updater != null)
			updater.stop();
		if (updaterAsync != null)
			updaterAsync.stop();
		if (packetListener != null)
			packetListener.Disable();
		if (mysql != null)
			mysql.close();
		if (client != null)
			client.getHandle().disconnect();
	}

	public static ClientWrapper createClient(JavaPlugin instance, ClientType type, String host, int port, String name) {
		if (client == null) {
			TranslationManager.setLanguageDirectory(new File("/root/languages/"));
			createUpdaterAsync(instance);
			ThreadFactory.setFactory(new ThreadFactory()); //149.202.150.185

			client = new ClientWrapper(Client.createServerClient(type, name, new InetSocketAddress(host, port), new ServerActionListener() {

				@Override
				public void settingUpdate(UUID player, Setting setting, String value) {
				}

				@Override
				public void serverMessage(String channel, DataBuffer buffer) {
					Bukkit.getPluginManager().callEvent(new ServerMessageEvent(client, buffer, channel));
				}

				@Override
				public void kickPlayer(int playerId, String message) {
					if (UtilPlayer.isOnline(playerId)) {
						UtilPlayer.searchExact(playerId).kickPlayer(message);
					}
				}

				@Override
				public void disconnected() {
					Bukkit.getPluginManager().callEvent(new ClientDisconnectedEvent(client));
				}

				@Override
				public void connected() {
					Bukkit.getPluginManager().callEvent(new ClientConnectedEvent(client));
				}

				@Override
				public void broadcast(String permission, String message) {
					message = message.replaceAll("&", "§");
					if (permission == null) {
						UtilServer.broadcast(message);
					} else {
						for (Player player : UtilServer.getPlayers()) {
							if (player.hasPermission(permission)) {
								player.sendMessage(message);
							}
						}
					}
				}

				public void setGamemode(GameType game, String subtype) {
					Bukkit.getPluginManager().callEvent(new ServerChangeGameTypeEvent(game, subtype));
				}

				@Override
				public void restart(String kickMessage) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				}

				@Override
				public void stop(String kickMessage) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");

				}

				@Override
				public void sendMessage(int playerId, String message) {
					Player player = UtilPlayer.searchExact(playerId);
					if (player != null) {
						player.sendMessage(message);
					}
				}

				@Override
				public void error(State state, Exception e) {
					// TODO Auto-generated method stub

				}
			}, new ServerInformations() {

				@Override
				public PacketInServerStatus getStatus() {
					ServerStatusUpdateEvent ev = new ServerStatusUpdateEvent(new PacketInServerStatus(0, 0, 0, "", GameType.NONE, GameState.Laden, "none", true, name));
					Bukkit.getPluginManager().callEvent(ev);
					return ev.getPacket();
				}
			}));
			try {
				new ClientListener(instance, client);
				TranslationHandler.setInstance(client.getTranslationManager());
				client.getHandle().connect("HelloWorld".getBytes());

				TranslationHandler.getInstance().updateTranslations();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public static UserDataConfig createUserData(UserDataConfig userData) {
		if (getUserData() == null && userData != null)
			setUserData(userData);
		return getUserData();
	}

	public static ItemShop createItemShop(ItemShop itemShop) {
		if (getItemShop() == null && itemShop != null)
			setItemShop(itemShop);
		return getItemShop();
	}

	public static PerkManager createPerkManager(PerkManager perkManager) {
		if (getPerkManager() == null && perkManager != null)
			setPerkManager(perkManager);
		return getPerkManager();
	}

	public static Hologram createHologram(Hologram hm) {
		if (hologram == null && hm != null)
			hologram = hm;
		return hologram;
	}

	public static MySQL createMySQL(String user, String pass, String host, String db, JavaPlugin instance) {
		if (mysql == null)
			mysql = new MySQL(user, pass, host, db, instance);
		return mysql;
	}

	public static CommandHandler createCommandHandler(JavaPlugin instance) {
		if (commandHandler == null)
			commandHandler = new CommandHandler(instance);
		return commandHandler;
	}

	public static DisguiseManager createDisguiseManager(JavaPlugin instance) {
		if (disguiseManager == null)
			disguiseManager = new DisguiseManager(instance);
		return disguiseManager;
	}

	public static UpdaterAsync createUpdaterAsync(JavaPlugin instance) {
		if (updaterAsync == null)
			updaterAsync = new UpdaterAsync(instance);
		return updaterAsync;
	}

	public static Updater createUpdater(JavaPlugin instance) {
		if (updater == null)
			updater = new Updater(instance);
		return updater;
	}

	public static GemsShop createGemsShop(GemsShop gemShop) {
		if (gemsShop == null && gemShop != null)
			gemsShop = gemShop;
		return gemsShop;
	}

	public static DeliveryPet createDeliveryPet(DeliveryPet pet) {
		if (deliveryPet == null && pet != null)
			deliveryPet = pet;
		return deliveryPet;
	}

	public static LagMeter createLagListener(CommandHandler handler) {
		if (lagMeter == null && handler != null)
			lagMeter = new LagMeter(handler);
		return lagMeter;
	}

	public static kPacketListener createPacketListener(JavaPlugin instance) {
		if (packetListener == null)
			packetListener = new kPacketListener(instance);
		return packetListener;
	}

	public static void DebugLog(long time, String[] Reason, String c) {
		System.err.println("[DebugMode]: Class: " + c);
		for (String r : Reason) {
			System.err.println("[DebugMode]: Reason: " + r);
		}
		System.err.println("[DebugMode]: Zeit: " + ((System.currentTimeMillis() - time) / 1000.0D) + " Seconds");
	}

	public static void DebugLog(long time, String Reason, String c) {
		System.err.println("[DebugMode]: Class: " + c);
		System.err.println("[DebugMode]: Reason: " + Reason);
		System.err.println("[DebugMode]: Zeit: " + ((System.currentTimeMillis() - time) / 1000.0D) + " Seconds");
	}

	public static void DebugLog(long time, String c) {
		System.err.println("[DebugMode]: Class: " + c);
		System.err.println("[DebugMode]: Zeit: " + ((System.currentTimeMillis() - time) / 1000.0D) + " Seconds");
	}

	public static void DebugLog(String m) {
		System.err.println("[DebugMode]: " + m);
	}

	public static double getLineY(int i) {
		double d = 0;
		for (int a = 0; a <= i; a++)
			d += 0.2;

		return d;
	}

	public static List<Integer> showLine(Location loc, String text) {

		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		EntityWitherSkull skull = new EntityWitherSkull(world);
		skull.setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
		PacketPlayOutSpawnEntity skull_packet = new PacketPlayOutSpawnEntity(skull, skull.getId());

		EntityHorse horse = new EntityHorse(world);
		horse.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
		horse.setAge(-1700000);
		horse.setCustomName(text);
		horse.setCustomNameVisible(true);
		PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
		for (Player player : loc.getWorld().getPlayers()) {
			EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
			nmsPlayer.playerConnection.sendPacket(packedt);
			nmsPlayer.playerConnection.sendPacket(skull_packet);

			PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
			nmsPlayer.playerConnection.sendPacket(pa);
		}
		return Arrays.asList(skull.getId(), horse.getId());
	}

	public static void sendTeamMessage(String message) {

		if (message.contains("%=%")) {
			String[] split = message.split("%=%");
			for (String msg : split) {
				for (Player p : getPlayers())
					if (p.hasPermission(PermissionType.TEAM_MESSAGE.getPermissionToString()))
						p.sendMessage(msg);
			}
		} else {
			for (Player p : getPlayers())
				if (p.hasPermission(PermissionType.TEAM_MESSAGE.getPermissionToString()))
					p.sendMessage(message);
		}

		client.broadcastMessage(PermissionType.TEAM_MESSAGE.getPermissionToString(), message);
	}

	public static double getMaxMemoryMB() {
		return Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0);
	}

	public static double getMaxMemoryGB() {
		return Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0 * 1024.0);
	}

	public static void spawnRabbit(Location loc) {
		final Skeleton skel1 = loc.getWorld().spawn(loc, Skeleton.class);
		final Skeleton skel2 = loc.getWorld().spawn(loc, Skeleton.class);
		skel1.setCustomName("Dinnerbone");
		skel1.setCustomNameVisible(false);
		skel2.setCustomNameVisible(false);
		skel2.setPassenger(skel1);

		ItemStack hasenkopf = UtilItem.Head("rabbit2077");
		ItemStack bauch = new ItemStack(Material.WOOL, 1, (byte) 8);//UtilItem.Head("Hunter_Comm");
		skel2.getEquipment().setHelmet(hasenkopf);
		skel1.getEquipment().setHelmet(bauch);
		skel1.getEquipment().setItemInHand(new ItemStack(0));
		skel2.getEquipment().setItemInHand(new ItemStack(Material.CARROT_ITEM));

		skel1.setNoDamageTicks(Integer.MAX_VALUE);
		skel2.setNoDamageTicks(Integer.MAX_VALUE);

		Bukkit.getScheduler().runTaskTimer(null, new Runnable() {

			@Override
			public void run() {
				skel1.setCustomNameVisible(false);
				skel2.setCustomNameVisible(false);
				((CraftEntity) skel1).getHandle().setPositionRotation(skel2.getLocation().getX(), skel2.getLocation().getY(), skel2.getLocation().getZ(), 0F, 0F);
			}
		}, 1, 1);
	}

	public static Collection<? extends Player> getPlayers() {
		return Bukkit.getServer().getOnlinePlayers();
	}

	public static Server getServer() {
		return Bukkit.getServer();
	}

	public static void broadcastLanguage(String name, Object input) {
		for (Player cur : getPlayers())
			UtilPlayer.sendMessage(cur, TranslationHandler.getText("PREFIX") + TranslationHandler.getText(cur, name, input));
	}

	public static void broadcastLanguage(String name, Object[] input) {
		for (Player cur : getPlayers())
			UtilPlayer.sendMessage(cur, TranslationHandler.getText("PREFIX") + TranslationHandler.getText(cur, name, input));
	}

	public static void broadcastLanguage(String name) {
		for (Player cur : getPlayers())
			UtilPlayer.sendMessage(cur, TranslationHandler.getText("PREFIX") + TranslationHandler.getText(cur, name));
	}

	public static void broadcast(String message) {
		for (Player cur : getPlayers())
			UtilPlayer.sendMessage(cur, message);
	}

	public static double getFilledPercent() {
		return getPlayers().size() / getServer().getMaxPlayers();
	}

	public static void loopbackUntilValidDataserverConnection(Runnable run, String name, boolean sync) {
		loopbackUntilValidDataserverConnection(run, name, sync, null);
	}

	public static void loopbackUntilValidDataserverConnection(Runnable run, String name, boolean sync, Object syncronicedObject) {
		if (getClient().getHandle().isConnected()) {
			if (!sync)
				if (syncronicedObject == null)
					ThreadFactory.getFactory().createThread(run).start();
				else
					ThreadFactory.getFactory().createThread(new Runnable() {
						@Override
						public void run() {
							synchronized (syncronicedObject) {
								run.run();
							}
						}
					}).start();
			else
				ensureMainthread(run);
		}
		ThreadFactory.getFactory().createThread(() -> {
			System.out.println("Loopback " + name);
			while (!UtilServer.getClient().getHandle().isConnected()) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
			}
			if (!sync)
				if (syncronicedObject == null)
					ThreadFactory.getFactory().createThread(run).start();
				else
					ThreadFactory.getFactory().createThread(new Runnable() {
						@Override
						public void run() {
							synchronized (syncronicedObject) {
								run.run();
							}
						}
					}).start();
			else
				ensureMainthread(run);
		}).start();
	}

	public static void ensureMainthread(Runnable run) {
		ensureMainthread(run, null);
	}

	public static void ensureMainthread(Runnable run, Object syncronicedObject) {
		boolean main = false;
		try {
			AsyncCatcher.catchOp("");
			main = true;
		} catch (Exception e) {
		}
		if (main) {
			if (syncronicedObject == null)
				run.run();
			else
				synchronized (syncronicedObject) {
					run.run();
				}
		}
		if (syncronicedObject == null)
			Bukkit.getScheduler().runTask(mysql.getInstance(), run);
		else
			synchronized (syncronicedObject) {
				Bukkit.getScheduler().runTask(mysql.getInstance(), run);
			}
	}
	
	public static Plugin getPluginInstance(){
		return  pluginInstance != null ? pluginInstance : mysql.getInstance();
	}

	public static Player getNickedPlayer(String string) {
		if(Bukkit.getPlayer(string) != null)
			return Bukkit.getPlayer(string);
		for(Player p : Bukkit.getOnlinePlayers())
			if(UtilServer.getClient().getPlayerAndLoad(string).getNickname().equalsIgnoreCase(string))
				return p;
		return null;
	}
}