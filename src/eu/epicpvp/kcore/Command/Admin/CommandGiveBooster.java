package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandGiveBooster implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "givebooster", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§a/givebooster [Spieler] [Zeit in Sekunden]");
				}else if(args.length >= 2){
					if(add(args));
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"Der Spieler "+args[0]+" hat die "+args[1]+" Booster Zeit bekommen!");
				}
			}
		}else if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /givebooster [Spieler] [Zeit in Sekunden]");
			}else if(args.length >= 2){
				add(args);
			}
		}
		return false;
	}

	public boolean add(String[] args){
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(args[0]);

		int sec = UtilInteger.isNumber(args[1]);
		if(sec==-1)return false;

		loadedplayer.addBoosterTime( 1000 * sec );
		System.out.println("[EpicPvP]: Der Spieler "+loadedplayer.getName()+" hat die "+sec+" Booster Zeit bekommen!");
		return true;
	}
}
