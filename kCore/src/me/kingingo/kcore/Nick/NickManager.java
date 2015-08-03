package me.kingingo.kcore.Nick;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.livings.DisguisePlayer;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.NICK_DEL;
import me.kingingo.kcore.Packet.Packets.NICK_SET;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutChat;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo.kPlayerInfoData;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilReflection;
import me.kingingo.kcore.Util.UtilServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_8_R3.ChatBaseComponent;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.ChatComponentUtils;
import net.minecraft.server.v1_8_R3.ChatDeserializer;
import net.minecraft.server.v1_8_R3.ChatModifier;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NickManager extends kListener{

	@Getter
	private HashMap<Integer,DisguisePlayer> nicks = new HashMap<>();
	private HashMap<UUID,String> wait = new HashMap<>();
	@Getter
	private PermissionManager permissionManager;
	
	public NickManager(PermissionManager permissionManager){
		super(permissionManager.getInstance(),"NickManager");
		UtilServer.createPacketListener(permissionManager.getInstance());
		this.permissionManager=permissionManager;
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
	kPacketPlayOutChat chat;
	String txt;
	String prefix;
	@EventHandler
	public void Send(PacketListenerSendEvent ev){
		if(nicks.isEmpty())return;
		if(ev.getPlayer()!=null&&!ev.getPlayer().hasPermission(kPermission.NICK_SEE.getPermissionToString())){
			if(ev.getPacket() instanceof PacketPlayOutSpawnEntityLiving){
				if( entityLiving == null )entityLiving=new kPacketPlayOutSpawnEntityLiving();
				entityLiving.setPacket(((PacketPlayOutSpawnEntityLiving)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=entityLiving.getEntityID()&&getNicks().containsKey(entityLiving.getEntityID())&&getNicks().get(entityMetadata.getEntityID())!=null){
					ev.setPacket(getNicks().get(entityLiving.getEntityID()).GetSpawnPacket().getPacket());
				}
			}else if(ev.getPacket() instanceof PacketPlayOutNamedEntitySpawn){
				if( namedEntitySpawn == null )namedEntitySpawn=new kPacketPlayOutNamedEntitySpawn();
				namedEntitySpawn.setPacket(((PacketPlayOutNamedEntitySpawn)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=namedEntitySpawn.getEntityID()&&getNicks().containsKey(namedEntitySpawn.getEntityID())&&getNicks().get(entityMetadata.getEntityID())!=null){
					if(getNicks().get(namedEntitySpawn.getEntityID()) instanceof DisguisePlayer)UtilPlayer.sendPacket(ev.getPlayer(), ((DisguisePlayer)getNicks().get(namedEntitySpawn.getEntityID())).getTabList());
					ev.setPacket(getNicks().get(namedEntitySpawn.getEntityID()).GetSpawnPacket().getPacket());
				}
			}else if(ev.getPacket() instanceof PacketPlayOutEntityMetadata){
				if( entityMetadata == null )entityMetadata=new kPacketPlayOutEntityMetadata();
				entityMetadata.setPacket(((PacketPlayOutEntityMetadata)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=entityMetadata.getEntityID()&&getNicks().containsKey(entityMetadata.getEntityID())&&getNicks().get(entityMetadata.getEntityID())!=null){
					ev.setPacket( getNicks().get(entityMetadata.getEntityID()).GetMetaDataPacket().getPacket() );
				}
			}else if(ev.getPacket() instanceof PacketPlayOutChat){
				if(chat==null)chat = new kPacketPlayOutChat();
				chat.setPacket( ((PacketPlayOutChat)ev.getPacket()) );
				
				if(chat.getIChatBaseComponent()!=null){
					txt=CraftChatMessage.fromComponent(chat.getIChatBaseComponent());
					for(int id : getNicks().keySet()){
						if(!UtilPlayer.isOnline(getNicks().get(id).GetEntity().getUniqueID()))continue;
						if(txt.contains(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()).getName())){
							prefix=getPermissionManager().getPrefix(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()));
							if(prefix!=null&&txt.contains(prefix))txt=txt.replace(prefix, "");
							txt=txt.replaceAll(Bukkit.getPlayer(getNicks().get(id).GetEntity().getUniqueID()).getName(), getNicks().get(id).getName());
						}
					}
					prefix=null;
					chat.setIChatBaseComponent( CraftChatMessage.fromString(txt)[0] );
					ev.setPacket(chat.getPacket());
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
			}else{
				wait.put(set.getUuid(), set.getNick());
			}
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
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
