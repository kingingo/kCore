package me.kingingo.kcore.Nick;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.livings.DisguisePlayer;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Game.Events.GameStartEvent;
import me.kingingo.kcore.Game.Events.GameStateChangeEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.NICK_DEL;
import me.kingingo.kcore.Packet.Packets.NICK_SET;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutChat;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutScoreboardTeam;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutScoreboardTeam.Modes;
import me.kingingo.kcore.PacketAPI.Packets.kPlayerInfoData;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.Event.PlayerLoadPermissionEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mojang.authlib.GameProfile;

public class NickManager extends kListener{

	@Getter
	private HashMap<Integer,DisguisePlayer> nicks = new HashMap<>();
	private HashMap<UUID,String> wait = new HashMap<>();
	@Getter
	private PermissionManager permissionManager;
	private boolean refresh=false;
	private boolean game=false;
	
	public NickManager(PermissionManager permissionManager){
		super(permissionManager.getInstance(),"NickManager");
		permissionManager.setNickManager(this);
		UtilServer.createPacketListener(permissionManager.getInstance());
		this.permissionManager=permissionManager;
	}
	
	@EventHandler
	public void s(GameStateChangeEvent ev){
		if(ev.getTo()==GameState.Restart){
			for(Player player : UtilServer.getPlayers()){
				if(hasNick(player)){
					delNick(player);
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_DISALLOW_TEMPORÄR"));
				}
			}
		}
	}
	
//	public void updateScoreboard(Player player){
//		if(nicks.isEmpty()||player.hasPermission(kPermission.NICK_SEE.getPermissionToString()))return;
//		for(Team team : player.getScoreboard().getTeams()){
//			if(team.getPrefix()!=null){
//				for(OfflinePlayer oplayer : team.getPlayers()){
//					if(UtilPlayer.isOnline(oplayer.getUniqueId())){
//						if(oplayer.getUniqueId()==player.getUniqueId())continue;
//						if(nicks.containsKey(oplayer.getPlayer().getEntityId())){
//							UtilPlayer.sendPacket(player, getNick(oplayer.getPlayer()).updateTabList(team.getPrefix()));
//							team.addEntry(getNick(oplayer.getPlayer()).getName());
//						}
//					}
//				}
//			}
//		}
//	}
	
	@EventHandler
	public void t(GameStartEvent ev){
		game=true;
	}
	
	@EventHandler
	public void update(UpdateEvent ev){
		if(ev.getType()==UpdateType.SEC_3&&refresh){
			for(Player p : UtilServer.getPlayers()){
				permissionManager.setTabList(p);
			}
			refresh=false;
		}
	}
	
	public boolean hasNick(LivingEntity entity){
		return getNicks().containsKey(entity.getEntityId());
	}
	
