package eu.epicpvp.kcore.Command.Admin;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandHomeCheck implements CommandExecutor{
	
	private JavaPlugin instance;
	
	public CommandHomeCheck(JavaPlugin instance){
		this.instance=instance;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "homecheck", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.isOp()){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§e/homecheck [Radius]");
			}else{
				int radius = UtilNumber.toInt(args[0]);
				Location ploc = player.getLocation();
				
				Bukkit.getScheduler().runTaskAsynchronously(instance, new Runnable() {
					
					@Override
					public void run() {
						ArrayList<kConfig> configs = new ArrayList<>();
						File[] list = new File(instance.getDataFolder(), "userdata").listFiles();
						System.out.println("[HomeCheck] Load "+list.length+" files..");
						
						try {
							Thread.sleep(1000 * 5);
						} catch (InterruptedException e) {}
						
						int i=0;
						for(File file : list){
							i++;
							kConfig config = new kConfig(file);
							if(config.contains("homes")){
								configs.add(config);
							}
							System.out.println("[HomeCheck/"+config.getFile().getName()+"] "+i+"/"+list.length+" controll files "+configs.size());
						}
						
						try {
							Thread.sleep(1000 * 5);
						} catch (InterruptedException e) {}
						
						System.out.println("[HomeCheck] Start to controll all HOMES");
						try {
							Thread.sleep(1000 * 5);
						} catch (InterruptedException e) {}

						i=0;
						
						for(kConfig config : configs){
							i++;
							int playerId = UtilNumber.toInt(config.getFile().getName().replaceAll(".yml", ""));
							
							for(String path : config.getPathList("homes").keySet()){
								Location location = config.getLocation("homes."+path);
								
								if(ploc.getWorld().getUID() == location.getWorld().getUID()){
									if(ploc.distance(location) <= radius){
										System.out.println("[HomeCheck] "+UtilServer.getClient().getPlayerAndLoad(playerId).getName()+" ("+playerId+"/"+path+")");
									}
								}
							}
						}

						System.out.println("[HomeCheck] DONE!");
					}
				});
			}
		}
		return false;
	}
}

