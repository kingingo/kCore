package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandGiveShards implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "giveshards", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§a/givesharps [Spieler] [+/- Sharps]");
				}else if(args.length >= 2){
					String spieler = args[0];
					int c=UtilInteger.isNumber(args[1]);
					
					if(c==-1)return false;

					UtilServer.getMoneyListener().update(spieler, StatsKey.MYSTERY_SHARPS, c);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+(c<0?TranslationHandler.getText(player, "GEMS_DEL_PLAYER",new String[]{player.getName(),String.valueOf(c)}):TranslationHandler.getText(player, "GEMS_ADD_PLAYER",new String[]{player.getName(),String.valueOf(c)})));
				}
			}
		}else if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /GiveGems [Spieler] [+/- Sharps]");
			}else if(args.length >= 2){
				String spieler = args[0];
				
				int c=UtilInteger.isNumber(args[1]);
				
				if(c==-1)return false;
				
				UtilServer.getMoneyListener().update(spieler, StatsKey.MYSTERY_SHARPS, c);
				System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Gems erhalten!");
			}
		}
		return false;
	}
	
}
