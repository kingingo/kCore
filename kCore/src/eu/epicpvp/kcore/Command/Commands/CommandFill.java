package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandFill implements CommandExecutor{

	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "kfill", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
			
		if(player.hasPermission(PermissionType.FILL.getPermissionToString())){
			if(args.length==0){
				for(ItemStack i : player.getInventory().getContents()){
					if(i!=null&&i.getType()!=Material.AIR){
						if(i.getType()==Material.GLASS_BOTTLE){
							i.setType(Material.POTION);
						}
					}
				}
				player.sendMessage(TranslationHandler.getText(player,"PREFIX")+"§aDie Wasser Flaschen wurden bef§llt!");
			}
		}
		return false;
	}
}