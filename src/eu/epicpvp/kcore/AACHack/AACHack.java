package eu.epicpvp.kcore.AACHack;

import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.connection.PacketListener;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import eu.epicpvp.kcore.AntiLogout.AntiLogoutManager;
import eu.epicpvp.kcore.Command.Admin.CommandReport;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Packets.PacketAACReload;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;
import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationCommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class AACHack extends kListener{

	@Getter
	private JavaPlugin instance;
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
		this.instance = mysql.getInstance();
		this.server = server;
		UtilServer.getCommandHandler().register(CommandReport.class, new CommandReport());
		
		UtilServer.getClient().getHandle().getHandlerBoss().addListener(new PacketListener() {
			
			@Override
			public void handle(Packet packet) {
				if(packet instanceof PacketAACReload){
					AACAPIProvider.getAPI().reloadAAC();
				}
			}
		});
		
		getMysql().Update("CREATE TABLE IF NOT EXISTS AAC_HACK(playerId int,ip varchar(30),server varchar(30),timestamp timestamp,hackType varchar(30),violations int)");
		logMessage("AACHack System aktiviert");
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerViolationKick(PlayerViolationCommandEvent ev){
		if (!ev.getCommand().contains("kick")) {
			return;
		}
		getMysql().Update("INSERT INTO AAC_HACK (playerId,ip,server,hackType,violations) VALUES ('" + UtilPlayer.getPlayerId(ev.getPlayer()) + "','" + ev.getPlayer().getAddress().getAddress().getHostAddress() + "','" + server + "','" + ev.getHackType().getName() + "','0');");

		if (getAntiLogoutManager() != null && ev.getHackType() != HackType.SPAM) {
			getAntiLogoutManager().del(ev.getPlayer());
		}

		if(ev.getHackType() == HackType.FLY
			|| ev.getHackType() == HackType.FASTBOW
			|| ev.getHackType() == HackType.FIGHTSPEED
			|| ev.getHackType() == HackType.KILLAURA
			|| ev.getHackType() == HackType.ANGLE
			|| ev.getHackType() == HackType.FORCEFIELD) {

			HackType hackType = ev.getHackType();

			if (hackType == HackType.KILLAURA) { //they're similar hacks
				hackType = HackType.FORCEFIELD;
			}

			int anzahl = getMysql().getInt("SELECT COUNT(*) FROM AAC_HACK WHERE hackType='" + hackType.getName() + "' AND playerId='" + UtilPlayer.getPlayerId(ev.getPlayer()) + "'");

			if (anzahl >= 5) {
				String type = "";
				int a = 0;
				if (anzahl <= 15) {
					a = anzahl * 2;
					type = "min";
				} else if (anzahl <= 20) {
					a = anzahl;
					type = "std";
				} else {
					a = anzahl / 2;
					type = "tag";
				}

				setZeitBan(ev.getPlayer(), a, type, ev.getHackType().getName());
				ev.setCancelled(true);
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
		loadedplayer.banPlayer(banned.getAddress().getHostName(), "AAC", "AAC", null, 2, time, reason);
		loadedplayer.kickPlayer(reason);
		UtilServer.sendTeamMessage("§cDer Spieler §e" + banned.getName() + "§c wurde vom §eAntiHackSystem§c für §e" + ti + " " + typ.toUpperCase() + " §cgesperrt. Grund: §e" + reason);
	}

	private void setBan(int lvl, Player banned, String reason) {
		LoadedPlayer loadedplayer = client.getPlayerAndLoad(banned.getName());
		loadedplayer.banPlayer(banned.getAddress().getHostName(), "AAC", "AAC", null, 2, -1, reason);
		loadedplayer.kickPlayer(reason);
		UtilServer.sendTeamMessage("§cDer Spieler §e" + banned.getName() + "§c wurde vom §eAntiHackSystem§c permanent gesperrt. Grund: §e" + reason);
	}
}
