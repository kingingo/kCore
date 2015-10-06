package me.kingingo.kcore.Command.Admin;

import java.io.File;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilDebug;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnloadChunks implements CommandExecutor{
	
	private long timings;
	private String list;
	private String list1;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "unloadchunks", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(player.hasPermission(kPermission.MONITOR.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(Language.getText(player,"PREFIX")+"/unloadchunks [World/ALL]");
				}else{
					if(args[0].equalsIgnoreCase("all")){
						unloadChunks(null, player);
					}else{
						unloadChunks(args[0], player);
					}
				}
			}
		}else{
			if(args.length==0){
				System.out.println("/unloadchunks [World/ALL]");
			}else{
				if(args[0].equalsIgnoreCase("all")){
					unloadChunks(null, null);
				}else{
					unloadChunks(args[0], null);
				}
			}
		}
		return false;
	}
	
	public void unloadChunks(String world,Player player){
		if(UtilDebug.isDebug()){
			list="";
			list1="";
		}
		
		if(world==null){
			int a = 0;
            for(World w : Bukkit.getWorlds()){
        		if(UtilDebug.isDebug()){
        			list+="World: "+w.getName()+"-/-";
        		}
            	for(Chunk ch : w.getLoadedChunks()){
            		if(UtilDebug.isDebug())timings = System.currentTimeMillis();
            		if(ch.unload(true, true)){
            			a++;
            		}
            		if(UtilDebug.isDebug()){
            			timings = System.currentTimeMillis() - timings;
            			if(timings==0){
            				list1+="Chunk-X: "+ch.getX()+" Chunk-Z: "+ch.getZ()+" unload-time: "+timings+" ("+UtilTime.formatMili(timings)+") -/-";
            			}else{
            				list+="Chunk-X: "+ch.getX()+" Chunk-Z: "+ch.getZ()+" unload-time: "+timings+" ("+UtilTime.formatMili(timings)+") -/-";
            			}
            		}
            	}
            	
            	if(UtilDebug.isDebug()){
            		list+=list1;
            		UtilFile.createFile(new File("kdebug"),UtilTime.now()+".txt", list);
            	}
            }
            if(player!=null){
            	player.sendMessage(Language.getText(player, "PREFIX")+" unloaded Chunks:§e "+a);
            }else{
                System.out.println(" unloaded Chunks: "+a);
            }
		}else{
			if(Bukkit.getWorld(world)!=null){
				int a = 0;

        		if(UtilDebug.isDebug()){
        			list="";
        			list1="";
        			list+="World: "+world+"-/-";
        		}
                for(Chunk ch : Bukkit.getWorld(world).getLoadedChunks()){
            		if(UtilDebug.isDebug())timings = System.currentTimeMillis();
                	if(ch.unload(true, true)){
                		a++;
                	}
            		if(UtilDebug.isDebug()){
            			timings = System.currentTimeMillis() - timings;
            			if(timings==0){
            				list1+="Chunk-X: "+ch.getX()+" Chunk-Z: "+ch.getZ()+" unload-time: "+timings+" ("+UtilTime.formatMili(timings)+") -/-";
            			}else{
            				list+="Chunk-X: "+ch.getX()+" Chunk-Z: "+ch.getZ()+" unload-time: "+timings+" ("+UtilTime.formatMili(timings)+") -/-";
            			}
            		}
                }
                
                if(UtilDebug.isDebug()){
            		list+=list1;
            		UtilFile.createFile(new File("kdebug"),UtilTime.now()+".txt", list);
            	}
                
                if(player!=null){
               	 	player.sendMessage(Language.getText(player, "PREFIX")+" unloaded Chunks from "+world+":§e "+a);
                }else{
                	System.out.println("unloaded Chunks from "+world+": "+a);
                }
			}else{
                if(player!=null){
	                player.sendMessage(Language.getText(player, "PREFIX")+"§cThe world §e"+world+"§c was not found!");
                }else{
					System.out.println("The world "+world+" was not found!");
                }
			}
		}
	}

}
