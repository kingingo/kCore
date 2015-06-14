package me.kingingo.kcore.Command.Admin;

import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.UtilInteger;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCoins implements CommandExecutor{
	
	private Coins coins;
	
	public CommandCoins(Coins coins){
		this.coins=coins;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "coins", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Text.PREFIX.getText()+"§a/coins [Spieler] [+/- Coins]");
			}else if(args.length >= 2){
				String spieler = args[0];
				UUID uuid=null;
				
				if(UtilPlayer.isOnline(spieler)){
					uuid=UtilPlayer.getRealUUID(Bukkit.getPlayer(spieler));
				}else{
					uuid=UtilPlayer.getUUID(spieler, coins.getMysql());
				}
				
				int c=UtilInteger.isNumber(args[1]);
				
				if(c==-1)return false;
				coins.addCoins(uuid, c);
				player.sendMessage(Text.PREFIX.getText()+"§aDen Spieler §b"+spieler+"§a wurden §b"+ (c<0 ? c+"§a entfernt" : c+"§a hinzugefügt"));
			}
		}
		return false;
	}

}
