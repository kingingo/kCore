package me.kingingo.kcore.Gilden;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Gilden.Commands.Annehmen;
import me.kingingo.kcore.Gilden.Commands.Einladen;
import me.kingingo.kcore.Gilden.Commands.Erstellen;
import me.kingingo.kcore.Gilden.Commands.Home;
import me.kingingo.kcore.Gilden.Commands.Info;
import me.kingingo.kcore.Gilden.Commands.Kicken;
import me.kingingo.kcore.Gilden.Commands.Money;
import me.kingingo.kcore.Gilden.Commands.Ranking;
import me.kingingo.kcore.Gilden.Commands.Verlassen;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

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
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "gilde", alias = {"g","c","clan","gild","gilden"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		p = (Player)cs;
		if(args.length==0){
			p.sendMessage("§b■■■■■■■■■■■■■■§6§l GILDE §b■■■■■■■■■■■■■■");
			p.sendMessage(Language.getText(p, "GILDE_CMD1"));
			p.sendMessage(Language.getText(p, "GILDE_CMD2"));
			p.sendMessage(Language.getText(p, "GILDE_CMD3"));
			p.sendMessage(Language.getText(p, "GILDE_CMD4"));
			p.sendMessage(Language.getText(p, "GILDE_CMD5"));
			p.sendMessage(Language.getText(p, "GILDE_CMD6"));
			p.sendMessage(Language.getText(p, "GILDE_CMD7"));
			if(manager.getTyp()==GildenType.PVP){
				p.sendMessage(Language.getText(p, "GILDE_CMD8"));
				p.sendMessage(Language.getText(p, "GILDE_CMD9"));
			}else if(manager.getTyp()==GildenType.SKY){
				p.sendMessage(Language.getText(p, "GILDE_CMD10"));
				p.sendMessage(Language.getText(p, "GILDE_CMD11"));
				p.sendMessage(Language.getText(p, "GILDE_CMD12"));
				p.sendMessage(Language.getText(p, "GILDE_CMD13"));
			}
			p.sendMessage("§b■■■■■■■■■■■■■■§6§l GILDE §b■■■■■■■■■■■■■■");
		}else if(args.length > 0){
			if(args[0].equalsIgnoreCase("erstellen")){
				Erstellen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("einladen")||args[0].equalsIgnoreCase("create")){
				Einladen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("annehmen")||args[0].equalsIgnoreCase("accept")){
				Annehmen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("verlassen")||args[0].equalsIgnoreCase("leave")){
				Verlassen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("ranking")){
				Ranking.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("kicken")){
				Kicken.use(p, args, manager);
			}else if(manager.getTyp()==GildenType.PVP&&args[0].equalsIgnoreCase("sethome")){
				Home.useSet(p, args, manager);
			}else if(manager.getTyp()==GildenType.PVP&&args[0].equalsIgnoreCase("home")){
				Home.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("info")){
				Info.use(p, args, manager);
			}else if(manager.getTyp()==GildenType.SKY&&args[0].equalsIgnoreCase("island")){
				Home.use(p, args, manager);
			}else if(manager.getTyp()==GildenType.SKY&&args[0].equalsIgnoreCase("money")){
				Money.use(p, args, manager);
			}else if(manager.getTyp()==GildenType.SKY&&args[0].equalsIgnoreCase("createisland")){
				Home.useSet(p, args, manager);
			}else if(manager.getTyp()==GildenType.SKY&&p.hasPermission(kPermission.GILDE_NEWISLAND.getPermissionToString())&&args[0].equalsIgnoreCase("newisland")){
				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
				sky.getSky().getGilden_world().newIsland(args[1]);
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+"§aDone.");
			}
		}
		return false;
	}
	
}

