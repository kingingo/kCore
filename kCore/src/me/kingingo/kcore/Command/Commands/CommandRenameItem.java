package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
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
				player.sendMessage(Text.PREFIX.getText()+"/renameitem [Name]");
			}else{
				if(player.getItemInHand()!=null&&player.getItemInHand().getType()!=Material.AIR){
					UtilItem.RenameItem(player.getItemInHand(), args[0].replaceAll("&", "§"));
					player.sendMessage(Text.PREFIX.getText()+Text.ITEM_RENAME.getText());
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.NO_ITEM_IN_HAND.getText());
				}
			}
		}
		return false;
	}

}
