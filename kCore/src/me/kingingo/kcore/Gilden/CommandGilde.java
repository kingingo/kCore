package me.kingingo.kcore.Gilden;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Enum.Text;
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
			p.sendMessage("§6/gilde erstellen §8|§b Erstellt eine neue Gilde.");
			p.sendMessage("§6/gilde einladen [Player] §8§8|§b Lädt einen Spieler in die Gilde ein");
			p.sendMessage("§6/gilde annehmen §8|§b Nimmt Gildeinladung an.");
			p.sendMessage("§6/gilde verlassen §8|§b Zum Verlassen/Schliesen der Gilde.");
			p.sendMessage("§6/gilde kicken [Player] §8|§b Kickt einen Spieler aus der Gilde.");
			p.sendMessage("§6/gilde info [Gilde] §8|§b Zeigt Infos über einer Gilde.");
			if(manager.getTyp()==GameType.PVP){
				p.sendMessage("§6/gilde sethome §8|§b setzt den Gilden-Home");
				p.sendMessage("§6/gilde home §8|§b Teleportiert dich zum Gilden-Home");
			}
		}else if(args.length > 0){
			if(args[0].equalsIgnoreCase("erstellen")){
				Erstellen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("einladen")){
				Einladen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("annehmen")){
				Annehmen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("verlassen")){
				Verlassen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("kicken")){
				Kicken.use(p, args, manager);
			}else if(manager.getTyp()==GameType.PVP&&args[0].equalsIgnoreCase("sethome")){
				Home.useSet(p, args, manager);
			}else if(manager.getTyp()==GameType.PVP&&args[0].equalsIgnoreCase("home")){
				Home.use(p, args, manager);
			}else if(manager.getTyp()==GameType.PVP&&args[0].equalsIgnoreCase("info")){
				Info.use(p, args, manager);
			}
		}
		return false;
	}
	
}

