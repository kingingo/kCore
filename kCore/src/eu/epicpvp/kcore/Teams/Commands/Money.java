package eu.epicpvp.kcore.Teams.Commands;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Teams.TeamRank;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilNumber;
import org.bukkit.entity.Player;

import static net.minecraft.server.v1_8_R3.PotionBrewer.g;

public class Money {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length == 3) {
			Team team = teamManager.getTeamIfLoaded(player);
			if (team == null) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return true;
			}

			if (args[1].equalsIgnoreCase("abheben") || args[1].equalsIgnoreCase("take") || args[1].equalsIgnoreCase("withdraws")) {
				if (team.getRank(player) == TeamRank.OWNER) {
					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_OWNER_NOT"));
					return true;
				}
				double money = UtilNumber.toDouble(args[2]);
				if (money == 0) {
					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "NO_INTEGER"));
				} else {
					if (money < 0) {
						return true;
					}

					double teamMoney = team.getStatisticDouble(StatsKey.MONEY);
					if (teamMoney < money) {
						player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_NOT_ENOUGH_MONEY"));
					} else {
						System.err.println("[GildenManager] Gilde:" + team.getName());
						System.err.println("[GildenManager] Kontostand-Gilde: " + teamMoney);
						System.err.println("[GildenManager] Kontostand-Player: " + teamManager.getServerStatsManager().getDouble(player, StatsKey.MONEY));
						System.err.println("[GildenManager] Einzahlen: " + money);
						team.setStatistic(StatsKey.MONEY, teamMoney - money);
						teamManager.getServerStatsManager().add(player, StatsKey.MONEY, money);
						team.broadcast("GILDE_MONEY_LIFTED", player.getName(), String.valueOf(money));
						System.err.println("[GildenManager] Kontostand-Gilde danach: " + team.getStatisticDouble(StatsKey.MONEY));
						System.err.println("[GildenManager] Kontostand-Player danach: " + teamManager.getServerStatsManager().getDouble(player, StatsKey.MONEY));
					}
				}
			} else if (args[1].equalsIgnoreCase("einzahlen") || args[1].equalsIgnoreCase("pay") || args[1].equalsIgnoreCase("deposite")) {
				double money = UtilNumber.toDouble(args[2]);
				if (money == 0) {
					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "NO_INTEGER"));
				} else {
					if (money < 0) {
						return true;
					}

					if (teamManager.getServerStatsManager().getDouble(player, StatsKey.MONEY) < money) {
						player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_NOT_ENOUGH_MONEY"));
					} else {
						System.err.println("[GildenManager] Gilde:" + g);
						System.err.println("[GildenManager] Kontostand-Gilde: " + team.getStatistic(StatsKey.MONEY));
						System.err.println("[GildenManager] Kontostand-Player: " + teamManager.getServerStatsManager().getDouble(player, StatsKey.MONEY));
						System.err.println("[GildenManager] Einzahlen: " + money);
						team.setStatistic(StatsKey.MONEY, team.getStatisticDouble(StatsKey.MONEY) + money);
						teamManager.getServerStatsManager().add(player, StatsKey.MONEY, -money);
						System.err.println("[GildenManager] Kontostand-Gilde danach: " + team.getStatistic(StatsKey.MONEY));
						System.err.println("[GildenManager] Kontostand-Player danach: " + teamManager.getServerStatsManager().getDouble(player, StatsKey.MONEY));
						team.broadcast("GILDE_MONEY_DEPOSIT", player.getName(), String.valueOf(money));
					}
				}
			} else {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde money abheben [Money]");
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde money einzahlen [Money]");
			}
		} else {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde money abheben [Money]");
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde money einzahlen [Money]");
		}
		return true;
	}
}
