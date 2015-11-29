package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandResetKit implements CommandExecutor{

	private CommandKit kit;
	private Player player;
	
	public CommandResetKit(CommandKit kit){
		this.kit=kit;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "resetkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT_RESET.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/resetkit [Name]");
			}else{
				String kit_player=args[0].toLowerCase();
				if(kit.getKits().containsKey(kit_player)){
					//epicpvp.kit.use.starter
					if(player.hasPermission(kPermission.KIT.getPermissionToString()+"."+kit_player)){
						if(kit.getKits().containsKey(kit_player)&&!player.hasPermission(kPermission.KIT_BYEPASS_DELAY.getPermissionToString())){
							kit.getUserData().getConfig(player).set("timestamps.kits."+kit_player, kit.getKits_delay().get(kit_player)+System.currentTimeMillis());
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_DELAY",UtilTime.formatMili(kit.getUserData().getConfig(player).getLong("timestamps.kits."+kit_player)-System.currentTimeMillis())));
						}
					}
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_EXIST"));
				}
			}
		}
		return false;
	}

}
