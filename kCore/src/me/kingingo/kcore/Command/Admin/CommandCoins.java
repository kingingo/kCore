package me.kingingo.kcore.Command.Admin;

import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
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
				player.sendMessage(Language.getText(player, "PREFIX")+"§a/coins [Spieler] [+/- Coins]");
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
				player.sendMessage(Language.getText(player, "PREFIX")+(c<0?Language.getText(player, "COINS_DEL_PLAYER",new String[]{player.getName(),String.valueOf(c)}):Language.getText(player, "COINS_ADD_PLAYER",new String[]{player.getName(),String.valueOf(c)})));
			}
		}
		return false;
	}

}