	public String setNick(Player player,String nick){
		if(game)return null;
		if(hasNick(player))delNick(player);
		DisguisePlayer disguise = new DisguisePlayer(player,nick);
		kPacketPlayOutEntityDestroy destroy = new kPacketPlayOutEntityDestroy(player.getEntityId());
		getNicks().put(player.getEntityId(), disguise);
		for(Player p : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)p).getHandle()){
				if(!p.hasPermission(kPermission.NICK_SEE.getPermissionToString())){
					UtilPlayer.sendPacket(p, destroy);
					UtilPlayer.sendPacket(p, disguise.getTabList());
					UtilPlayer.sendPacket(p, disguise.GetSpawnPacket());
				}
			}
		}
		player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_SET_NICK",nick));
		refresh=true;
		return nick;
	}
	
	public DisguisePlayer getNick(LivingEntity entity){
		if(!hasNick(entity))return null;
		return getNicks().get(entity.getEntityId());
	}
	
	public kPacket getTabList(GameProfile g) {
	      try {
	         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
	         PlayerInfoData data = new kPlayerInfoData(packet,g,g.getName());
	         List<PlayerInfoData> players = packet.getList();
	         players.add(data);
	         
	         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
	         packet.setList(players);

	         return packet;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return null;
	   }
	
	public void delNick(Player entity){
		if(hasNick(entity)){
			DisguiseBase disguise = getNick(entity);
			getNicks().remove(entity.getEntityId());
		    kPacketPlayOutEntityDestroy de = new kPacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
		    kPacketPlayOutNamedEntitySpawn s = new kPacketPlayOutNamedEntitySpawn( ((CraftPlayer)entity).getHandle() );
		    kPacket tab = getTabList( ((CraftPlayer)entity).getHandle().getProfile() );
			for(Player player : UtilServer.getPlayers()){
				if(entity.getEntityId()!=player.getEntityId()&&!player.hasPermission(kPermission.NICK_SEE.getPermissionToString())){
					UtilPlayer.sendPacket(player, de);
					if(disguise instanceof DisguisePlayer)UtilPlayer.sendPacket(player, ((DisguisePlayer)disguise).removeFromTablist());
					UtilPlayer.sendPacket(player, tab);
					UtilPlayer.sendPacket(player, s);
					if(entity instanceof Player){
						player.showPlayer(((Player)entity));
					}
				}
			}
			refresh=true;
		}
	}
	
	@EventHandler
	public void Send(PacketListenerSendEvent ev){
		if(nicks.isEmpty())return;
		if(ev.getPlayer()!=null&&ev.getPacket()!=null){
			if(!permissionManager.haskPermission(ev.getPlayer(),kPermission.NICK_SEE)){
				if(ev.getPacket() instanceof PacketPlayOutNamedEntitySpawn){
					kPacketPlayOutNamedEntitySpawn namedEntitySpawn=new kPacketPlayOutNamedEntitySpawn();
					namedEntitySpawn.setPacket(((PacketPlayOutNamedEntitySpawn)ev.getPacket()));
					if(ev.getPlayer().getEntityId()!=namedEntitySpawn.getEntityID()&&getNicks().containsKey(namedEntitySpawn.getEntityID())&&getNicks().get(namedEntitySpawn.getEntityID())!=null){
						if(getNicks().get(namedEntitySpawn.getEntityID()) instanceof DisguisePlayer){
							UtilPlayer.sendPacket(ev.getPlayer(), ((DisguisePlayer)getNicks().get(namedEntitySpawn.getEntityID())).getTabList());
						}
						ev.setPacket(getNicks().get(namedEntitySpawn.getEntityID()).GetSpawnPacket().getPacket());
					}
					namedEntitySpawn.setPacket(null);
					namedEntitySpawn=null;
				}else if(ev.getPacket() instanceof PacketPlayOutEntityMetadata){
					kPacketPlayOutEntityMetadata entityMetadata=new kPacketPlayOutEntityMetadata();
					entityMetadata.setPacket(((PacketPlayOutEntityMetadata)ev.getPacket()));
					if(ev.getPlayer().getEntityId()!=entityMetadata.getEntityID()&&getNicks().containsKey(entityMetadata.getEntityID())&&getNicks().get(entityMetadata.getEntityID())!=null){
						ev.setPacket( getNicks().get(entityMetadata.getEntityID()).GetMetaDataPacket().getPacket() );
					}
					entityMetadata.setPacket(null);
					entityMetadata=null;
				}else if(ev.getPacket() instanceof PacketPlayOutChat){
					kPacketPlayOutChat chat = new kPacketPlayOutChat();
					chat.setPacket( ((PacketPlayOutChat)ev.getPacket()) );
					
					if(chat.getIChatBaseComponent()!=null){
						String txt=CraftChatMessage.fromComponent(chat.getIChatBaseComponent());
						for(int id : getNicks().keySet()){
							if(!UtilPlayer.isOnline(getNicks().get(id).GetEntity().getUniqueID()))continue;
							if(txt.contains(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()).getName())){
								String prefix=getPermissionManager().getPrefix(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()));
								if(prefix!=null&&txt.contains(prefix))txt=txt.replace(prefix, "");
								txt=txt.replaceAll(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()).getName(), getNicks().get(id).getName());
								prefix=null;
							}
						}
						
						chat.setIChatBaseComponent( CraftChatMessage.fromString(txt)[0] );
						ev.setPacket(chat.getPacket());
						chat.setPacket(null);
						chat=null;
						txt=null;
					}
				}else if(ev.getPacket() instanceof PacketPlayOutScoreboardTeam){
					kPacketPlayOutScoreboardTeam st = new kPacketPlayOutScoreboardTeam( ((PacketPlayOutScoreboardTeam)ev.getPacket()) );
					if(st.getMode()==Modes.PLAYERS_ADDED&&st.getPrefix()!=null&&st.getPlayers().size()>0){
						String player;
						for(int i = 0; i < st.getPlayers().size() ; i++){
							player=(String)st.getPlayers().toArray()[i];
							if(UtilPlayer.isOnline(player)){
								if(nicks.containsKey( Bukkit.getPlayer(player).getEntityId() )){
									st.setPrefix( ev.getPlayer().getScoreboard().getTeam(st.getTeamName()).getPrefix() );
									st.removePlayer(player);
									st.addPlayer(getNick(Bukkit.getPlayer(player)).getName());
									UtilPlayer.sendPacket(ev.getPlayer(), getNick(Bukkit.getPlayer(player)).updateTabList(st.getPrefix()));
								}
							}
						}
						ev.setPacket(st.getPacket());
						player=null;
					}
					st=null;
				}
			}else{
				if(ev.getPacket() instanceof PacketPlayOutChat){
					kPacketPlayOutChat chat = new kPacketPlayOutChat();
					chat.setPacket( ((PacketPlayOutChat)ev.getPacket()) );
						
					if(chat.getIChatBaseComponent()!=null){
						String txt=CraftChatMessage.fromComponent(chat.getIChatBaseComponent());
						for(int id : getNicks().keySet()){
							if(!UtilPlayer.isOnline(getNicks().get(id).GetEntity().getUniqueID()))continue;
							if(txt.contains(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()).getName())){
								txt=txt.replaceAll(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()).getName(),Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()).getName()+" §7§o["+getNicks().get(id).getName()+"§7§o]");
							}
						}
						chat.setIChatBaseComponent( CraftChatMessage.fromString(txt)[0] );
						ev.setPacket(chat.getPacket());
						chat.setPacket(null);
						chat=null;
						txt=null;
					}
				}
