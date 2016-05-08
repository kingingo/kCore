package eu.epicpvp.kcore.Teams;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Teams.Commands.Accept;
import eu.epicpvp.kcore.Teams.Commands.Create;
import eu.epicpvp.kcore.Teams.Commands.Home;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandTeam implements CommandExecutor {
	private TeamManager teamManager;

	public CommandTeam(TeamManager teamManager) {
		this.teamManager = teamManager;
	}

	@Override
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "team", alias = {"g", "c", "guild", "clan", "gild", "gilden", "gilde", "teams"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player player = (Player) cs;
		if (args.length == 0) {
			player.sendMessage("§b---------------------§§6§l GILDE §b---------------------");
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD1"));
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD2"));
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD3"));
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD4"));
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD5"));
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD6"));
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD7"));
			player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD14"));
			if (teamManager.getTeamType() == GameType.TEAMS_PVP) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD8"));
				player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD9"));
			} else if (teamManager.getTeamType() == GameType.TEAMS_SKYBLOCK) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD10"));
				player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD11"));
				player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD12"));
				player.sendMessage(TranslationHandler.getText(player, "GILDE_CMD13"));
			}
			player.sendMessage("§b§ ---------------------6§l GILDE §b---------------------");
			return true;
		}
		switch (args[0].toLowerCase()) {
			case "accept":
			case "annehmen": {
				Accept.on(player, args, teamManager);
				break;
			}
			case "erstellen":
			case "create": {
				Create.on(player, args, teamManager);
				break;
			}
			case "basis":
			case "base":
			case "home": {
				Home.on(player, args, teamManager);
				break;
			}
			case "setbase":
			case "setbasis":
			case "baseset":
			case "homeset":
			case "sethome": {
				Home.onSet(player, args, teamManager);
				break;
			}
			case "info": {
				Home.on(player, args, teamManager);
				break;
			}
			case "einladen":
			case "invite": {
				Home.on(player, args, teamManager);
				break;
			}
			case "kicken":
			case "kick": {
				Home.on(player, args, teamManager);
				break;
			}
			case "verlassen":
			case "leave": {
				Home.on(player, args, teamManager);
				break;
			}
			case "geld":
			case "epics":
			case "money": {
				Home.on(player, args, teamManager);
				break;
			}
			case "rang":
			case "ranking": {
				Home.on(player, args, teamManager);
				break;
			}
		}
		return true;
	}
}

