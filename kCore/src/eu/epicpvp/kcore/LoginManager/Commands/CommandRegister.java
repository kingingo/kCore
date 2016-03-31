package eu.epicpvp.kcore.LoginManager.Commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.LoginManager.LoginManager;
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
		
		if(!getLoginManager().getRegister().contains(player.getName().toLowerCase()))return false;
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"§c/register [Password]");
			return true;
		}else{
			String password = args[0];
			
			if(!password.matches("[a-zA-Z0-9_]*")){
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "LOGIN_FAIL"));
				return false;
			}else{
				getLoginManager().getRegister().remove(player.getName().toLowerCase());
				LoadedPlayer loadedplayer = getLoginManager().getClient().getPlayerAndLoad(player.getName());
				loadedplayer.setPasswordSync(password);
				Title title = new Title("","");
				title.send(player);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "REGISTER_ACCEPT"));
				
				getLoginManager().addPlayerToBGList(player);
				return true;
			}
		}
	}

}