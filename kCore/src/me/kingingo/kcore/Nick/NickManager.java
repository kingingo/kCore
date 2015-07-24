package me.kingingo.kcore.Nick;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.livings.DisguisePlayer;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Nick.Events.BroadcastMessageEvent;
import me.kingingo.kcore.Nick.Events.PlayerSendMessageEvent;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.NICK_DEL;
import me.kingingo.kcore.Packet.Packets.NICK_SET;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo.kPlayerInfoData;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NickManager extends kListener{

	@Getter
	private HashMap<Integer,DisguisePlayer> nicks = new HashMap<>();
	
	public NickManager(JavaPlugin instance){
		super(instance,"NickManager");
		UtilServer.createPacketListener(instance);
	}
	
	public boolean hasNick(LivingEntity entity){
		return getNicks().containsKey(entity.getEntityId());
	}
	
	public String setNick(Player player,String nick){
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
		
		return nick;
	}
	
	public DisguisePlayer getNick(LivingEntity entity){
		if(!hasNick(entity))return null;
		return getNicks().get(entity.getEntityId());
	}
	
	public void delNick(Player entity){
		if(hasNick(entity)){
			DisguiseBase disguise = getNick(entity);
		    kPacketPlayOutEntityDestroy de = new kPacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
		    kPacketPlayOutNamedEntitySpawn s = new kPacketPlayOutNamedEntitySpawn( ((CraftPlayer)entity).getHandle() );
			for(Player player : UtilServer.getPlayers()){
				if(entity!=player){
					UtilPlayer.sendPacket(player, de);
					if(disguise instanceof DisguisePlayer)UtilPlayer.sendPacket(player, ((DisguisePlayer)disguise).removeFromTablist());
					UtilPlayer.sendPacket(player, s);
					if(entity instanceof Player){
						player.showPlayer(((Player)entity));
					}
				}
			}
			getNicks().remove(disguise);
		}
	}
	
	kPacketPlayOutSpawnEntityLiving entityLiving;
	kPacketPlayOutNamedEntitySpawn namedEntitySpawn;
	kPacketPlayOutEntityMetadata entityMetadata;
	kPacketPlayOutPlayerInfo info;
	kPlayerInfoData data;
	@EventHandler
	public void Send(PacketListenerSendEvent ev){
		if(ev.getPlayer()!=null&&!ev.getPlayer().hasPermission(kPermission.NICK_SEE.getPermissionToString())){
			if(ev.getPacket() instanceof PacketPlayOutSpawnEntityLiving){
				if( entityLiving == null )entityLiving=new kPacketPlayOutSpawnEntityLiving();
				entityLiving.setPacket(((PacketPlayOutSpawnEntityLiving)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=entityLiving.getEntityID()&&getNicks().containsKey(entityLiving.getEntityID())){
					ev.setPacket(getNicks().get(entityLiving.getEntityID()).GetSpawnPacket().getPacket());
				}
			}else if(ev.getPacket() instanceof PacketPlayOutNamedEntitySpawn){
				if( namedEntitySpawn == null )namedEntitySpawn=new kPacketPlayOutNamedEntitySpawn();
				namedEntitySpawn.setPacket(((PacketPlayOutNamedEntitySpawn)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=namedEntitySpawn.getEntityID()&&getNicks().containsKey(namedEntitySpawn.getEntityID())){
					if(getNicks().get(namedEntitySpawn.getEntityID()) instanceof DisguisePlayer)UtilPlayer.sendPacket(ev.getPlayer(), ((DisguisePlayer)getNicks().get(namedEntitySpawn.getEntityID())).getTabList());
					ev.setPacket(getNicks().get(namedEntitySpawn.getEntityID()).GetSpawnPacket().getPacket());
				}
			}else if(ev.getPacket() instanceof PacketPlayOutEntityMetadata){
				if( entityMetadata == null )entityMetadata=new kPacketPlayOutEntityMetadata();
				entityMetadata.setPacket(((PacketPlayOutEntityMetadata)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=entityMetadata.getEntityID()&&getNicks().containsKey(entityMetadata.getEntityID())){
					ev.setPacket( getNicks().get(entityMetadata.getEntityID()).GetMetaDataPacket().getPacket());
				}
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
			}
		}
	}
	
	@EventHandler
	public void QUIT(PlayerQuitEvent ev){
		if(hasNick(ev.getPlayer()))delNick(ev.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void AsyncChat(AsyncPlayerChatEvent ev){
		if(hasNick(ev.getPlayer()))ev.setMessage(ev.getMessage().replaceAll(ev.getPlayer().getName(), getNicks().get(ev.getPlayer().getEntityId()).getName()));
	}
	
	Player player;
	@EventHandler(priority=EventPriority.LOWEST)
	public void SendMessage(PlayerSendMessageEvent ev){
		for(int id : getNicks().keySet()){
			player=Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID());
			if(ev.getMessage().equalsIgnoreCase(player.getName())){
				ev.setMessage(ev.getMessage().replaceAll(player.getName(), getNicks().get(player).getName()));
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void BroadcastMessage(BroadcastMessageEvent ev){
		for(Integer id : getNicks().keySet()){
			player=Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID());
			if(ev.getMessage().equalsIgnoreCase(player.getName())){
				ev.setMessage(ev.getMessage().replaceAll(player.getName(), getNicks().get(id).getName()));
			}
		}
	}
	
}
