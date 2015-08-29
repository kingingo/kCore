package me.kingingo.kcore.Command.Admin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMySQL implements CommandExecutor{
	
	private MySQL mysql;
	
	public CommandMySQL(MySQL mysql){
		this.mysql=mysql;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "mysql", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.MONITOR.getPermissionToString())){
			if(args.length==0){
				if(mysql.isDebug()){
					mysql.setDebug(false);
					player.sendMessage(Language.getText(player,"PREFIX")+" MySQL debug mode §cdeakiviert!");
				}else{
					mysql.setDebug(true);
					player.sendMessage(Language.getText(player,"PREFIX")+" MySQL debug mode §aakiviert!");
				}
			}
		}
		return false;
	}
	
}
