package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandGivePro implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "givepro", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§a/givepro [Spieler] [Path] [+/- Value]");
				}else if(args.length >= 2){
					String spieler = args[0];
					int c=UtilInteger.isNumber(args[2]);

					if(c==-1)return false;

					UtilServer.getMoneyListener().update(spieler, StatsKey.PROPERTIES,args[1], c);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+(TranslationHandler.getText(player, c<0?"GEMS_DEL_PLAYER":"GEMS_ADD_PLAYER",new String[]{player.getName(),String.valueOf(Math.abs(c))})));
				}
			}
		}else if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /givepro [Spieler] [Path] [+/- Value]");
			}else if(args.length >= 2){
				String spieler = args[0];

				int c=UtilInteger.isNumber(args[2]);

				if(c==-1)return false;

				UtilServer.getMoneyListener().update(spieler, StatsKey.PROPERTIES,args[1], c);
				System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Properties erhalten!");
			}
		}
		return false;
	}

}
