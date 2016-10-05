package eu.epicpvp.kcore.Arena;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.datenclient.event.EventListener;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.events.Event;
import eu.epicpvp.datenserver.definitions.events.EventConditions;
import eu.epicpvp.datenserver.definitions.events.EventType;
import eu.epicpvp.datenserver.definitions.events.player.PlayerServerSwitchEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Listener.SkinCatcherListener.SkinCatcherListener;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperGameProfile;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutPlayerInfo;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPlayerInfoData;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilSkin;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class TabManager extends kListener {

	@Getter
	private GameType[] types;
	private WrapperPacketPlayOutPlayerInfo info;
	private HashMap<Integer, WrapperPlayerInfoData> players;
	private HashMap<Integer, WrapperPacketPlayOutPlayerInfo> last_packet;

	@SuppressWarnings("unchecked")
	public TabManager(JavaPlugin instance, GameType[] types) {
		super(instance, "TabManager");
		this.types = types;
		this.players = new HashMap<>();
		this.last_packet = new HashMap<>();
		new SkinCatcherListener(instance, TimeSpan.HOUR * 4);
		this.info = new WrapperPacketPlayOutPlayerInfo();
		this.info.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
		UtilServer.getClient().getHandle().getEventManager().getEventManager(EventType.SERVER_SWITCH).setConditionEnables(EventConditions.GAME_TYPE_ARRAY, true);
		for (GameType type : types)
			UtilServer.getClient().getHandle().getEventManager().getEventManager(EventType.SERVER_SWITCH).getCondition(EventConditions.GAME_TYPE_ARRAY).addValue(type);

		UtilServer.getClient().getHandle().getEventManager().registerListener(new EventListener() {

			@SuppressWarnings("deprecation")
			public void fireEvent(Event e) {
				if (e instanceof PlayerServerSwitchEvent) {
					PlayerServerSwitchEvent ev = (PlayerServerSwitchEvent) e;
					LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(ev.getPlayerId());

					if (ev.getFrom() != null && ev.getFrom().startsWith("versus") && ev.getTo() != null && ev.getTo().startsWith("a")) {
						Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, new Runnable() {

							@Override
							public void run() {
								if (!UtilPlayer.isOnline(loadedplayer.getName())) {
									WrapperPacketPlayOutPlayerInfo add = new WrapperPacketPlayOutPlayerInfo();
									add.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);

									add.getList().add(new WrapperPlayerInfoData(add, EnumGamemode.SPECTATOR, new WrapperGameProfile(loadedplayer.getUUID(), loadedplayer.getName(), (UtilSkin.getCatcher().getSkins().containsKey(loadedplayer.getName().toLowerCase()) ? UtilSkin.getCatcher().getSkins().get(loadedplayer.getName().toLowerCase()) : null)), (UtilSkin.getCatcher().getTabnames().containsKey(loadedplayer.getPlayerId()) ? UtilSkin.getCatcher().getTabnames().get(loadedplayer.getUUID()) + loadedplayer.getName() : "§7" + loadedplayer.getName())));
									for (Player player : UtilServer.getPlayers()) {
										UtilPlayer.sendPacket(player, add);
									}
									last_packet.put(loadedplayer.getPlayerId(), add);
								}
							}
						}, 10);

						players.put(loadedplayer.getPlayerId(), new WrapperPlayerInfoData(info, EnumGamemode.SPECTATOR, new WrapperGameProfile(loadedplayer.getUUID(), loadedplayer.getName(), (UtilSkin.getCatcher().getSkins().containsKey(loadedplayer.getName().toLowerCase()) ? UtilSkin.getCatcher().getSkins().get(loadedplayer.getName().toLowerCase()) : null)), (UtilSkin.getCatcher().getTabnames().containsKey(loadedplayer.getPlayerId()) ? UtilSkin.getCatcher().getTabnames().get(loadedplayer.getUUID()) + loadedplayer.getName() : "§7" + loadedplayer.getName())));
						info.getList().add(players.get(loadedplayer.getPlayerId()));
					} else if (ev.getFrom() != null && ev.getFrom().startsWith("a") && players.containsKey(loadedplayer.getPlayerId())) {
						if (last_packet.containsKey(loadedplayer.getPlayerId())) {
							WrapperPacketPlayOutPlayerInfo remove = last_packet.get(loadedplayer.getPlayerId());
							remove.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
							for (Player player : UtilServer.getPlayers())
								UtilPlayer.sendPacket(player, remove);
						}
						info.getList().remove(players.get(loadedplayer.getPlayerId()));
						players.remove(loadedplayer.getPlayerId());
						last_packet.remove(loadedplayer.getPlayerId());
					}
				}
			}
		});

		Bukkit.getScheduler().runTaskAsynchronously(instance, new Runnable() {

			@Override
			public void run() {
				if (!UtilServer.getClient().getHandle().isConnected())
					return;
				for (Integer id : new ArrayList<>(players.keySet())) {
					if (!UtilServer.getClient().getPlayerAndLoad(id).isOnlineSync()) {
						if (last_packet.containsKey(id)) {
							WrapperPacketPlayOutPlayerInfo remove = last_packet.get(id);
							remove.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
							for (Player player : UtilServer.getPlayers())
								UtilPlayer.sendPacket(player, remove);
						}
						players.remove(id);
						last_packet.remove(id);
					}
				}

				try {
					Thread.sleep(1000 * 20);
				} catch (InterruptedException e) {
				}
			}

		});
		UtilServer.setTabManager(this);
	}

	public int getSize() {
		return players.size();
	}

	@EventHandler
	public void join(PlayerJoinEvent ev) {
		UtilPlayer.sendPacket(ev.getPlayer(), info);
	}
}
