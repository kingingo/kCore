package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilItem;

public class CommandRenameItem implements CommandExecutor{
	
	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "renameitem",alias={"rename","ri"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(PermissionType.RENAMEITEM.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/renameitem [Name]");
			}else{
				if(player.getItemInHand()!=null&&player.getItemInHand().getType()!=Material.AIR){
					UtilItem.RenameItem(player.getItemInHand(), args[0].replaceAll("&", "§"));
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "ITEM_RENAME"));
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_ITEM_IN_HAND"));
				}
			}
		}
		return false;
	}

}
