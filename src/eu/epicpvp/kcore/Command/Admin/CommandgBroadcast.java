package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.epicpvp.datenclient.client.ClientWrapper;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;

public class CommandgBroadcast implements CommandExecutor{

	private ClientWrapper client;

	public CommandgBroadcast(ClientWrapper client){
		this.client=client;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "gbroadcast", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
			String m = "";
			for(int i = 0; i < args.length; i++)m =m + args[i] + " ";
            client.broadcastMessage(null, m);
		return false;
	}

}
