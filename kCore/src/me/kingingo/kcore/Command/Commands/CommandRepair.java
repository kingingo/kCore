package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRepair implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "repair",alias={"rp"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.REPAIR.getPermissionToString())){
			if(args.length==0){
				if(player.hasPermission(kPermission.REPAIR_HAND.getPermissionToString())){
					UtilItem.RepairItem(player.getItemInHand());
					player.sendMessage(Text.PREFIX.getText()+Text.REPAIR_HAND.getText());
				}
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(player.hasPermission(kPermission.REPAIR_ALL.getPermissionToString())){
						if(player.hasPermission(kPermission.REPAIR_BODY.getPermissionToString())){
							UtilInv.repairInventory(player, true);
						}else{
							UtilInv.repairInventory(player, false);
						}
						player.sendMessage(Text.PREFIX.getText()+Text.REPAIR_ALL.getText());
					}
				}
			}
		}
		return false;
	}

}
