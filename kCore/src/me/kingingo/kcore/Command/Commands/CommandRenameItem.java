package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRenameItem implements CommandExecutor{
	
	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "renameitem",alias={"rename","ri"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(kPermission.RENAMEITEM.getPermissionToString())){
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
