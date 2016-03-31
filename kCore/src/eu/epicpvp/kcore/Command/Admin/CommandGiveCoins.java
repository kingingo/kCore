package eu.epicpvp.kcore.Command.Admin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandGiveCoins implements CommandExecutor{
	
	private StatsManager money;
	
	public CommandGiveCoins(StatsManager money){
		this.money=money;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "givecoins",alias={"coins"}, sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(Language.getText(player, "PREFIX")+"§a/givecoins [Spieler] [+/- Gems]");
				}else if(args.length >= 2){
					String spieler = args[0];
					int c=UtilInteger.isNumber(args[1]);
					
					if(c==-1)return false;
					if(UtilPlayer.isOnline(spieler)){
						money.add(Bukkit.getPlayer(spieler), StatsKey.COINS, c);
					}else{
						money.loadPlayer(spieler, new Callback<UUID>() {
							
							@Override
							public void call(UUID uuid) {
								money.add(uuid, null, StatsKey.COINS, c);
							}
						});
					}
					player.sendMessage(Language.getText(player, "PREFIX")+(c<0?Language.getText(player, "GEMS_DEL_PLAYER",new String[]{player.getName(),String.valueOf(c)}):Language.getText(player, "GEMS_ADD_PLAYER",new String[]{player.getName(),String.valueOf(c)})));
				}
			}
		}else if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /givecoins [Spieler] [+/- Gems]");
			}else if(args.length >= 2){
				String spieler = args[0];
				
				int c=UtilInteger.isNumber(args[1]);
				
				if(c==-1)return false;
				if(UtilPlayer.isOnline(spieler)){
					money.add(Bukkit.getPlayer(spieler), StatsKey.COINS, c);
				}else{
					money.loadPlayer(spieler, new Callback<UUID>() {
						
						@Override
						public void call(UUID uuid) {
							money.add(uuid, null, StatsKey.COINS, c);
						}
					});
				}
				System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Coins erhalten!");
			}
		}
		return false;
	}
	
}
