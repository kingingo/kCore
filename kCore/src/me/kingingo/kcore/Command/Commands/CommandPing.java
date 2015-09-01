package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPing implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "ping", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		((Player)sender).sendMessage(Language.getText(((Player)sender), "PREFIX")+"Player-Ping: §e"+UtilPlayer.getPlayerPing(((Player)sender))+"§7 Server-TPS: §e"+(int)UtilServer.getLagMeter().getTicksPerSecond());
		return false;
	}

}
