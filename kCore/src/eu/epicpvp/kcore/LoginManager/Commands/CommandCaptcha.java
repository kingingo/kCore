package eu.epicpvp.kcore.LoginManager.Commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.LoginManager.LoginManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.Title;
import lombok.Getter;

public class CommandCaptcha implements CommandExecutor{

	@Getter
	private LoginManager loginManager;
	
	public CommandCaptcha(LoginManager loginManager) {
		this.loginManager=loginManager;
	}


	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "captcha", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs,org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if(!(cs instanceof Player))return false;
		Player player = (Player)cs;
		
		if(!getLoginManager().getCaptcha().containsKey(player.getName()))return false;
		if(args.length==0){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§c/captcha [CAPTCHA]");
			return true;
		}else{
			if(args[0].equalsIgnoreCase(getLoginManager().getCaptcha().get(player.getName()))){
				getLoginManager().getCaptcha().remove(player.getName());
				getLoginManager().getRegister().add(player.getName());
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "REGISTER_MESSAGE"));
			}else{
				player.kickPlayer(TranslationHandler.getText(player, "CAPTCHA_FALSE"));
			}
			return true;
		}
	}

}