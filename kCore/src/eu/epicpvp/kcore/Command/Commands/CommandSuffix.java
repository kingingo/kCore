package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.Color;

public class CommandSuffix implements CommandExecutor{

	private Player player;
	private String color;
	private UserDataConfig userData;
	
	public CommandSuffix(UserDataConfig userData){
		this.userData=userData;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "suffix", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(player.hasPermission(PermissionType.SUFFIX.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/suffix &COLOR");
			}else{
				if(args[0].equalsIgnoreCase("reset")){
					this.userData.getConfig(player).set("Chat.Suffix", null);
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "SUFFIX_RESET"));
					return true;
				}else{
					this.color = args[0];
					
					if(color.contains("&")){
						this.color=this.color.replaceAll("&", "§");
						
						if(this.color.length()>=2){
							if(this.color.length()<=10){
								if(Color.isColor(this.color)){
									this.userData.getConfig(player).set("Chat.Suffix", this.color);
									player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "SUFFIX_SAVE"));
									return true;
								}else{
									player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "NOT_COLOR_CODE"));
								}
							}else{
								player.sendMessage(TranslationManager.getText(player,"PREFIX")+TranslationManager.getText(player, "SUFFIX_TO_LONG"));
							}
						}else{
							player.sendMessage(TranslationManager.getText(player,"PREFIX")+TranslationManager.getText(player, "SUFFIX_TO_SHORT"));
						}
						return false;
					}
					
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "NOT_COLOR_CODE"));
				}
			}
			return true;
		}
		return false;
	}
}