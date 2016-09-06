package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandGiveGems implements CommandExecutor{
	
	private StatsManager money;
	
	public CommandGiveGems(StatsManager money){
		this.money=money;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "givegems", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§a/GiveGems [Spieler] [+/- Gems]");
				}else if(args.length >= 2){
					String spieler = args[0];
					int c=UtilInteger.isNumber(args[1]);
					
					if(c==-1)return false;

					UtilServer.getMoneyListener().update(spieler, StatsKey.GEMS, c);
					String translationId;
					if (c < 0) {
						translationId = "GEMS_DEL_PLAYER";
					} else {
						translationId = "GEMS_ADD_PLAYER";
					}
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, translationId, String.valueOf(c), spieler));
				}
			}
		}else if(sender instanceof ConsoleCommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /GiveGems [Spieler] [+/- Gems]");
			}else if(args.length >= 2){
				String spieler = args[0];
				
				int c=UtilInteger.isNumber(args[1]);
				
				if(c==-1)return false;
				
				UtilServer.getMoneyListener().update(spieler, StatsKey.GEMS, c);
				System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Gems erhalten!");
			}
		}else {
			System.out.println("Unknown command sender for /givegems command: " + sender.getClass().getSimpleName() + " " + sender);
		}
		return false;
	}
	
}
