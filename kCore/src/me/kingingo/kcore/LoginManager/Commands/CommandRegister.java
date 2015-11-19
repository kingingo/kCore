package me.kingingo.kcore.LoginManager.Commands;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.LoginManager.LoginManager;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRegister implements CommandExecutor{

	public CommandRegister(LoginManager lManager) {
		this.LoginManager=lManager;
	}

	@Getter
	private LoginManager LoginManager;
	private Player p;
	private String pw;

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "register", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs,org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if(!(cs instanceof Player))return false;
		p=(Player)cs;
		if(!getLoginManager().getRegister().containsKey(p))return false;
		if(getLoginManager().getRegister().get(p)!=null){
			p.kickPlayer(Language.getText(p, "CAPTCHA_FIRST_ENTER"));
			return false;
		}
		if(args.length==0)p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "PREFIX"));
		else if(args.length==1){
			pw=args[0];
			if(!pw.matches("[a-zA-Z0-9_]*")){
				p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "LOGIN_FAIL"));
				return false;
			}
			getLoginManager().getRegister().remove(p);
			getLoginManager().delLogin(p.getName());
			getLoginManager().setUser(p, pw, "");
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "REGISTER_ACCEPT"));
			return true;
		}
		return false;
	}

}
