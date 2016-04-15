package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.CommandKit;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandGiveKit implements CommandExecutor{

	private CommandKit kitCMD;
	
	public CommandGiveKit(CommandKit kitCMD){
		this.kitCMD=kitCMD;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "givekit", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {

			if(args.length==0){
				System.out.println("/givekit [Player] [Name]");
			}else{
				String playername = args[0];
				
				if(UtilPlayer.isOnline(playername)){
					String kit = args[1].toLowerCase();
					if(kitCMD.getKits().containsKey(kit)){
						for(ItemStack i : kitCMD.getKits().get(kit)){
							if(i!=null&&i.getType()!=Material.AIR){
								Bukkit.getPlayer(playername).getInventory().addItem(i.clone());
							}
						}
						System.out.println("Der Spieler "+playername+" hat das Kit "+kit+" erhalten!");
					}else{
						System.out.println("Dieses Kit wurde nicht gefunden!");
					}
				}
			}
		
		return false;
	}

}
