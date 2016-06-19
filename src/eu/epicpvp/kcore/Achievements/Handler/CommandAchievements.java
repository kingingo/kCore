package eu.epicpvp.kcore.Achievements.Handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Util.UtilInv;
import lombok.Getter;

public class CommandAchievements implements CommandExecutor{

	@Getter
	private AchievementsHandler handler;
	
	public CommandAchievements(AchievementsHandler handler){
		this.handler=handler;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "achievements", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player player = (Player)cs;
		handler.getInventory().open(player, UtilInv.getBase());
		return true;
	}	
}
