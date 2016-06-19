package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandVersusMore implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "more", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(player.getGameMode()==GameMode.CREATIVE){
			if(args.length==0){
				if(player.getItemInHand()!=null){
					player.getItemInHand().setAmount(64);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "MORE_HAND"));
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_ITEM_IN_HAND"));
				}
			}else if(args[0].equalsIgnoreCase("all")){
				for(ItemStack item : player.getInventory().getContents())if(item!=null&&item.getType()!=Material.AIR)item.setAmount(64);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "MORE_INV"));
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/more");
			}
		}else{
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "VERSUS_CREATIV_AREA"));
		}
		return false;
	}
}
