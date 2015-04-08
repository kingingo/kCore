package me.kingingo.kcore.Permission;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PERMISSION_GROUP_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_REMOVE_ALL;
import me.kingingo.kcore.Permission.Event.PlayerLoadPermissionEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilReflection;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import com.earth2me.essentials.Essentials;

public class PermissionListener implements Listener {
	
	private PermissionManager manager;
	@Getter
	private ArrayList<UUID> cloned;
	private UUID uuid;
	
	public PermissionListener(PermissionManager manager){
		this.manager=manager;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
	    manager.loadPermission(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
	}
	
	String p;
	@EventHandler
	public void loadList(UpdateEvent ev){
		if(ev.getType()==UpdateType.SEC_3){
			if(!manager.getLoad_now().isEmpty()){
				cloned=(ArrayList<UUID>)manager.getLoad_now().clone();
				for(Player player : UtilServer.getPlayers()){
					uuid=UtilPlayer.getRealUUID(player);
					if(cloned.contains(uuid)){
						if(UtilPlayer.getRealUUID(player).equals(uuid)){
							if(player.isOp())player.setOp(false);
							if(!manager.getPlist().containsKey(uuid))manager.getPlist().put(uuid, player.addAttachment(manager.getInstance()));
							Map<String,Boolean> list = reflectMap(manager.getPlist().get(uuid));
							list.clear();
							
							if(manager.getLoad().containsKey(uuid)){
								
								for(String perm : manager.getLoad().get(uuid)){
									if(perm.equalsIgnoreCase(kPermission.ALL_PERMISSION.getPermissionToString())){
										player.setOp(true);
										break;
									}
									if(!player.isOp())if(!perm.substring(0, 1).equalsIgnoreCase("-")&&!manager.getPlist().get(uuid).getPermissions().containsKey(perm.toLowerCase())){
										if(Bukkit.getPluginManager().getPermission(perm)==null){
							    			  Bukkit.getPluginManager().addPermission(new Permission(perm));
							    		}
										list.put(perm.toLowerCase(), true);
										
										if(perm.startsWith("epicpvp.timer.")){
											p = perm.substring(0, ("epicpvp.timer.".length()+ perm.substring("epicpvp.timer.".length(),perm.length()).substring(0,perm.substring("epicpvp.timer.".length(),perm.length()).indexOf(".")).length())).toLowerCase();
											if(!manager.getPlist().get(uuid).getPermissions().containsKey(p)){
												if(Bukkit.getPluginManager().getPermission(p)==null){
													Bukkit.getPluginManager().addPermission(new Permission(p));
												}
												list.put(p.toLowerCase(), true);
											}
										}
									}
								}	
							}
							
							if(manager.getPgroup().containsKey(uuid)){
								if(!player.isOp()){
									for(String perm : manager.getGroups().get(manager.getPgroup().get(uuid)).getPerms() ){
										if(perm.equalsIgnoreCase(kPermission.ALL_PERMISSION.getPermissionToString())){
											player.setOp(true);
											break;
										}
										if(!perm.substring(0, 1).equalsIgnoreCase("-")&&!manager.getPlist().get(uuid).getPermissions().containsKey(perm.toLowerCase())){
											if(Bukkit.getPluginManager().getPermission(perm)==null){
								    			  Bukkit.getPluginManager().addPermission(new Permission(perm));
								    		}
											
											list.put(perm.toLowerCase(), true);	
											
											if(perm.startsWith("epicpvp.timer.")){
												p = perm.substring(0, ("epicpvp.timer.".length()+ perm.substring("epicpvp.timer.".length(),perm.length()).substring(0,perm.substring("epicpvp.timer.".length(),perm.length()).indexOf(".")).length())).toLowerCase();
												if(!manager.getPlist().get(uuid).getPermissions().containsKey(p)){
													if(Bukkit.getPluginManager().getPermission(p)==null){
														Bukkit.getPluginManager().addPermission(new Permission(p));
													}
													list.put(p.toLowerCase(), true);
												}
											}
										}
									}
								}
								
								if(!player.isOp()){
									for(String perm : manager.getGroups().get(manager.getPgroup().get(uuid)).getPerms()){
										if(perm.substring(0, 1).equalsIgnoreCase("-")){
											list.remove(perm.substring(1, perm.length()));
											//list.put(perm.substring(1, perm.length()).toLowerCase(), false);
										}
									}
									
									if(manager.getLoad().containsKey(uuid)){
										for(String perm : manager.getLoad().get(uuid)){
											if(perm.substring(0, 1).equalsIgnoreCase("-")){
												list.remove(perm.substring(1, perm.length()).toLowerCase());
												//list.put(perm.substring(1, perm.length()).toLowerCase(), false);
											}
										}
									}
								}
							}
							
//							if(Bukkit.getPluginManager().getPlugin("Essentials")!=null){
//								Essentials ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
//								ess.getPermissionsHandler()
//							}
							
							player.recalculatePermissions();
							manager.getLoad().remove(uuid);
							manager.getLoad_now().remove(uuid);
							manager.setTabList(player);
							Bukkit.getPluginManager().callEvent(new PlayerLoadPermissionEvent(manager, player));
						}
					}	
				}	
				cloned.clear();
				cloned=null;
			}
		}
	}
	
	private Map<String, Boolean> reflectMap(PermissionAttachment attachment){
	    try
	    {
	        Field pField = PermissionAttachment.class.getDeclaredField("permissions");
	        pField.setAccessible(true);
	      
	      return (Map)pField.get(attachment);
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	  }
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_64){
			UtilList.CleanList(manager.getPgroup());
			UtilList.CleanList(manager.getPlist());
			UtilList.CleanList(manager.getLoad());
			UtilList.CleanList(manager.getLoad_now());
		}
	}
	
	@EventHandler
	public void Packet(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof PERMISSION_GROUP_RELOAD){
			PERMISSION_GROUP_RELOAD packet = (PERMISSION_GROUP_RELOAD)ev.getPacket();
			manager.getGroups().remove(packet.getGroup().toLowerCase());
			manager.loadGroup(packet.getGroup().toLowerCase());
		}else if(ev.getPacket() instanceof PERMISSION_USER_RELOAD){
			PERMISSION_USER_RELOAD packet = (PERMISSION_USER_RELOAD)ev.getPacket();
			manager.getPgroup().remove(packet.getUuid());
			if(manager.getPlist().containsKey(packet.getUuid())){
				manager.getPlist().get(packet.getUuid()).remove();
			}
			manager.getPlist().remove(packet.getUuid());
			manager.loadPermission(packet.getUuid());
		}else if(ev.getPacket() instanceof PERMISSION_USER_REMOVE_ALL){
			PERMISSION_USER_REMOVE_ALL packet = (PERMISSION_USER_REMOVE_ALL)ev.getPacket();
			manager.getPgroup().remove(packet.getUuid());
			if(manager.getPlist().containsKey(packet.getUuid())){
				manager.getPlist().get(packet.getUuid()).remove();
			}
			manager.getPlist().remove(packet.getUuid());
			Bukkit.getServer().getOperators().remove(Bukkit.getOfflinePlayer(packet.getUuid()));
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Kick(PlayerKickEvent ev){
		if(manager.getPlist().containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			ev.getPlayer().removeAttachment(manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())));
			manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())).remove();
			manager.getPlist().remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
		if(ev.getPlayer().isOp())ev.getPlayer().setOp(false);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(manager.getPlist().containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			ev.getPlayer().removeAttachment(manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())));
			manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())).remove();
			manager.getPlist().remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
	}
	
}