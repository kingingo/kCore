package eu.epicpvp.kcore.Command.Commands;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.StatsManager.Ranking;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.ServerType;
import dev.wolveringer.dataserver.gamestats.StatsKey;

public class CommandMoney implements CommandExecutor{
	
	private StatsManager stats;
	private Player player;
	private Player target;
	private double money;
	private Ranking ranking;
	private String prefix;
	
	public CommandMoney(StatsManager stats,MySQL mysql,ServerType type){
		this.stats=stats;
		this.ranking=new Ranking(mysql,stats, StatsKey.MONEY, -1, 10);
		this.stats.addRanking(ranking);
		this.prefix=(type==ServerType.SKYBLOCK?Language.getText("PREFIX_GAME",GameType.SKYBLOCK.getTyp()):Language.getText("PREFIX"));
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "money",alias = {"geld","konto","kontostand","stand"}, sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			player = (Player)cs;
			if(args.length==0){
				player.sendMessage(prefix+ Language.getText(player, "MONEY") + stats.getDouble(player, StatsKey.MONEY));
			}else{
				if(args.length==1){
					if(args[0].equalsIgnoreCase("ranking")||args[0].equalsIgnoreCase("top")){
						stats.SendRankingMessage(player, ranking);
						return true;
					}

					if(UtilPlayer.isOnline(args[0])){
						target=Bukkit.getPlayer(args[0]);
						player.sendMessage(prefix+target.getName()+" Kontostand betr§§gt:§§3 " + stats.getDouble(target, StatsKey.MONEY));
					}else{
						player.sendMessage(prefix+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					}
				}else if(args.length==3){
					if(args[0].equalsIgnoreCase("send")){
						if(UtilPlayer.isOnline(args[1])){
							target=Bukkit.getPlayer(args[1]);
							money=UtilNumber.toDouble(args[2]);
							
							if(money<0)return false;
							
							if(money!=0){
								if(stats.getDouble(player, StatsKey.MONEY) >= money){
									stats.add(player, StatsKey.MONEY, -money);
									stats.add(target, StatsKey.MONEY, money);
									target.sendMessage(prefix+Language.getText(player, "MONEY_RECEIVE_FROM",new String[]{player.getName(),String.valueOf(money)}));
									player.sendMessage(prefix+Language.getText(target, "MONEY_SEND_TO",new String[]{target.getName(),String.valueOf(money)}));
								}else{
									player.sendMessage(prefix+Language.getText(player, "MONEY_ENOUGH_MONEY"));
								}
							}else{
								player.sendMessage(prefix+Language.getText(player, "MONEY_NO_DOUBLE"));
							}
						}else{
							player.sendMessage(prefix+Language.getText(player, "PLAYER_IS_OFFLINE",args[1]));
						}
					}
				}
			}
		}else{
			if(args.length==3){
				if(args[0].equalsIgnoreCase("add")){
					if(UtilPlayer.isOnline(args[1])){
						target=Bukkit.getPlayer(args[1]);
						money=UtilNumber.toDouble(args[2]);
						
						if(money<0)return false;
						
						if(money!=0){
							stats.add(target, StatsKey.MONEY, money);
						}else{
							System.out.println("[EpicPvP] ist kein Double.");
						}
					}else{
						System.out.println("[EpicPvP] Der Spieler ist OFFLINE.");
					}
				}
			}
		}
		return false;
	}
	
}

