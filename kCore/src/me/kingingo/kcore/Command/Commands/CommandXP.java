package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandXP implements CommandExecutor{

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "xp", alias = {"exp"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage(Language.getText(p, "PREFIX")+"§9/Xp send <Spieler> <Level>");
		}else{
			if(args[0].equalsIgnoreCase("send")){
				
				if(args.length == 1){
					p.sendMessage(Language.getText(p, "PREFIX")+"§c/xp send <Player> <Level>");
					return false;
				}
				if(args.length == 2){
					p.sendMessage(Language.getText(p, "PREFIX")+"§c/xp send <Player> <Level>");
					return false;
				}
				if(args.length == 3){
					
					int exp = 0;
					Player target;
					
					try{
						exp = Integer.parseInt(args[2]);
						
					}catch(NumberFormatException e){					
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "NO_INTEGER"));
						return false;
					}
					
					try{
						target = (Player) p.getServer().getPlayer(args[1]);
						if(target.isOnline()){
							
						}else{
							throw new NullPointerException();
						}
					}catch(NullPointerException e){
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "PLAYER_IS_OFFLINE",args[1]));
						return false;
					}
					
					if(exp < 1){
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "EXP_MINUS"));
						return false;
					}
					
					if(p.getLevel() < exp){
						p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "NOT_ENOUGH_EXP"));
						return false;
					}
					
					target.setLevel(target.getLevel() + exp);
					p.setLevel(p.getLevel() - exp);
					
					target.sendMessage(Language.getText(target, "PREFIX")+ Language.getText(target, "EXP_HIS_TO_ME",new String[]{p.getName(),String.valueOf(exp)}));
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(target, "EXP_ME_TO_HIS",new String[]{target.getName(),String.valueOf(exp)}));
					
					
					return false;
				}
				}
		}
		return false;
	}
	
}
