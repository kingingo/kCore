package eu.epicpvp.kcore.Util;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import eu.epicpvp.datenclient.client.ClientWrapper;
import eu.epicpvp.datenclient.client.connection.Client;
import dev.wolveringer.client.connection.ClientType;
import eu.epicpvp.datenclient.client.connection.ServerInformations;
import eu.epicpvp.datenclient.client.connection.State;
import eu.epicpvp.datenclient.client.external.ServerActionListener;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameState;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.player.Setting;
import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketInServerStatus;
import dev.wolveringer.thread.ThreadFactory;
import eu.epicpvp.datenclient.translation.TranslationManager;
import eu.epicpvp.kcore.Achievements.Handler.AchievementsHandler;
import eu.epicpvp.kcore.Arena.BestOf.BestOf;
import eu.epicpvp.kcore.Arena.TabManager;
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
import eu.epicpvp.kcore.Gilden.GildenManager;
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
import eu.epicpvp.kcore.PacketAPI.packetlistener.NettyPacketListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.Updater;
import eu.epicpvp.kcore.UpdateAsync.UpdaterAsync;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.newGilde.GildeHandler;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.AsyncCatcher;

public class UtilServer {
	@Getter
	@Setter
	private static GildenManager gildenManager = null;
	@Getter
	@Setter
	private static GildeHandler gildeHandler = null;
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
	private static NettyPacketListener packetListener;
	@Getter
	@Setter
	private static PermissionManager permissionManager;
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
	private static Updater updater;
	private static UpdaterAsync updaterAsync;
	@Getter
	@Setter
	private static UserDataConfig userData;
	@Getter
	@Setter
	private static ItemShop itemShop;
	@Getter
	@Setter
	private static LaunchItemManager launchItemManager;
	private static CommandHandler commandHandler;
	@Getter
	@Setter
	private static ClientWrapper client;
	@Getter
	@Setter
	private static MySQL mysql;
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

	//Hologram list? Mayby later. Unused currently
	private ArrayList<Hologram> holograms = new ArrayList<>();

	public static void disable() {
		for (Player player : UtilServer.getPlayers())
			player.closeInventory();
		if (userData != null)
			userData.saveAllConfigs();
		for (Entity e : EntityClickListener.getEntities())
			e.remove();
//		if (hologram != null)
//			hologram.RemoveText();
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

				@Override
				public boolean isOnline(String name) {
					Player plr = Bukkit.getPlayerExact(name);
					return plr != null && plr.isOnline();
				}
			}, new ServerInformations() {

				@Override
				public PacketInServerStatus getStatus() {
					ServerStatusUpdateEvent ev = new ServerStatusUpdateEvent(new PacketInServerStatus(0, 0, 0, "", GameType.NONE, GameState.Laden, "none", true, name));
					Bukkit.getPluginManager().callEvent(ev);
					return ev.getPacket();
				}
			}));

			String password = "vtpmfru";

			try {
				new ClientListener(instance, client,password);
				TranslationHandler.setInstance(client.getTranslationManager());
				client.getHandle().connect(password.getBytes());

				TranslationHandler.getInstance().updateTranslations();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	//----------------------------------------------------
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
	//Useless?

	public static MySQL createMySQL(String user, String pass, String host, String db) {
		if (mysql == null)
			mysql = new MySQL(user, pass, host, db, getPluginInstance());
		return mysql;
	}

	public static CommandHandler getCommandHandler() {
		if (commandHandler == null)
			commandHandler = new CommandHandler(getPluginInstance());
		return commandHandler;
	}

	public static DisguiseManager getDisguiseManager() {
		if (disguiseManager == null)
			disguiseManager = new DisguiseManager(getPluginInstance());
		return disguiseManager;
	}

	public static UpdaterAsync getAsyncUpdater() {
		if (updaterAsync == null)
			updaterAsync = new UpdaterAsync(getPluginInstance());
		return updaterAsync;
	}

	public static Updater getUpdater() {
		if (updater == null)
			updater = new Updater(getPluginInstance());
		return updater;
	}

	public static GemsShop getGemsShop(GemsShop defaultValue) {
		if (gemsShop == null && defaultValue != null)
			gemsShop = defaultValue;
		return gemsShop;
	}

	public static DeliveryPet getDeliveryPet(DeliveryPet defaultValue) {
		if (deliveryPet == null && defaultValue != null)
			deliveryPet = defaultValue;
		return deliveryPet;
	}

	public static LagMeter getLagListener() {
		Validate.notNull(getCommandHandler(), "Command handle must be initialized");
		if (lagMeter == null)
			lagMeter = new LagMeter(getCommandHandler());
		return lagMeter;
	}

	public static NettyPacketListener getPacketListener() {
		if (packetListener == null)
			packetListener = new NettyPacketListener(getPluginInstance());
		return packetListener;
	}

	public static double getLineY(int i) {
		double d = 0;
		for (int a = 0; a <= i; a++)
			d += 0.2;

		return d;
	}

	public static void broadcastTeamChatMessage(String message){
		client.broadcastMessage(PermissionType.TEAM_MESSAGE.getPermissionToString(), message);
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
		if (isMainthread()) {
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

	public static boolean isMainthread(){
		try {
			AsyncCatcher.catchOp("");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void runAsync(Runnable run){
		ThreadFactory.getFactory().createThread(run).start();
	}
	public static void runSync(Runnable run){
		Bukkit.getScheduler().runTask(getPluginInstance(), run);
	}

	public static void runAsyncLater(Runnable run,int ms){
		runAsync(()->{
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				System.err.println("Cant run task "+run.getClass().getName()+" "+ms+" ms later! Interrupted.");
				return;
			}
			run.run();
		});
	}

	public static void runSyncLater(Runnable run,int ms){
		runAsyncLater(()->{
			runSync(run);
		}, ms);
	}

	public static Plugin getPluginInstance(){
		return  pluginInstance != null ? pluginInstance : mysql.getInstance();
	}

	public static Player getNickedPlayer(String string) {
		if(Bukkit.getPlayerExact(string) != null)
			return Bukkit.getPlayerExact(string);
		for(Player p : Bukkit.getOnlinePlayers())
			if(UtilServer.getClient().getPlayerAndLoad(string).getNickname().equalsIgnoreCase(string))
				return p;
		return null;
	}
}
