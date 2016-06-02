package eu.epicpvp.kcore.Command.Admin;

import eu.epicpvp.kcore.Translation.TranslationHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandAdminStats  implements CommandExecutor{
	
	private StatsManager statsManager;
	
	public CommandAdminStats(StatsManager statsManager){
		this.statsManager=statsManager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "adminstats", alias = {"astats"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;

			if (!player.isOp()) {
				return true;
			}
			if (args.length==0) {
				sender.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/adminstats [Player]");
				return true;
			}
			String spieler = args[0];
			
			if(args.length==1){
				if(!UtilPlayer.isOnline(spieler)){
					statsManager.loadPlayer(spieler, new Callback<Integer>() {
						
						@Override
						public void call(Integer playerId) {
							callAllStats(player,playerId);
						}
					});
				}else{
					callAllStats(player,UtilPlayer.getPlayerId(Bukkit.getPlayer(spieler)));
				}
			}else{
				if(args[2].equalsIgnoreCase("set")){
					StatsKey key = StatsKey.valueOf(args[3]);
					String value = args[4];
					
					if(!UtilPlayer.isOnline(spieler)){
						statsManager.loadPlayer(spieler, new Callback<Integer>() {
							
							@Override
							public void call(Integer playerId) {
								set(player, playerId, key, value);
							}
						});
					}else{
						set(player, UtilPlayer.getPlayerId(Bukkit.getPlayer(spieler)), key, value);
					}
				}
			}
		}
		return true;
	}
	
	public void set(Player sender,int playerId, StatsKey key, Object value){
		statsManager.set(playerId, key, value);
		sender.sendMessage("§eSet§6 "+key.name() +"§e to§6 "+value);
	}
	
	public void callAllStats(Player sender, int playerId){
		for(StatsKey key : statsManager.getType().getStats()){
			sender.sendMessage("§eStats:§6 "+key.name()+" §eValue:§6"+statsManager.get(playerId, key));
		}
	}
	
}

