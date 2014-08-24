package me.kingingo.kcore.Gilden;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Gilden.Commands.Annehmen;
import me.kingingo.kcore.Gilden.Commands.Einladen;
import me.kingingo.kcore.Gilden.Commands.Erstellen;
import me.kingingo.kcore.Gilden.Commands.Home;
import me.kingingo.kcore.Gilden.Commands.Info;
import me.kingingo.kcore.Gilden.Commands.Kicken;
import me.kingingo.kcore.Gilden.Commands.Verlassen;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGilde implements CommandExecutor{
	
	Player p;
	GildenManager manager;
	
	public CommandGilde(GildenManager manager){
		this.manager=manager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "gilde", alias = {"g","gild","gilden"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		p = (Player)cs;
		if(args.length==0){
		
			
		}else if(args.length > 0){
			if(args[1].equalsIgnoreCase("erstellen")){
				Erstellen.use(p, args, manager);
			}else if(args[1].equalsIgnoreCase("einladen")){
				Einladen.use(p, args, manager);
			}else if(args[1].equalsIgnoreCase("annehmen")){
				Annehmen.use(p, args, manager);
			}else if(args[1].equalsIgnoreCase("verlassen")){
				Verlassen.use(p, args, manager);
			}else if(args[1].equalsIgnoreCase("kicken")){
				Kicken.use(p, args, manager);
			}else if(manager.getTyp()==GildenTyp.PvP&&args[1].equalsIgnoreCase("sethome")){
				Home.useSet(p, args, manager);
			}else if(manager.getTyp()==GildenTyp.PvP&&args[1].equalsIgnoreCase("home")){
				Home.use(p, args, manager);
			}else if(manager.getTyp()==GildenTyp.PvP&&args[1].equalsIgnoreCase("info")){
				Info.use(p, args, manager);
			}
		}
		return false;
	}
	
}

