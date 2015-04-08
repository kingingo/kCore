package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandXP implements CommandExecutor{

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "xp", alias = {"exp"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage(Text.PREFIX.getText()+"§9/Xp send <Spieler> <Level> : Spieler Level schicken");
		}else{
			if(args[0].equalsIgnoreCase("send")){
				
				if(args.length == 1){
					p.sendMessage(Text.PREFIX.getText()+"§c/xp send <Player> <Level>");
					return false;
				}
				if(args.length == 2){
					p.sendMessage(Text.PREFIX.getText()+"§c/xp send <Player> <Level>");
					return false;
				}
				if(args.length == 3){
					
					int exp = 0;
					Player target;
					
					try{
						exp = Integer.parseInt(args[2]);
						
					}catch(NumberFormatException e){					
						p.sendMessage(Text.PREFIX.getText()+"§cDas ist keine Zahl " + exp);
						return false;
					}
					
					try{
						target = (Player) p.getServer().getPlayer(args[1]);
						if(target.isOnline()){
							
						}else{
							throw new NullPointerException();
						}
					}catch(NullPointerException e){
						p.sendMessage(Text.PREFIX.getText()+"§c" + args[1] + " ist offline. Abbruch.");
						return false;
					}
					
					if(exp < 1){
						p.sendMessage(Text.PREFIX.getText()+"§cDu kannst keine Minus zahlen verschicken!");
						return false;
					}
					
					if(p.getLevel() < exp){
						p.sendMessage(Text.PREFIX.getText()+"§cDu hast nicht genug Exp!");
						return false;
					}
					
					target.setLevel(target.getLevel() + exp);
					p.setLevel(p.getLevel() - exp);
					
					target.sendMessage(Text.PREFIX.getText()+ p.getName() + " hat dir " + exp + " Exp überwiesen!");
					p.sendMessage(Text.PREFIX.getText()+" Du hast " + target.getName() + " " + exp + " Exp überwiesen ");
					
					
					return false;
				}
				}
		}
		return false;
	}
	
}
