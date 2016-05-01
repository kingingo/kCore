package eu.epicpvp.kcore.Teams;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeam implements CommandExecutor{
	
	private TeamManager teamManager;
	
	public CommandTeam(TeamManager teamManager){
		this.teamManager=teamManager;
	}
	
	@Override
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "team", alias = {"g", "c", "guild", "clan", "gild", "gilden", "gilde", "teams"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage("§b---------------------§§6§l GILDE §b---------------------");
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD1"));
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD2"));
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD3"));
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD4"));
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD5"));
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD6"));
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD7"));
			p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD14"));
			if(teamManager.getTeamType()== GameType.TEAMS_PVP){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD8"));
				p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD9"));
			}else if(teamManager.getTeamType()== GameType.TEAMS_SKYBLOCK){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD10"));
				p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD11"));
				p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD12"));
				p.sendMessage(TranslationHandler.getText(p, "GILDE_CMD13"));
			}
			p.sendMessage("§b§ ---------------------6§l GILDE §b---------------------");
		}else if(args.length > 0){
			if(args[0].equalsIgnoreCase("erstellen")||args[0].equalsIgnoreCase("create")){
				
			}
		}
		return false;
	}
	
}