//				else if(ev.getPacket() instanceof PacketPlayOutScoreboardTeam){
//					kPacketPlayOutScoreboardTeam st = new kPacketPlayOutScoreboardTeam( ((PacketPlayOutScoreboardTeam)ev.getPacket()) );
//					if(st.getMode()==Modes.PLAYERS_ADDED&&st.getPrefix()!=null&&st.getPlayers().size()>0){
//						String player;
//						for(int i = 0; i < st.getPlayers().size() ; i++){
//							player=(String)st.getPlayers().toArray()[i];
//							if(UtilPlayer.isOnline(player)){
//								if(nicks.containsKey( Bukkit.getPlayer(player).getEntityId() )){
//									if(ev.getPlayer().getScoreboard().getTeam(st.getTeamName())==null){
//										System.out.println(st.getTeamName()+" == null "+ ev.getPlayer().getName()+" "+player);
//										continue;
//									}
//									st.setPrefix( ev.getPlayer().getScoreboard().getTeam(st.getTeamName()).getPrefix() );
//									if(ev.getPlayer().getScoreboard().getTeam(player)==null){
//										UtilScoreboard.addTeam(
//												ev.getPlayer().getScoreboard()
//												, player
//												, st.getPrefix()
//												, "§7 "+getNick(Bukkit.getPlayer(player)).getName());
//									}
//									st.setTeamName(player);
//								}
//							}
//						}
//						ev.setPacket(st.getPacket());
//						player=null;
//					}
//					st=null;
//				}
			}
		}
	}
	
	NICK_DEL del;
	NICK_SET set;
	@EventHandler
	public void receive(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof NICK_DEL){
			del=(NICK_DEL)ev.getPacket();
			if(UtilPlayer.isOnline(del.getUuid())){
				delNick(Bukkit.getPlayer(del.getUuid()));
			}
		}else if(ev.getPacket() instanceof NICK_SET){
			set=(NICK_SET)ev.getPacket();
			if(UtilPlayer.isOnline(set.getUuid())){
				setNick(Bukkit.getPlayer(set.getUuid()), set.getNick());
			}else{
				wait.put(set.getUuid(), set.getNick());
			}
		}
	}
	
	@EventHandler
	public void load(PlayerLoadPermissionEvent ev){
		if(wait.containsKey(ev.getPlayer().getUniqueId())){
			setNick(ev.getPlayer(), wait.get(ev.getPlayer().getUniqueId()));
			wait.remove(ev.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void QUIT(PlayerQuitEvent ev){
		if(hasNick(ev.getPlayer()))delNick(ev.getPlayer());
	}
}

