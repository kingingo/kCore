package eu.epicpvp.kcore.Permission;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.spigotmc.AsyncCatcher;

import dev.wolveringer.bukkit.permissions.GroupTyp;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.client.threadfactory.ThreadFactory;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketServerMessage;
import eu.epicpvp.kcore.Permission.Events.PlayerLoadPermissionEvent;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilReflection;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class PermissionManager {
	private static PermissionManager manager;

	public static PermissionManager getManager() {
		return manager;
	}

	public static void setManager(PermissionManager manager) {
		PermissionManager.manager = manager;
		Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
		Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.PLAYER_LIST);
	}

	@Getter
	private ArrayList<Group> groups = new ArrayList<>();
	@Getter
	private HashMap<Integer, PermissionPlayer> user = new HashMap<>();
	@Getter
	protected JavaPlugin instance;
	@Getter
	protected PermissionChannelHandler handler;
	@Getter
	private GroupTyp type;

	public PermissionManager(JavaPlugin instance, GroupTyp type) {
		this.instance = instance;
		PermissionManager.manager = this;
		this.type = type;
		this.handler = new PermissionChannelHandler(this);
		Bukkit.getMessenger().registerIncomingPluginChannel(instance, "permission", handler);
		Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "permission");
		UtilServer.setPermissionManager(this);
	}

	public boolean isBuyableTimeRank(String group) {
		return group.equalsIgnoreCase("vip1") || group.equalsIgnoreCase("vip3") || group.equalsIgnoreCase("vip6") || group.equalsIgnoreCase("ultra1") || group.equalsIgnoreCase("ultra3") || group.equalsIgnoreCase("ultra6") || group.equalsIgnoreCase("legend1") || group.equalsIgnoreCase("legend3") || group.equalsIgnoreCase("legend6") || group.equalsIgnoreCase("mvp1") || group.equalsIgnoreCase("mvp3") || group.equalsIgnoreCase("mvp6") || group.equalsIgnoreCase("mvp+1")
				|| group.equalsIgnoreCase("mvp+3") || group.equalsIgnoreCase("mvp+6");
	}

	public boolean isBuyableRank(String group) {
		return group.equalsIgnoreCase("vip") || group.equalsIgnoreCase("ultra") || group.equalsIgnoreCase("legend") || group.equalsIgnoreCase("mvp") || group.equalsIgnoreCase("mvp+");
	}

	public int getUpdgradeGroupPrice(Player player, String toGroup) {
		PermissionPlayer permissionPlayer = getPermissionPlayer(player);
		Group group = permissionPlayer.getGroups().get(0);

		if (isBuyableRank(group.getName())) {
			return getUpdgradeGroupPrice(group.getName(), toGroup);
		}

		return getGroupPrice(toGroup);
	}

	public int getUpdgradeGroupPrice(String playerGroup, String toGroup) {
		int player_group = getGroupPrice(playerGroup);
		int new_group = getGroupPrice(toGroup);
		return new_group - player_group;
	}

	public boolean checkHigherRank(Player player, String group) {
		PermissionPlayer permissionPlayer = getPermissionPlayer(player);
		Group pgroup = permissionPlayer.getGroups().get(0);

		if (isBuyableRank(pgroup.getName())) {
			return (getGroupPrice(pgroup.getName()) > getGroupPrice(group));
		}

		return false;
	}

	public int getGroupPrice(String groupName) {
		switch (groupName.toLowerCase()) {
		case "vip":
			return 5400;
		case "ultra":
			return 12000;
		case "legend":
			return 17400;
		case "mvp":
			return 24000;
		case "mvp+":
			return 35000;
		default:
			return 99999999;
		}
	}

	public Scoreboard getScoreboard() {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

		for (Player p : UtilServer.getPlayers()) {
			if (getPermissionPlayer(p) != null && !getPermissionPlayer(p).getGroups().isEmpty() && board.getTeam(getPermissionPlayer(p).getGroups().get(0).getName()) == null) {
				UtilScoreboard.addTeam(board, getPermissionPlayer(p).getGroups().get(0).getName(), null, getPermissionPlayer(p).getGroups().get(0).getPrefix());
			}
			if (getPermissionPlayer(p) != null && !getPermissionPlayer(p).getGroups().isEmpty())
				UtilScoreboard.addPlayerToTeam(board, getPermissionPlayer(p).getGroups().get(0).getName(), p);
		}

		return board;
	}

	public void setTabList(Player player) {
		setTabList(player, true);
	}

	public void setTabList(Player player, boolean callEvent) {
		try {
			AsyncCatcher.catchOp("");
		} catch (Exception e) {
			Bukkit.getScheduler().runTask(instance, new Runnable() { //Call sync
				@Override
				public void run() {
					setTabList(player, callEvent);
				}
			});
			return;
		}
		if (getPermissionPlayer(player) == null) {
			System.err.println("PermissionPlayer from " + player.getName() + " == NULL [" + getClass().getName() + "]");
			return;
		}

		if (getPermissionPlayer(player).getGroups().isEmpty()) {
			System.err.println(player.getName() + " has not any groups [" + getClass().getName() + "]");
			return;
		}
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		player.setPlayerListName(getPrefix(player)+player);
		UtilScoreboard.addTeam(player.getScoreboard(), getTabGroup(player)).addEntry(player.getName());;

		for (Player p : UtilServer.getPlayers()) {
			if (p.equals(player))
				continue;
			UtilScoreboard.addTeam(p.getScoreboard(), getTabGroup(player)).addEntry(player.getName());
			UtilScoreboard.addTeam(player.getScoreboard(), getTabGroup(p)).addEntry(p.getName());
		}

		Bukkit.getPluginManager().callEvent(new PlayerSetScoreboardEvent(player));
		if (callEvent) {
			Bukkit.getPluginManager().callEvent(new PlayerLoadPermissionEvent(this, getPermissionPlayer(player)));
		}
	}

	public void unloadPlayer(Player player) {
		user.remove(UtilPlayer.getPlayerId(player));
	}

	public void loadPlayer(Player player, int playerId) {
		if (!user.containsKey(playerId))
			user.put(playerId, new PermissionPlayer(player, this, playerId));
	}

	public PermissionPlayer getPermissionPlayer(Player player) {
		int playerId = UtilPlayer.getPlayerId(player);
		PermissionPlayer pplayer = getPermissionPlayer(playerId);
		return pplayer;
	}

	public PermissionPlayer getPermissionPlayer(int playerId) {
		return user.get(playerId);
	}

	public boolean hasPermission(Player player, String permission) {
		return hasPermission(UtilPlayer.getPlayerId(player), permission);
	}

	public boolean hasPermission(Player player, PermissionType teamMessage) {
		return hasPermission(UtilPlayer.getPlayerId(player), teamMessage.getPermissionToString());
	}

	public boolean hasPermissionType(Player player, PermissionType teamMessage) {
		return hasPermission(UtilPlayer.getPlayerId(player), teamMessage.getPermissionToString());
	}

	public boolean hasPermission(Player player, PermissionType teamMessage, boolean message) {
		return hasPermission(player, teamMessage.getPermissionToString(), message);
	}

	public String getTabGroup(Player player) {
		String name = player.getName();
		PermissionPlayer pplayer = getPermissionPlayer(player);
		if (!pplayer.getGroups().isEmpty())
			name = String.format("%03d", 999 - pplayer.getGroups().get(0).getImportance()) + pplayer.getGroups().get(0).getName();
		else
			name = "000" + name;
		if (name.length() > 16)
			name = name.substring(0, 15);
		return name;
	}

	public String getPrefix(Player player) {
		PermissionPlayer pplayer = getPermissionPlayer(player);
		if (pplayer.getGroups().isEmpty() || !pplayer.isPrefix())
			return "§6§m";
		String prefix = pplayer.getGroups().get(0).getPrefix();
		if (prefix.length() > 16)
			prefix = prefix.substring(0, 16);
		return prefix;
	}

	public boolean hasPermission(Player player, String permission, boolean message) {
		boolean perm = hasPermission(UtilPlayer.getPlayerId(player), permission);
		if (message && !perm)
			player.sendMessage(UtilServer.getClient().getTranslationManager().translate("prefix", UtilServer.getClient().getTranslationManager().getLanguage(UtilServer.getClient().getPlayerAndLoad(player.getName()))) + "§cYou don't have permission to do that.");
		return perm;
	}

	public boolean hasPermission(int playerId, PermissionType permission) {
		return hasPermission(playerId, permission.getPermissionToString());
	}

	public boolean hasPermission(int playerId, String permission) {
		PermissionPlayer permissionPlayer = user.get(playerId);
		if (permissionPlayer == null)
			return false;
		return permissionPlayer.hasPermission(permission);
	}

	public void addPermission(Player player, PermissionType type) {
		getPermissionPlayer(player).addPermission(type.getPermissionToString());
	}

	public Group getGroup(String name) {
		for (Group g : new ArrayList<>(groups))
			if (g != null)
				if (g.getName() != null)
					if (g.getName().equalsIgnoreCase(name))
						return g;
		return null;
	}

	public Group loadGroup(String name) {
		Group g = getGroup(name);
		if (g == null) {
			groups.add(g = new Group(this, name));
		}
		if (!g.isLoaded())
			g.load();
		return g;
	}

	public void unloadGroup(Group g) {
		groups.remove(g);
	}

	public void setGroup(Player player, String toGroup) {
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		DataBuffer buffer = new DataBuffer();
		buffer.writeByte(2);
		buffer.writeInt(loadedplayer.getPlayerId());
		buffer.writeString(null);
		buffer.writeString(toGroup);
		PacketServerMessage packet = new PacketServerMessage("permission", ClientType.BUNGEECORD, buffer);

		UtilReflection.setValue("targets", packet, new PacketServerMessage.Target[]
		{ new PacketServerMessage.Target(ClientType.BUNGEECORD, "targetlimit;1") });

		UtilServer.getClient().writePacket(packet);
		System.out.println("Set " + player + "(" + loadedplayer.getPlayerId() + ") to " + toGroup);
	}

	protected void updatePlayer(int playerId) {
		if (user.containsKey(playerId))
			user.remove(playerId);
		if (UtilPlayer.isOnline(playerId)) {
			ThreadFactory.getFactory().createThread(new Runnable() {
				@Override
				public void run() {
					loadPlayer(UtilPlayer.searchExact(playerId), playerId);
					setTabList(UtilPlayer.searchExact(playerId), false);
				}
			}).start();
		}
	}

	public void updateGroup(String group) {
		ThreadFactory.getFactory().createThread(new Runnable() {
			@Override
			public void run() {
				groups.remove(getGroup(group));
				if (!Bukkit.getOnlinePlayers().isEmpty()) {
					loadGroup(group);
					for (PermissionPlayer p : new ArrayList<PermissionPlayer>(user.values()))
						updatePlayer(p.getPlayerId());
				}
			}
		}).start();
	}
}
