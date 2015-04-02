package me.kingingo.kcore.Permission;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PERMISSION_GROUP_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_REMOVE_ALL;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilPlayer;
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
	
	@EventHandler
	public void loadList(UpdateEvent ev){
		if(ev.getType()==UpdateType.SEC_3){
			if(!manager.getLoad_now().isEmpty()){
				cloned=(ArrayList<UUID>)manager.getLoad_now().clone();
				for(Player player : UtilServer.getPlayers()){
					uuid=UtilPlayer.getRealUUID(player);
					if(cloned.contains(uuid)){
						if(UtilPlayer.getRealUUID(player).equals(uuid)){
							if(!manager.getPlist().containsKey(uuid))manager.getPlist().put(uuid, player.addAttachment(manager.getInstance()));
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
										manager.getPlist().get(uuid).setPermission(perm.toLowerCase(), true);
									}
								}	
								
								for(String perm : manager.getLoad().get(uuid)){
									if(perm.substring(0, 1).equalsIgnoreCase("-")){
										manager.getPlist().get(uuid).unsetPermission(perm.substring(1, perm.length()).toLowerCase());
										manager.getPlist().get(uuid).setPermission(perm.substring(1, perm.length()).toLowerCase(), false);
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
											manager.getPlist().get(uuid).setPermission(perm.toLowerCase(), true);	
										}
									}
								}
								
								if(!player.isOp()){
									for(String perm : manager.getGroups().get(manager.getPgroup().get(uuid)).getPerms()){
										if(perm.substring(0, 1).equalsIgnoreCase("-")){
											manager.getPlist().get(uuid).unsetPermission(perm.substring(1, perm.length()));
											manager.getPlist().get(uuid).setPermission(perm.substring(1, perm.length()).toLowerCase(), false);
										}
									}
								}
							}
							player.recalculatePermissions();
							manager.getLoad().remove(uuid);
							manager.getLoad_now().remove(uuid);
						}
					}	
				}	
				cloned.clear();
				cloned=null;
			}
		}
	}
	
	boolean b = false;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_10){
			for(Player player : UtilServer.getPlayers()){
				if(manager.getPlist().containsKey(UtilPlayer.getRealUUID(player))){
//					player.removeAttachment(manager.getPlist().get(UtilPlayer.getRealUUID(player)));
//					manager.getPlist().get(UtilPlayer.getRealUUID(player)).remove();
//					manager.getPlist().remove(UtilPlayer.getRealUUID(player));
					manager.loadPermission(UtilPlayer.getRealUUID(player));
				}
			}
		}else if(ev.getType()==UpdateType.MIN_64){
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
//			if(Bukkit.getPluginManager().getPlugin("PermissionsEx")!=null){
//				ru.tehkode.permissions.PermissionManager pex = ((PermissionsEx)Bukkit.getPluginManager().getPlugin("PermissionEx")).getPermissionsManager();
//				
//				for(String world : pex.getUser(packet.getUuid()).getAllPermissions().keySet()){
//					for(String permission : pex.getUser(packet.getUuid()).getAllPermissions().get(world)){
//						pex.getUser(packet.getUuid()).removePermission(permission, world);
//					}
//				}
//				for(String world : pex.getUser(packet.getUuid()).getAllGroups().keySet()){
//					for(PermissionGroup group : pex.getUser(packet.getUuid()).getAllGroups().get(world)){
//						pex.getUser(packet.getUuid()).removeGroup(group, world);
//					}
//				}
//			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Kick(PlayerKickEvent ev){
		if(manager.getPlist().containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			ev.getPlayer().removeAttachment(manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())));
			manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())).remove();
			manager.getPlist().remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(manager.getPlist().containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
			ev.getPlayer().removeAttachment(manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())));
			manager.getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())).remove();
			manager.getPlist().remove(UtilPlayer.getRealUUID(ev.getPlayer()));
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Join(PlayerJoinEvent ev){
		manager.setTabList(ev.getPlayer());
	}
}