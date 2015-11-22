package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.Color;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSuffix implements CommandExecutor{

	private Player player;
	private String color;
	private UserDataConfig userData;
	
	public CommandSuffix(UserDataConfig userData){
		this.userData=userData;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "suffix", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(player.hasPermission(kPermission.SUFFIX.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/suffix &COLOR");
			}else{
				if(args[0].equalsIgnoreCase("reset")){
					this.userData.getConfig(player).set("Chat.Suffix", null);
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SUFFIX_RESET"));
					return true;
				}else{
					this.color = args[0];
					
					if(color.contains("&")){
						this.color=this.color.replaceAll("&", "�");
						
						if(this.color.length()>=2){
							if(this.color.length()<=10){
								if(Color.isColor(this.color)){
									this.userData.getConfig(player).set("Chat.Suffix", this.color);
									player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SUFFIX_SAVE"));
									return true;
								}else{
									player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_COLOR_CODE"));
								}
							}else{
								player.sendMessage(Language.getText(player,"PREFIX")+Language.getText(player, "SUFFIX_TO_LONG"));
							}
						}else{
							player.sendMessage(Language.getText(player,"PREFIX")+Language.getText(player, "SUFFIX_TO_SHORT"));
						}
						return false;
					}
					
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_COLOR_CODE"));
				}
			}
			return true;
		}
		return false;
	}
}
