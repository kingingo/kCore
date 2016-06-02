package eu.epicpvp.kcore.Command.Admin;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandBanned implements CommandExecutor{
	
//	private MySQL mysql;
	
	public CommandBanned(MySQL mysql){
//		this.mysql=mysql;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "kban", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(args.length==1){
			LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(args[0]);
			loadedplayer.banPlayer("", "CONSOLE", "CONSOLE", UUID.randomUUID(), 5, -1, "CHARGEBACK");
//			Date MyDate = new Date();
//			SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//			df2.setTimeZone(TimeZone.getDefault());
//			df2.format(MyDate);
//			Calendar gc2 = new GregorianCalendar();
//			Date now = gc2.getTime();
//			UUID uuid = UtilPlayer.getUUID(args[0], mysql);
//			mysql.Update("INSERT INTO BG_Ban (name,name_uuid, nameip,banner,banner_uuid,bannerip,time,reason, level,aktiv) VALUES ('" + args[0].toLowerCase() + "','"+uuid+"', 'null', 'CONSOLE','CONSOLE', 'null', '" + df2.format(now) + "', 'CHARGEBACK', '5','true')");
//			System.err.println("[kBan] "+args[0]+" wurde gebannt");
		}
		return false;
	}
	
}
