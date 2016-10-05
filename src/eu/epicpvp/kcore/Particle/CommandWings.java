package eu.epicpvp.kcore.Particle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Util.UtilInv;

public class CommandWings implements CommandExecutor{
	
	private WingShop wings;
	
	public CommandWings(WingShop wings){
		this.wings=wings;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "wings", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		wings.open(p, UtilInv.getBase());
		return false;
	}
	
}
