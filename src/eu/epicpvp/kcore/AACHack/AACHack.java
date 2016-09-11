package eu.epicpvp.kcore.AACHack;

import com.comphenix.protocol.ProtocolLibrary;
import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.connection.PacketListener;
import dev.wolveringer.dataserver.player.LanguageType;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import eu.epicpvp.kcore.AntiLogout.AntiLogoutManager;
import eu.epicpvp.kcore.Command.Admin.CommandReport;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Packets.PacketAACReload;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kCore;
import lombok.Getter;
import lombok.Setter;
import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationCommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class AACHack extends kListener {
	@Getter
	private MySQL mysql;
	@Setter
	@Getter
	private AntiLogoutManager antiLogoutManager;
	@Getter
	private String server;
	private ClientWrapper client;

	public AACHack(String server) {
		super(UtilServer.getPermissionManager().getInstance(), "AACHack");
		if (Bukkit.getPluginManager().getPlugin("AAC") == null) {
			logMessage("Das Plugin AAC fehlt!!!");
			return;
		}
		this.mysql = UtilServer.getMysql();
		this.client = UtilServer.getClient();
		this.server = server;
		UtilServer.getCommandHandler().register(CommandReport.class, new CommandReport(server));

		UtilServer.getClient().getHandle().getHandlerBoss().addListener(new PacketListener() {

			@Override
			public void handle(Packet packet) {
				if (packet instanceof PacketAACReload) {
					AACAPIProvider.getAPI().reloadAAC();
				}
			}
		});

		getMysql().Update("CREATE TABLE IF NOT EXISTS AAC_HACK(playerId int,ip varchar(30),server varchar(30),timestamp timestamp,hackType varchar(30),violations int)");
		logMessage("AACHack System aktiviert");

		//Prevent fly through a bug in nofall
		ProtocolLibrary.getProtocolManager().addPacketListener(new FlyBypassFixer());
		logMessage("Registered FlyBypassFixer");
		kCore.getInstance().getServer().getPluginManager().registerEvents(new ScaffoldWalkCheck(), kCore.getInstance());
		logMessage("Registered ScaffoldWalkCheck");
		kCore.getInstance().getServer().getPluginManager().registerEvents(new TowerLimiter(), kCore.getInstance());
		logMessage("Registered TowerLimiter");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerViolationKick(PlayerViolationCommandEvent event) {
		if (!event.getCommand().contains("kick")) {
			return;
		}
		int vl = AACAPIProvider.getAPI().getViolationLevel(event.getPlayer(), event.getHackType());

		LoadedPlayer loadedPlayer = client.getPlayerAndLoad(event.getPlayer().getName());
		int playerId = loadedPlayer.getPlayerId();
		getMysql().Update("INSERT INTO AAC_HACK (playerId,ip,server,hackType,violations) VALUES ('" + playerId + "','" + event.getPlayer().getAddress().getAddress().getHostAddress() + "','" + server + "','" + event.getHackType().getName() + "','" + vl + "');");

		if (getAntiLogoutManager() != null && event.getHackType() != HackType.SPAM) {
			getAntiLogoutManager().del(event.getPlayer());
		}
		HackType hackType = event.getHackType();

		if (hackType == HackType.KILLAURA || hackType == HackType.COMBATIMPOSSIBLE) { //they're similar hacks
			hackType = HackType.FORCEFIELD;
		}

		int anzahl = -1;
		int gesamtanzahl = -1;
		String reason;
		String code = event.getHackType() + server + "x" + vl + "y" + anzahl + "z" + gesamtanzahl;
		if (loadedPlayer.getLanguageSync() == LanguageType.GERMAN) {
			reason = "§4§lDu wurdest von unserem Anticheat-System gekickt.\n" +
					"\n" +
					"§cDas Nutzen sogenannter Hack-Clients, Autoclicker, Makros o.ä.\n" +
					"§cist auf unserem Netzwerk §nnicht§c gestattet.\n" +
					"§4Dies kann zu einem §npermanenten§4 Bann führen!\n" +
					"\n" +
					"§6Wenn du nicht gecheatet haben solltest, melde dies bitte §numgehend\n" +
					"§6im TS3 oder erstelle ein Supportticket unter www.ClashMC.eu\n" +
					"§6Für einen gültigen Report, erwähne bitte die Uhrzeit und folgenden Code: §e" + code;
		} else {
			reason = "§4§lYou were kicked by our anti cheat system.\n" +
					"\n" +
					"§cThe usage of so-called hack-clients, auto clicker, macros or similar things is §nnot§c allowed on our network.\n" +
					"§4This can lead to a §npermanent§4 ban!\n" +
					"\n" +
					"§6If you did not cheat, please report this issue §nimmediately\n" +
					"§6on our teampspeak or create a support ticket at www.ClashMC.eu\n" +
					"§6For a valid report, please tell us the current time and the following code: §e" + code;
		}
		if (hackType == HackType.FLY
				|| hackType == HackType.NOSWING
				|| hackType == HackType.HEADROLL
				|| hackType == HackType.REACH
				|| hackType == HackType.FASTBOW
				|| hackType == HackType.IMPOSSIBLEINTERACT
				|| hackType == HackType.FASTPLACE
				|| hackType == HackType.FIGHTSPEED
				|| hackType == HackType.ANGLE
				|| hackType == HackType.FORCEFIELD) {

			if (hackType == HackType.FORCEFIELD) {
				anzahl = getMysql().getInt("SELECT COUNT(*) FROM AAC_HACK WHERE (hackType='forcefield' OR hacktype='killaura' OR hacktype='combatimpossible') AND playerId='" + playerId + "'");
			} else {
				anzahl = getMysql().getInt("SELECT COUNT(*) FROM AAC_HACK WHERE hackType='" + hackType.getName() + "' AND playerId='" + playerId + "'");
			}
			gesamtanzahl = getMysql().getInt("SELECT COUNT(*) FROM AAC_HACK WHERE hackType!='nofall' AND playerId='" + playerId + "'");

			if (anzahl >= 4 || gesamtanzahl >= 7 || hackType == HackType.HEADROLL) {
				String type;
				int a;
				if (gesamtanzahl <= 10) {
					a = gesamtanzahl;
					type = "std";
				} else {
					a = gesamtanzahl / 2;
					type = "tag";
				}

				setZeitBan(event.getPlayer(), a, type, reason.replace("gekickt", "temporär gebannt").replace("kicked", "temporarely banned"));
				event.setCancelled(true);
			}
		}
		if (event.isCancelled()) {
			return;
		}
		if (event.getHackType() != HackType.BADPACKETS && event.getHackType() != HackType.SPAM && event.getHackType() != HackType.REGEN) { //might occurr on lag so don't kick them fully
			String cmd = event.getCommand();
			if (cmd.charAt(0) == '/') {
				cmd = cmd.substring(1);
			}
			if (cmd.startsWith("aackick")) {
				event.setCancelled(true);
				System.out.println("Anticheat-System-Kick: " + event.getPlayer().getName() + " hack: " + event.getHackType().getName() + " mysqlanzahl: " + anzahl + " gesamtanzahl: " + gesamtanzahl + " code: " + code);
				loadedPlayer.kickPlayer(reason);

			}
		}
	}

	private void setZeitBan(Player banned, int ti, String typ, String reason) {
		long time = 0;

		if (typ.equalsIgnoreCase("sec")) {
			long t = 1000 * ti;
			time = System.currentTimeMillis() + t;
		} else if (typ.equalsIgnoreCase("min")) {
			long t = 60000 * ti;
			time = System.currentTimeMillis() + t;
		} else if (typ.equalsIgnoreCase("std")) {
			long t = 3600000 * ti;
			time = System.currentTimeMillis() + t;
		} else if (typ.equalsIgnoreCase("tag")) {
			long t = 86400000 * ti;
			time = System.currentTimeMillis() + t;
		}

		LoadedPlayer loadedplayer = client.getPlayerAndLoad(banned.getName());
		loadedplayer.banPlayer(banned.getAddress().getAddress().getHostAddress(), "AAC", "AAC", null, 2, time, reason);
		loadedplayer.kickPlayer(reason);
		UtilServer.broadcastTeamChatMessage("§cDer Spieler §e" + banned.getName() + "§c wurde vom §eAntiCheatSystem§c für §e" + ti + " " + typ.toUpperCase() + " §cgesperrt. Grund: §e" + reason);
	}

	private void setBan(int lvl, Player banned, String reason) {
		LoadedPlayer loadedPlayer = client.getPlayerAndLoad(banned.getName());
		loadedPlayer.banPlayer(banned.getAddress().getHostName(), "AAC", "AAC", null, 2, -1, reason);
		loadedPlayer.kickPlayer(reason);
		UtilServer.broadcastTeamChatMessage("§cDer Spieler §e" + banned.getName() + "§c wurde vom §eAntiCheatSystem§c permanent gesperrt. Grund: §e" + reason);
	}
}
