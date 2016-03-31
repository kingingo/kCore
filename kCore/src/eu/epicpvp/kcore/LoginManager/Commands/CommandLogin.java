package eu.epicpvp.kcore.LoginManager.Commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.LoginManager.LoginManager;
import eu.epicpvp.kcore.Util.Title;
import lombok.Getter;

public class CommandLogin implements CommandExecutor{

	@Getter
	private LoginManager loginManager;
	
	public CommandLogin(LoginManager loginManager) {
		this.loginManager=loginManager;
	}


	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "login", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs,org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if(!(cs instanceof Player))return false;
		Player player = (Player)cs;
		
		if(!getLoginManager().getLogin().containsKey(player.getName().toLowerCase()))return false;
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"§c/login [Password]");
			return true;
		}else{
			if(args[0].equalsIgnoreCase(getLoginManager().getLogin().get(player.getName().toLowerCase()))){
				getLoginManager().getLogin().remove(player.getName().toLowerCase());
				Title title = new Title("","");
				title.send(player);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "LOGIN_ACCEPT"));
				getLoginManager().addPlayerToBGList(player);
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "LOGIN_DENY"));
			}
			return true;
		}
	}

}