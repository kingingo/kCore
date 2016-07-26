package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilEXP;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandXP implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "xp", alias = {"exp"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§c/xp send <Player> <LVL>");
		}else{
			if(args[0].equalsIgnoreCase("send")){
				
				if(args.length == 1){
					p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§c/xp send <Player> <LVL>");
					return false;
				}
				if(args.length == 2){
					p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§c/xp send <Player> <LVL>");
					return false;
				}
				if(args.length == 3){
					
					int lvl = 0;
					Player target;
					
					try{
						lvl = Integer.parseInt(args[2]);
						
					}catch(NumberFormatException e){					
						p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "NO_INTEGER"));
						return false;
					}
					
					try{
						target = UtilServer.getNickedPlayer(args[1]);
						if(target.isOnline()){
							
						}else{
							throw new NullPointerException();
						}
					}catch(NullPointerException e){
						p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "PLAYER_IS_OFFLINE",args[1]));
						return false;
					}
					
					if(lvl < 1){
						p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "EXP_MINUS"));
						return false;
					}
					
					if(p.getLevel() < lvl){
						p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "NOT_ENOUGH_EXP"));
						return false;
					}
					
					int exp = UtilEXP.getLevelToExp(p.getLevel()-lvl);
					int amount_exp = UtilEXP.getLevelToExp(p.getLevel());
					
					target.giveExp(amount_exp-exp);
					p.setLevel(0);
					p.setExp(0);
					p.giveExp( exp );
					
					target.sendMessage(TranslationHandler.getText(target, "PREFIX")+ TranslationHandler.getText(target, "EXP_HIS_TO_ME", p.getName(),String.valueOf(amount_exp-exp)));
					p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(target, "EXP_ME_TO_HIS", target.getName(),String.valueOf(amount_exp-exp)));
					
					
					return false;
				}
				}
		}
		return false;
	}
	
}
