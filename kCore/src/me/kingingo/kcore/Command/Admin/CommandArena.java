package me.kingingo.kcore.Command.Admin;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Arena.ArenaManager;
import me.kingingo.kcore.Arena.ArenaType;
import me.kingingo.kcore.Arena.GameRound;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.GameType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandArena implements CommandExecutor{
	
	@Getter
	private ArrayList<ArenaManager> list;
	
	public CommandArena(){
		list=new ArrayList<>();
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "arena", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.isOp()){
			if(args.length==0){
				for(ArenaManager m : list){
					for(ArenaType t : m.getRounds().keySet()){
						player.sendMessage(" "+ t.name()+" : "+m.getRounds().get(t).size());
					}

					player.sendMessage("Rounds_player: "+ m.getRounds_player().size());
					player.sendMessage("Server: "+ m.getServer().size());
					player.sendMessage("Wait_list: "+ m.getWait_list().size());
					m.getRounds().clear();
					for(ArenaType type : ArenaType.values())m.getRounds().put(type, new HashMap<Integer,GameRound>());
					m.getRounds_player().clear();
					m.setRound_counter(0);
					
					m.getServer().clear();
					m.getWait_list().clear();
					for(ArenaType type : ArenaType.values())m.getWait_list().put(type, new ArrayList<Player>());

				}
				
				player.sendMessage("§ACLEAREDD!!!");
			}else{
				if(args[0].equalsIgnoreCase("c1")){
					for(ArenaManager m : list){
						m.getRounds().clear();
						for(ArenaType type : ArenaType.values())m.getRounds().put(type, new HashMap<Integer,GameRound>());
						m.getRounds_player().clear();
						m.setRound_counter(0);
					}
					
					player.sendMessage("§aC1!!!");
				}else if(args[0].equalsIgnoreCase("c2")){
					for(ArenaManager m : list){
						m.getServer().clear();
					}
					
					player.sendMessage("§aC2!!!");
				}else if(args[0].equalsIgnoreCase("c3")){
					for(ArenaManager m : list){
						m.getWait_list().clear();
						for(ArenaType type : ArenaType.values())m.getWait_list().put(type, new ArrayList<Player>());
					}
					
					player.sendMessage("§aC3!!!");
				}else if(args[0].equalsIgnoreCase("skywars")){
					ArenaManager a = null;
					
					for(ArenaManager aa : list){
						if(aa.getT()==GameType.SkyWars1vs1){
							a=aa;
							break;
						}
					}
					
					for(String s : a.getServer().keySet()){
						player.sendMessage("S: "+s+" "+a.getServer().get(s).getArena()+" "+a.getServer().get(s).getState());
					}
					player.sendMessage("LIST:"+a.getServer().size());
					player.sendMessage("ROUNDS:"+a.getRounds_player().size());
				}else if(args[0].equalsIgnoreCase("bedwars")){
					ArenaManager a = null;
					
					for(ArenaManager aa : list){
						if(aa.getT()==GameType.BedWars1vs1){
							a=aa;
							break;
						}
					}
					
					for(String s : a.getServer().keySet()){
						player.sendMessage("S: "+s+" "+a.getServer().get(s).getArena()+" "+a.getServer().get(s).getState());
					}
					player.sendMessage("LIST:"+a.getServer().size());
					player.sendMessage("ROUNDS:"+a.getRounds_player().size());
				}else if(args[0].equalsIgnoreCase("sg")){
					ArenaManager a = null;
					
					for(ArenaManager aa : list){
						if(aa.getT()==GameType.SurvivalGames1vs1){
							a=aa;
							break;
						}
					}
					
					for(String s : a.getServer().keySet()){
						player.sendMessage("S: "+s+" "+a.getServer().get(s).getArena()+" "+a.getServer().get(s).getState());
					}
					player.sendMessage("LIST:"+a.getServer().size());
					player.sendMessage("ROUNDS:"+a.getRounds_player().size());
				}if(args[0].equalsIgnoreCase("versus")){
					ArenaManager a = null;
					
					for(ArenaManager aa : list){
						if(aa.getT()==GameType.Versus){
							a=aa;
							break;
						}
					}
					
					for(String s : a.getServer().keySet()){
						player.sendMessage("S: "+s+" "+a.getServer().get(s).getArena()+" "+a.getServer().get(s).getState());
					}
					player.sendMessage("LIST:"+a.getServer().size());
					player.sendMessage("ROUNDS:"+a.getRounds_player().size());
				}
			}
		}
		return false;
	}

}
