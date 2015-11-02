package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandVersusMore implements CommandExecutor{

	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "more", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.getGameMode()==GameMode.CREATIVE){
			if(args.length==0){
				if(player.getItemInHand()!=null){
					player.getItemInHand().setAmount(64);
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "MORE_HAND"));
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_ITEM_IN_HAND"));
				}
			}else if(args[0].equalsIgnoreCase("all")){
				for(ItemStack item : player.getInventory().getContents())if(item!=null&&item.getType()!=Material.AIR)item.setAmount(64);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "MORE_INV"));
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+"/more");
			}
		}else{
			player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "VERSUS_CREATIV_AREA"));
		}
		return false;
	}
}
