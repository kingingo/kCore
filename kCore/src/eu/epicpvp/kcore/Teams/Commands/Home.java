package eu.epicpvp.kcore.Teams.Commands;

import static net.minecraft.server.v1_8_R3.PotionBrewer.g;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Teams.TeamRank;
import eu.epicpvp.kcore.Teams.Events.TeamPlayerTeleportedEvent;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilServer;

public class Home {
	public static boolean onSet(Player player, String[] args, TeamManager teamManager) {
		return onSet(player, player.getLocation(), args, teamManager);
	}

	public static boolean onSet(Player player, Location loc, String[] args, TeamManager teamManager) {
		if (args.length != 1) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde sethome");
			return true;
		}
		Team team = teamManager.getTeamIfLoaded(player);
		if (team == null) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
			return true;
		}
		if (team.getRank(player) != TeamRank.OWNER) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_OWNER_NOT"));
			return true;
		}

		if (teamManager.getTeamType() == GameType.TEAMS_SKYBLOCK) {
//			if(teamManager instanceof SkyBlockGildenManager){
//				SkyBlockGildenManager skymanager = (SkyBlockGildenManager)teamManager;
//				
//				if(skymanager.getSky().getGilden_world().getIslands().containsKey(g.toLowerCase())){
//					return true;
//				}
//				
//				if(player.hasPermission(PermissionType.SKYBLOCK_GILDEN_ISLAND.getPermissionToString())){
//					skymanager.getSky().addGildenIsland(player, g);
//					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_SETISLAND"));
//					return true;
//				}else{
//					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "NO_RANG"));
//					return true;
//				}
//			}
		}

		if (UtilDebug.isDebug()) {
			UtilDebug.debug("CMD:Home", new String[]{"Gilde:" + g, "PLAYER: " + player.getName()});
		}

		team.setStatistic(StatsKey.LOC_X, loc.getBlockX());
		team.setStatistic(StatsKey.LOC_Y, loc.getBlockY());
		team.setStatistic(StatsKey.LOC_Z, loc.getBlockZ());
		team.setStatistic(StatsKey.WORLD, loc.getWorld().getName());
		if (teamManager.getTeamType() == GameType.TEAMS_PVP) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_SETHOME"));
		}
		return true;
	}

	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length == 1) {
			Team team = teamManager.getTeamIfLoaded(player);
			if (team == null) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return true;
			}
			TeamPlayerTeleportedEvent ev = new TeamPlayerTeleportedEvent(player, team);
			Bukkit.getPluginManager().callEvent(ev);
			if (ev.isCancelled()) {
				if (ev.getCancelReason() != null) {
					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + ev.getCancelReason());
				}
				return true;
			}

			Location loc_to = getHome(team);

			Teleporter teleporter = new Teleporter(player, loc_to, 5);
			UtilServer.getTeleportManager().getTeleport().add(teleporter);
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_HOME", "5 sec"));
		} else if (args.length == 2) {
			if (!player.isOp()) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde " + args[0]);
				return true;
			}
			if (teamManager.getTeamType() == GameType.TEAMS_SKYBLOCK) {
//				SkyBlockGildenManager sky = (SkyBlockGildenManager)teamManager;
//				
//				if(sky.getSky().getGilden_world().getIslands().containsKey(args[1].toLowerCase())){
//					player.teleport(sky.getSky().getGilden_world().getIslandHome(args[1].toLowerCase()));
//					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aDu wurdest Teleporiert.");
//				}else{
//					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cGilde nicht gefunden");
//				}
			} else {
				teamManager.getTeam(args[1], team -> {
					if (team == null) {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§cGilde nicht gefunden");
						return;
					}
					player.teleport(getHome(team));
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§aDu wurdest Teleporiert.");
				});
			}
		} else {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde " + args[0]);
		}
		return true;
	}

	private static Location getHome(Team team) {
		double x = team.getStatisticDouble(StatsKey.LOC_X);
		double y = team.getStatisticDouble(StatsKey.LOC_Y);
		double z = team.getStatisticDouble(StatsKey.LOC_Z);
		String world = (String) team.getStatistic(StatsKey.WORLD);
		return new Location(Bukkit.getWorld(world), x, y, z);
	}
}
