package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandHead implements CommandExecutor{

	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "head",alias={"kopf","hat","hut"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
			
		if(player.hasPermission(PermissionType.HEAD.getPermissionToString())){
			if(player.getItemInHand()!=null){
				if(player.getItemInHand().getType().isBlock()){
					if(player.getInventory().getHelmet()==null){
						player.getInventory().setHelmet(player.getItemInHand());
						player.setItemInHand(null);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAD"));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAD_ITEM_NOT_BLOCK"));
					}
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAD_ITEM_NOT_BLOCK"));
				}
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAD_ITEM_EQUAL_NULL"));
			}
		}
		return false;
	}
}
