package me.kingingo.kcore.Command.Commands;

import java.sql.ResultSet;
import java.util.HashMap;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMoney implements CommandExecutor{
	
	private StatsManager stats;
	private Player player;
	private Player target;
	private double money;
	HashMap<Integer,String> ranking = new HashMap<>();
	
	public CommandMoney(StatsManager stats){
		this.stats=stats;
		stats.setRanking_Stats(Stats.MONEY);
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "money",alias = {"geld","konto","kontostand","stand"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			player = (Player)cs;
			if(args.length==0){
				player.sendMessage(Text.PREFIX_GAME.getText(GameType.SKYBLOCK.getTyp())+"Dein Kontostand beträgt:§3 " + stats.getDouble(Stats.MONEY, player));
			}else{
				if(args.length==1){
					if(args[0].equalsIgnoreCase("ranking")||args[0].equalsIgnoreCase("top")){
						stats.Ranking(player);
						return true;
					}

					if(UtilPlayer.isOnline(args[0])){
						target=Bukkit.getPlayer(args[0]);
						player.sendMessage(Text.PREFIX_GAME.getText(GameType.SKYBLOCK.getTyp())+target.getName()+" Kontostand beträgt:§3 " + stats.getDouble(Stats.MONEY, target));
					}else{
						player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
					}
				}else if(args.length==3){
					if(args[0].equalsIgnoreCase("send")){
						if(UtilPlayer.isOnline(args[1])){
							target=Bukkit.getPlayer(args[1]);
							money=UtilNumber.toDouble(args[2]);
							
							if(money<0)return false;
							
							if(money!=0){
								if(stats.getDouble(Stats.MONEY, player) >= money){
									stats.setDouble(player, stats.getDouble(Stats.MONEY, player)-money, Stats.MONEY);
									stats.setDouble(target, stats.getDouble(Stats.MONEY, target)+money, Stats.MONEY);
									target.sendMessage(Text.PREFIX.getText()+Text.MONEY_RECEIVE_FROM.getText(new String[]{player.getName(),String.valueOf(money)}));
									player.sendMessage(Text.PREFIX.getText()+Text.MONEY_SEND_TO.getText(new String[]{target.getName(),String.valueOf(money)}));
								}else{
									player.sendMessage(Text.PREFIX.getText()+Text.MONEY_ENOUGH_MONEY.getText());
								}
							}else{
								player.sendMessage(Text.PREFIX.getText()+Text.MONEY_NO_DOUBLE.getText());
							}
						}else{
							player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[1]));
						}
					}
				}
			}
		}
		return false;
	}
	
}

