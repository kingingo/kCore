package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev.wolveringer.dataclient.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandAddEpics implements CommandExecutor{
	
	private StatsManager statsManager;
	
	public CommandAddEpics(StatsManager statsManager){
		this.statsManager=statsManager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "addepics", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /addepics [Spieler] [+/- Epics]");
			}else if(args.length >= 2){
				String spieler = args[0];
				
				double c=UtilNumber.toDouble(args[1]);
				
				if(c==-1)return false;
				
				if(UtilPlayer.isOnline(spieler)){
					statsManager.addDouble(Bukkit.getPlayer(spieler), c, StatsKey.MONEY);
					System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Epics erhalten!");
				}else{
					System.out.println("[EpicPvP]: Der Spieler "+spieler+" ist nicht online!");
				}
			}
		}
		return false;
	}
	
}
