package me.kingingo.kcore.LoginManager.Commands;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.LoginManager.LoginManager;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCaptcha implements CommandExecutor{

	public CommandCaptcha(LoginManager lManager) {
		this.LoginManager=lManager;
	}

	@Getter
	private LoginManager LoginManager;
	private Player p;

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "captcha", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs,org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if(!(cs instanceof Player))return false;
		p=(Player)cs;
		if(!getLoginManager().getRegister().containsKey(p))return false;
		if(getLoginManager().getRegister().get(p)==null)return false;
		if(args.length==0)p.sendMessage(Language.getText(p, "PREFIX")+"§c/captcha [CAPTCHA]");
		else if(args.length==1){
			if(args[0].equalsIgnoreCase(getLoginManager().getCaptcha_string())){
				getLoginManager().getRegister().remove(p);
				getLoginManager().getRegister().put(p, null);
				p.getInventory().clear();
				p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "REGISTER_MESSAGE"));
			}else{
				p.kickPlayer(Language.getText(p, "CAPTCHA_FALSE"));
			}
		}
		return false;
	}

}
