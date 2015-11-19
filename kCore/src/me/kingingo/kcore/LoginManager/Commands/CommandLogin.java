package me.kingingo.kcore.LoginManager.Commands;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.LoginManager.LoginManager;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLogin implements CommandExecutor{

	public CommandLogin(LoginManager lManager) {
		this.LoginManager=lManager;
	}

	@Getter
	private LoginManager LoginManager;
	private Player p;
	private String pw;

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "login", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs,org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if(!(cs instanceof Player))return false;
		p=(Player)cs;
		if(!getLoginManager().getLogin().containsKey(p))return false;
		if(args.length==0)p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "LOGIN_MESSAGE"));
		else if(args.length==1){
			pw=args[0];
			if(pw.equalsIgnoreCase(getLoginManager().getLogin().get(p))){
				getLoginManager().getLogin().remove(p);
				getLoginManager().delLogin(p.getName());
				p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "LOGIN_ACCEPT"));
				return true;
			}else{
				p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "LOGIN_DENY"));
			}
		}
		return false;
	}

}
