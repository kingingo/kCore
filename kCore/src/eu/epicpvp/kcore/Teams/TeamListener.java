package eu.epicpvp.kcore.Teams;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Teams.Events.TeamPlayerJoinEvent;
import eu.epicpvp.kcore.Teams.Events.TeamPlayerQuitEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeamListener extends kListener {
	private TeamManager teamManager;
	private StatsManager statsManager;

	public TeamListener(TeamManager teamManager) {
		super(teamManager.getInstance(), "TeamListener");
		this.teamManager = teamManager;
		statsManager = StatsManagerRepository.getStatsManager(teamManager.getInstance(), teamManager.getServerType());
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev) {
		int teamId = statsManager.getInt(ev.getPlayer(), StatsKey.TEAM_ID);

		if (teamId > 0) {
			Team team = teamManager.getTeam(teamId);

			if (team != null) {
				team.broadcast("GILDE_PLAYER_LEAVE", ev.getPlayer().getName());
				Bukkit.getPluginManager().callEvent(new TeamPlayerQuitEvent(team, ev.getPlayer()));
			}
		}
	}

	@EventHandler
	public void join(PlayerStatsLoadedEvent ev) {
		if (ev.getManager().getType() == teamManager.getServerType()) {
			if (ev.getManager().containsKey(ev.getPlayerId(), StatsKey.TEAM_ID)) {
				int teamId = ev.getManager().getInt(ev.getPlayerId(), StatsKey.TEAM_ID);

				if (teamId > 0) {
					Team team = teamManager.getTeam(teamId);
					Player player = UtilPlayer.searchExact(ev.getPlayerId());

					if (team != null && player != null) {
						team.broadcast("GILDE_PLAYER_JOIN", player.getName());
						Bukkit.getPluginManager().callEvent(new TeamPlayerJoinEvent(team, player));
					}
				}
			}
		}
	}
}
