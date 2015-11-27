package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandFill implements CommandExecutor{

	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "fill", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
			
		if(player.hasPermission(kPermission.FILL.getPermissionToString())){
			if(args.length==0){
				for(ItemStack i : player.getInventory().getContents()){
					if(i!=null&&i.getType()!=Material.AIR){
						if(i.getType()==Material.GLASS_BOTTLE){
							i.setType(Material.POTION);
						}
					}
				}
				player.sendMessage(Language.getText(player,"PREFIX")+"§aDie Wasser Flaschen wurden befüllt!");
			}
		}
		return false;
	}
}