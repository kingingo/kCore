package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilItem;

public class CommandRenameItem implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "renameitem",alias={"rename","ri"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		
		if(player.hasPermission(PermissionType.RENAMEITEM.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/renameitem [Name]");
			}else{
				if(player.getItemInHand()!=null&& player.getItemInHand().getType()!=Material.AIR){
					UtilItem.RenameItem(player.getItemInHand(), args[0].replaceAll("_", " ").replaceAll("&", "§"));
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "ITEM_RENAME"));
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_ITEM_IN_HAND"));
				}
			}
		}
		return false;
	}

}
