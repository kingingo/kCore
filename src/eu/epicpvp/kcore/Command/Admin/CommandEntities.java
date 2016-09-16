package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.EntityClick.EntityClickListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandEntities implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "entities", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(player.hasPermission(PermissionType.MONITOR.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationHandler.getText(player,"PREFIX")+"/entities [list/clearall/WORLD/HM]");
				}else{
					if(args[0].equalsIgnoreCase("list")){
						
		                for(World world : Bukkit.getWorlds()){
		                	int tileEntities = 0;
		                    try
		                    {
		                      for (Chunk chunk : world.getLoadedChunks())
		                      {
		                        tileEntities += chunk.getTileEntities().length;
		                      }
		                    }
		                    catch (ClassCastException ex)
		                    {
		                  	 ex.printStackTrace(); 
		                    }  
		                    
		                    for(EntityType type : EntityType.values()){
		                    	if(type==null||type==EntityType.UNKNOWN)continue;
		                    	if(world.getEntitiesByClass(type.getEntityClass()).size()<=0)continue;
		                    	player.sendMessage(TranslationHandler.getText(player,"PREFIX")+"§e"+world.getName()+"§7: Entity Type:§e"+type.name()+" §7Counted:§e"+world.getEntitiesByClass(type.getEntityClass()).size());
		                    }
		                    
		                	player.sendMessage(TranslationHandler.getText(player,"PREFIX")+"§e"+world.getName()+"§7: Chunks:§e"+world.getLoadedChunks().length+" §7Entities:§e"+world.getEntities().size()+" §7Tile:§e"+tileEntities);
		                }
					}else if(args[0].equalsIgnoreCase("hm")){
						int a = 0;
						for(World w : Bukkit.getWorlds()){
							for(Entity e : w.getEntities()){
			                	if( (e instanceof ArmorStand) ){
									e.remove();
									a++;
			                	}
							}
			                player.sendMessage(TranslationHandler.getText(player, "PREFIX")+" All entities "+w.getName()+" removed:§e "+a);
			                a=0;
						}
					}else if(args[0].equalsIgnoreCase("clearall")){
						int a = 0;
						for(World w : Bukkit.getWorlds()){
							for(Entity e : w.getEntities()){
			                	if((!(e instanceof Player))&&(!(e instanceof ItemFrame))&&(!(e instanceof ArmorStand))){
									
			                		if(EntityClickListener.getEntities().contains(e)){
										continue;
									}
			                		
			                		if(UtilServer.getDeliveryPet()!=null){
										if(UtilServer.getDeliveryPet().getJockey()!=null&&UtilServer.getDeliveryPet().getJockey().getEntityId()==e.getEntityId())continue;
										if(UtilServer.getDeliveryPet().getEntity()!=null&&UtilServer.getDeliveryPet().getEntity().getEntityId()==e.getEntityId())continue;
									}
									
									if(UtilServer.getPerkManager()!=null&&UtilServer.getPerkManager().getEntity()!=null&&UtilServer.getPerkManager().getEntity().getEntityId()==e.getEntityId())continue;
									
									if(UtilServer.getGemsShop()!=null&&UtilServer.getGemsShop().getListener()!=null&&UtilServer.getGemsShop().getListener().getEntity()!=null){
										if(UtilServer.getGemsShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
									}
									
									if(UtilServer.getPerkManager()!=null&&UtilServer.getPerkManager().getEntity()!=null){
										if(UtilServer.getPerkManager().getEntity().getEntityId()==e.getEntityId())continue;
									}
									
									if(UtilServer.getItemShop()!=null&&UtilServer.getItemShop().getListener()!=null&&UtilServer.getItemShop().getListener().getEntity()!=null){
										if(UtilServer.getItemShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
									}
									
			                		a++;
			                		e.remove();
			                	}
							}
			                player.sendMessage(TranslationHandler.getText(player, "PREFIX")+" All entities "+w.getName()+" removed:§e "+a);
			                a=0;
						}
					}else{
						if(Bukkit.getWorld(args[0])!=null){
							int a = 0;
			                
			                for(Entity e : Bukkit.getWorld(args[0]).getEntities()){
			                	if((!(e instanceof Player))&&(!(e instanceof ItemFrame))&&(!(e instanceof ArmorStand))){
			                		
			                		if(EntityClickListener.getEntities().contains(e)){
										continue;
									}
			                		
			                		if(UtilServer.getDeliveryPet()!=null){
										if(UtilServer.getDeliveryPet().getJockey()!=null&&UtilServer.getDeliveryPet().getJockey().getEntityId()==e.getEntityId())continue;
										if(UtilServer.getDeliveryPet().getEntity()!=null&&UtilServer.getDeliveryPet().getEntity().getEntityId()==e.getEntityId())continue;
									}

			                		if(UtilServer.getPerkManager()!=null&&UtilServer.getPerkManager().getEntity()!=null&&UtilServer.getPerkManager().getEntity().getEntityId()==e.getEntityId())continue;
									
									if(UtilServer.getGemsShop()!=null&&UtilServer.getGemsShop().getListener()!=null&&UtilServer.getGemsShop().getListener().getEntity()!=null){
										if(UtilServer.getGemsShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
									}

									if(UtilServer.getItemShop()!=null&&UtilServer.getItemShop().getListener()!=null&&UtilServer.getItemShop().getListener().getEntity()!=null){
										if(UtilServer.getItemShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
									}
									
			                		a++;
			                		e.remove();
			                	}
			                }
			                
			                player.sendMessage(TranslationHandler.getText(player, "PREFIX")+" All entities from "+args[0]+" removed:§e "+a);
						}else{
			                player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cThe world §e"+args[0]+"§c was not found!");
						}
					}
				}
			}
		}else{
			if(args.length==0){
				System.out.println("/entities [list/clearall/WORLD]");
			}else{
				if(args[0].equalsIgnoreCase("list")){
	                for(World world : Bukkit.getWorlds()){
	                	int tileEntities = 0;
	                    try
	                    {
	                      for (Chunk chunk : world.getLoadedChunks())
	                      {
	                        tileEntities += chunk.getTileEntities().length;
	                      }
	                    }
	                    catch (ClassCastException ex)
	                    {
	                  	 ex.printStackTrace(); 
	                    }  
	                    System.out.println(world.getName()+": Chunks:"+world.getLoadedChunks().length+" Entities:"+world.getEntities().size()+" Tile:§e"+tileEntities);
	                }
				}else if(args[0].equalsIgnoreCase("clearall")){
					int a = 0;
					for(World w : Bukkit.getWorlds()){
						for(Entity e : w.getEntities()){
		                	if((!(e instanceof Player))&&(!(e instanceof ItemFrame))&&(!(e instanceof ArmorStand))){

		                		if(EntityClickListener.getEntities().contains(e)){
									continue;
								}
								
		                		if(UtilServer.getDeliveryPet()!=null){
									if(UtilServer.getDeliveryPet().getJockey()!=null&&UtilServer.getDeliveryPet().getJockey().getEntityId()==e.getEntityId())continue;
									if(UtilServer.getDeliveryPet().getEntity()!=null&&UtilServer.getDeliveryPet().getEntity().getEntityId()==e.getEntityId())continue;
								}

		                		if(UtilServer.getPerkManager()!=null&&UtilServer.getPerkManager().getEntity()!=null&&UtilServer.getPerkManager().getEntity().getEntityId()==e.getEntityId())continue;
								
								if(UtilServer.getGemsShop()!=null){
									if(UtilServer.getGemsShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
								}

								if(UtilServer.getItemShop()!=null&&UtilServer.getItemShop().getListener()!=null&&UtilServer.getItemShop().getListener().getEntity()!=null){
									if(UtilServer.getItemShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
								}
		                		
		                		a++;
		                		e.remove();
		                	}
						}
						System.out.println(" All entities "+w.getName()+" removed: "+a);
					}
				}else{
					if(Bukkit.getWorld(args[0])!=null){
						int a = 0;
		                
		                for(Entity e : Bukkit.getWorld(args[0]).getEntities()){
		                	if((!(e instanceof Player))&&(!(e instanceof ItemFrame))&&(!(e instanceof ArmorStand))){

		                		if(EntityClickListener.getEntities().contains(e)){
									continue;
								}
								
		                		if(UtilServer.getDeliveryPet()!=null){
									if(UtilServer.getDeliveryPet().getJockey()!=null&&UtilServer.getDeliveryPet().getJockey().getEntityId()==e.getEntityId())continue;
									if(UtilServer.getDeliveryPet().getEntity()!=null&&UtilServer.getDeliveryPet().getEntity().getEntityId()==e.getEntityId())continue;
								}

		                		if(UtilServer.getPerkManager()!=null&&UtilServer.getPerkManager().getEntity()!=null&&UtilServer.getPerkManager().getEntity().getEntityId()==e.getEntityId())continue;
								
								if(UtilServer.getGemsShop()!=null){
									if(UtilServer.getGemsShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
								}

								if(UtilServer.getItemShop()!=null&&UtilServer.getItemShop().getListener()!=null&&UtilServer.getItemShop().getListener().getEntity()!=null){
									if(UtilServer.getItemShop().getListener().getEntity().getEntityId()==e.getEntityId())continue;
								}
		                		
		                		a++;
		                		e.remove();
		                	}
		                }
		                
		                System.out.println("All entities from "+args[0]+" removed:§e "+a);
					}else{
						System.out.println("The world "+args[0]+" was not found!");
					}
				}
			}
		}
		return false;
	}

}
