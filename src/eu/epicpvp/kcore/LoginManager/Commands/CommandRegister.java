package eu.epicpvp.kcore.LoginManager.Commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.LoginManager.LoginManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.Title;
import lombok.Getter;

public class CommandRegister implements CommandExecutor{

	@Getter
	private LoginManager loginManager;
	
	public CommandRegister(LoginManager loginManager) {
		this.loginManager=loginManager;
	}


	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "register", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs,org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if(!(cs instanceof Player))return false;
		Player player = (Player)cs;
		
		if(!getLoginManager().getRegister().contains(player.getName().toLowerCase())){
			loginManager.logMessage("Register "+player.getName()+" is not on the list!");
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cDu musst erst das Jump And Run schaffen und dich auf die Gold platten stellen!");
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aDies ist nur ein Bot schutzt.");
			return false;
		}
		
		if(args.length==0){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§c/register [Password]");
			return true;
		}else{
			String password = args[0];
			if(password.isEmpty())return false;
			
			if(!password.matches("[a-zA-Z0-9_]*")){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "LOGIN_FAIL"));
				return false;
			}else{
				loginManager.logMessage("Der Spieler "+player.getName()+" hat sich Regestriert!");
				getLoginManager().getRegister().remove(player.getName().toLowerCase());
				LoadedPlayer loadedplayer = getLoginManager().getClient().getPlayerAndLoad(player.getName());
				loadedplayer.setPasswordSync(password);
				Title title = new Title("  "," ");
				title.resetTitle(player);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "REGISTER_ACCEPT"));
				
				getLoginManager().addPlayerToBGList(player);
				return true;
			}
		}
	}

}