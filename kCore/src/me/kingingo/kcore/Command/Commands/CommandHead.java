package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHead implements CommandExecutor{

	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "head",alias={"kopf","hat","hut"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
			
		if(player.hasPermission(kPermission.HEAD.getPermissionToString())){
			if(player.getItemInHand()!=null){
				if(player.getItemInHand().getType().isBlock()){
					if(player.getInventory().getHelmet()==null){
						player.getInventory().setHelmet(player.getItemInHand());
						player.setItemInHand(null);
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HEAD"));
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HEAD_ITEM_NOT_BLOCK"));
					}
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HEAD_ITEM_NOT_BLOCK"));
				}
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HEAD_ITEM_EQUAL_NULL"));
			}
		}
		return false;
	}
}
