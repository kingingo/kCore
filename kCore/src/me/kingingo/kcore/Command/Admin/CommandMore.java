package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandMore implements CommandExecutor{
	
	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "more", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.MORE.getPermissionToString())){
			if(args.length==0){
				if(player.getItemInHand()!=null){
					player.getItemInHand().setAmount(64);
					player.sendMessage(Text.PREFIX.getText()+Text.MORE_HAND.getText());
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.NO_ITEM_IN_HAND.getText());
				}
			}else if(player.hasPermission(kPermission.MORE_ALL.getPermissionToString())&&args[0].equalsIgnoreCase("all")){
				for(ItemStack item : player.getInventory().getContents())if(item!=null&&item.getType()!=Material.AIR)item.setAmount(64);
				player.sendMessage(Text.PREFIX.getText()+Text.MORE_INV.getText());
			}else{
				player.sendMessage(Text.PREFIX.getText()+"/more");
			}
		}
		return false;
	}
	
}
