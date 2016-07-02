package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import me.konsolas.aac.api.AACAPIProvider;

public class CommandReport implements CommandExecutor {

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "report", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(args.length==0){
			System.out.println("/report [Player] [Reason]");
		}else{
				String playername = args[0];
				
				if(UtilPlayer.isOnline(playername)){
					Player player = Bukkit.getPlayer(playername);
					LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
					
					if(loadedplayer.getPlayerId()>0){
						StringBuilder sb = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							sb.append(args[i]);
							sb.append(" ");
						}
						sb.setLength(sb.length() - 1);
						String reason = sb.toString();

						UtilServer.getClient().createReport(
								UtilServer.getClient().getPlayerAndLoad("DasAntiHackSystem").getPlayerId(),
								loadedplayer.getPlayerId(), reason,
								"Player Ping: "+AACAPIProvider.getAPI().getPing(player)+" Server-TPS: "+UtilMath.trim(2, AACAPIProvider.getAPI().getTPS()));
						System.out.println("[ClashMC]: Der Report gegen "+player.getName()+" wurde erstellt! Grund: "+reason);
					}
				}
			
		}
		
		return false;
	}
}
