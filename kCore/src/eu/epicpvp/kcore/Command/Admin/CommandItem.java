package eu.epicpvp.kcore.Command.Admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilItem;

public class CommandItem implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "item", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(player.isOp()){
				if(args.length==0){
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/item glow");
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/item add [Line]");
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/item clear");
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/item del [Line]");
				}else{
					if(args[0].equalsIgnoreCase("glow")){
						player.setItemInHand( UtilItem.addEnchantmentGlow(player.getItemInHand()) );
					}else if(args[0].equalsIgnoreCase("clear")){
						player.setItemInHand( UtilItem.SetDescriptions(player.getItemInHand(), new String[]{}) );
					}else if(args[0].equalsIgnoreCase("del")){
						StringBuilder sb = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							sb.append(args[i]);
							sb.append(" ");
						}
						sb.setLength(sb.length() - 1);
						String line = sb.toString().replaceAll("&", "§");
						
						ItemMeta im = player.getItemInHand().getItemMeta();
						if(!im.hasLore())im.setLore(new ArrayList<String>());
						List<String> list = im.getLore();
						for(int i = 0; i < list.size(); i++){
							if(list.get(i).contains(line)){
								list.remove(i);
								break;
							}
						}
						im.setLore(list);
						player.getItemInHand().setItemMeta(im);
						
					}else if(args[0].equalsIgnoreCase("add")){
						StringBuilder sb = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							sb.append(args[i]);
							sb.append(" ");
						}
						sb.setLength(sb.length() - 1);
						String line = sb.toString().replaceAll("&", "§");
						
						ItemMeta im = player.getItemInHand().getItemMeta();
						if(!im.hasLore())im.setLore(new ArrayList<String>());
						List<String> list = im.getLore();
						list.add(line);
						im.setLore(list);
						player.getItemInHand().setItemMeta(im);
					}
				}
			}
		}
		return false;
	}
	
}