package eu.epicpvp.kcore.Command.Admin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.dataclient.gamestats.GameType;
import eu.epicpvp.kcore.Arena.ArenaManager;
import eu.epicpvp.kcore.Arena.ArenaType;
import eu.epicpvp.kcore.Arena.GameRound;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;

public class CommandArena implements CommandExecutor{
	
	@Getter
	private ArrayList<ArenaManager> list;
	
	public CommandArena(){
		list=new ArrayList<>();
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "arena", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.isOp()){
			if(args.length==0){
				for(ArenaManager m : list){
					System.out.println("GameType: "+ m.getT().name());
					player.sendMessage("GameType: "+ m.getT().name());
					for(ArenaType t : m.getRounds().keySet()){
						System.out.println("		"+ t.name()+" : "+m.getRounds().get(t).size());
						player.sendMessage("		"+ t.name()+" : "+m.getRounds().get(t).size());
					}
					System.out.println("	Rounds_player: "+ m.getRounds_player().size());
					System.out.println("	Server: "+ m.getServer().size());
					System.out.println("	Wait_list: "+ m.getWait_list().size());
					
					player.sendMessage("	Rounds_player: "+ m.getRounds_player().size());
					player.sendMessage("	Server: "+ m.getServer().size());
					player.sendMessage("	Wait_list: "+ m.getWait_list().size());
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
				if(args[0].equalsIgnoreCase("save")){
					try {
						FileWriter fstream = new FileWriter("data.dat");
						BufferedWriter out = new BufferedWriter(fstream);  
						
						for(ArenaManager m : list){
							out.write("GameType: "+ m.getT().name());
							out.write("\n");
							for(ArenaType t : m.getRounds().keySet()){
								out.write("		"+ t.name()+" : "+m.getRounds().get(t).size());
								out.write("\n");
							}
							out.write("	Rounds_player: "+ m.getRounds_player().size());
							out.write("\n");
							for(HashMap<Integer, GameRound> l : m.getRounds().values()){
								out.write("	Rounds: "+ l.size());
								out.write("\n");
								for(GameRound r : l.values()){
									out.write("	ROUND: "+ r.getType().name());
									out.write("\n");
									for(UUID uuid : r.getPlayers()){
										out.write("		UUID: "+ uuid);
										out.write("\n");
										out.write("		NAME: "+ (UtilPlayer.isOnline(uuid) ? Bukkit.getPlayer(uuid).getName() : "OFFLINE"));
										out.write("\n");
									}
								}
							}
							out.write("	Server: "+ m.getServer().size());
							out.write("\n");
							for(String s : m.getServer().keySet()){
								out.write("		LIST: "+s+" Server: "+ m.getServer().get(s).getServer() + " " + m.getServer().get(s).getArena() +" "+ m.getServer().get(s).getState().name());
								out.write("\n");
							}
							out.write("	Wait_list: "+ m.getWait_list().size());
							for(ArenaType t : m.getWait_list().keySet()){
								out.write("		ARENA_T: "+  t.name());
								out.write("\n");
								for(Player p : m.getWait_list().get(t)){
									out.write("			PLAYER: "+ p.getName() +" "+p.isOnline());
									out.write("\n");
								}
							}
							out.write("\n");

						}
						
						out.close();
						fstream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(args[0].equalsIgnoreCase("c1")){
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
