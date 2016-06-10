package eu.epicpvp.kcore.Command.Admin;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import me.konsolas.aac.api.AACAPIProvider;
import net.md_5.bungee.api.ChatColor;

public class CommandReport implements CommandExecutor {

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "report", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(args.length==0){
			System.out.println("/report [Player] [Reason]");
		}else{
			if(args.length>1){
				String playername = args[0];
				
				if(UtilPlayer.isOnline(playername)){
					Player player = Bukkit.getPlayer(playername);
					LoadedPlayer loadedplayer = UtilServer.getClient().getPlayer(player.getName());
					String reason = ChatColor.translateAlternateColorCodes('&', join(Arrays.copyOfRange(args, 2, args.length)," "));

					UtilServer.getClient().createReport(
							UtilServer.getClient().getPlayerAndLoad("DasAntiHackSystem").getPlayerId(),
							loadedplayer.getPlayerId(), reason,
							"Player Ping:"+AACAPIProvider.getAPI().getPing(player)+" Server-TPS:"+AACAPIProvider.getAPI().getTPS());
					System.out.println("[ClashMC]: Der Report gegen "+player.getName()+" wurde erstellt! Grund: "+reason);
				}
			}
		}
		
		return false;
	}
	
	private String join(String[] copyOfRange, String string) {
		String out = "";
		for(String s : copyOfRange)
			out+=string+s;
		return out.substring(string.length());
	}
}
