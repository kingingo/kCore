package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;

public class CommandVersusDurability implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "durability",alias={"dura","du","dur"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.getGameMode()==GameMode.CREATIVE){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/durability [1-100 %]");
			}else{
				if(UtilNumber.isInt(args[0])){
					int pro = UtilNumber.toInt(args[0]);
					
					if(pro>=1&&pro<=100){
						if(player.getItemInHand()!=null){
							player.getItemInHand().setDurability( UtilMath.prozentRechnen(pro, player.getItemInHand().getType().getMaxDurability()) );
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_ITEM_IN_HAND"));
						}
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_INTEGER"));
					}
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_INTEGER"));
				}
			}
		}else{
			player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "VERSUS_CREATIV_AREA"));
		}
		return false;
	}
}
