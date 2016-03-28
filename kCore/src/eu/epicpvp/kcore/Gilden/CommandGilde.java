package eu.epicpvp.kcore.Gilden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Gilden.Commands.Annehmen;
import eu.epicpvp.kcore.Gilden.Commands.Einladen;
import eu.epicpvp.kcore.Gilden.Commands.Erstellen;
import eu.epicpvp.kcore.Gilden.Commands.Home;
import eu.epicpvp.kcore.Gilden.Commands.Info;
import eu.epicpvp.kcore.Gilden.Commands.Kicken;
import eu.epicpvp.kcore.Gilden.Commands.Money;
import eu.epicpvp.kcore.Gilden.Commands.Ranking;
import eu.epicpvp.kcore.Gilden.Commands.Verlassen;

public class CommandGilde implements CommandExecutor{
	
	Player p;
	GildenManager manager;
	
	public CommandGilde(GildenManager manager){
		this.manager=manager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "gilde", alias = {"g","c","guild","clan","gild","gilden"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		p = (Player)cs;
		if(args.length==0){
			p.sendMessage("\u00A75b\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A756\u00A75l GILDE \u00A75b\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75");
			p.sendMessage(Language.getText(p, "GILDE_CMD1"));
			p.sendMessage(Language.getText(p, "GILDE_CMD2"));
			p.sendMessage(Language.getText(p, "GILDE_CMD3"));
			p.sendMessage(Language.getText(p, "GILDE_CMD4"));
			p.sendMessage(Language.getText(p, "GILDE_CMD5"));
			p.sendMessage(Language.getText(p, "GILDE_CMD6"));
			p.sendMessage(Language.getText(p, "GILDE_CMD7"));
			p.sendMessage(Language.getText(p, "GILDE_CMD14"));
			if(manager.getTyp()==GildenType.PVP){
				p.sendMessage(Language.getText(p, "GILDE_CMD8"));
				p.sendMessage(Language.getText(p, "GILDE_CMD9"));
			}else if(manager.getTyp()==GildenType.SKY){
				p.sendMessage(Language.getText(p, "GILDE_CMD10"));
				p.sendMessage(Language.getText(p, "GILDE_CMD11"));
				p.sendMessage(Language.getText(p, "GILDE_CMD12"));
				p.sendMessage(Language.getText(p, "GILDE_CMD13"));
			}
			p.sendMessage("\u00A75b\u00A75 \u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A756\u00A75l GILDE \u00A75b\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75\u00A75");
		}else if(args.length > 0){
			if(args[0].equalsIgnoreCase("erstellen")||args[0].equalsIgnoreCase("create")){
				Erstellen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("einladen")||args[0].equalsIgnoreCase("invite")){
				Einladen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("annehmen")||args[0].equalsIgnoreCase("accept")){
				Annehmen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("verlassen")||args[0].equalsIgnoreCase("leave")){
				Verlassen.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("ranking")){
				Ranking.use(p, args, manager);
			}else if(args[0].equalsIgnoreCase("kick")||args[0].equalsIgnoreCase("kicken")){
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
			}else if(manager.getTyp()==GildenType.SKY&&p.hasPermission(PermissionType.GILDE_NEWISLAND.getPermissionToString())&&args[0].equalsIgnoreCase("newisland")){
				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
				sky.getSky().getGilden_world().newIsland(args[1]);
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+"\u00A75aDone.");
			}
		}
		return false;
	}
	
}

