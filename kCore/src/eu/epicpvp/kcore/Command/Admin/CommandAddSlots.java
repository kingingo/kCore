package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.DisplaySlot;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandAddSlots implements CommandExecutor{
	
	private UserDataConfig userData;
	
	public CommandAddSlots(UserDataConfig userData){
		this.userData=userData;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "addslots", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /addslots [Spieler] [+/- Slots]");
			}else if(args.length >= 2){
				String spieler = args[0];
				
				int c=UtilInteger.isNumber(args[1]);
				
				if(c==-1)return false;
				
				if(UtilPlayer.isOnline(spieler)){
					kConfig config = userData.getConfig(Bukkit.getPlayer(spieler));
					config.set("Stores", config.getInt("Stores")+c);
					config.save();
					UtilScoreboard.resetScore(Bukkit.getPlayer(spieler).getScoreboard(), 2, DisplaySlot.SIDEBAR);
					UtilScoreboard.setScore(Bukkit.getPlayer(spieler).getScoreboard(), ""+config.getInt("Stores"), DisplaySlot.SIDEBAR, 2);
					System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Slots erhalten!");
				}else{
					System.out.println("[EpicPvP]: Der Spieler "+spieler+" ist nicht online!");
				}
			}
		}
		return false;
	}
	
}
