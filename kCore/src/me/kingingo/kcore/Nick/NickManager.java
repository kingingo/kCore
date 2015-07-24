package me.kingingo.kcore.Nick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.NPC.NPC;
import me.kingingo.kcore.NPC.NPCManager;
import me.kingingo.kcore.NPC.Event.PlayerInteractNPCEvent;
import me.kingingo.kcore.Nick.Command.CommandNick;
import me.kingingo.kcore.Nick.Events.BroadcastMessageEvent;
import me.kingingo.kcore.Nick.Events.PlayerListNameChangeEvent;
import me.kingingo.kcore.Nick.Events.PlayerSendMessageEvent;
import me.kingingo.kcore.PacketAPI.Packets.kDataWatcher;
import me.kingingo.kcore.PacketAPI.Packets.kGameProfile;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayInUseEntity;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo.kPlayerInfoData;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilReflection;
import me.kingingo.kcore.Util.UtilScoreboard;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.mojang.authlib.GameProfile;

public class NickManager extends kListener{

	@Getter
	private HashMap<Player,NPC> name = new HashMap<>();
	@Getter
	private CommandHandler cmd;
	@Getter
	private JavaPlugin instance;
	private NPCManager npcManager;
	
	
	public String[] nick = new String[]{"king","Exteme","Steve","Buddy","Flex","Apex","Captain","Tim"
			,"Gigga","AdamHD","Jesus","xgen","BTW","Robin","checker","dc","ingo","Style"
			,"Jonny","leon","Manii","Ginkis","eco","ungadunga","John","Samir","Pika","fredwa",
			"Dox","Dove","Ole","Crypt","Bro","zocker","jman","coder","win"};
	
	public NickManager(JavaPlugin instance,CommandHandler cmd,PermissionManager permManager){
		super(instance,"NickManager");
		this.cmd=cmd;
		this.instance=instance;
		cmd.register(CommandNick.class, new CommandNick(this));
	}
	
	kPacketPlayOutNamedEntitySpawn p2;
	kPacketPlayOutPlayerInfo info;
	PlayerInfoData data;
	List<PlayerInfoData> players;
	@EventHandler
	public void Receive(PacketListenerSendEvent ev){
		if(ev.getPacket() instanceof PacketPlayOutNamedEntitySpawn&&ev.getPlayer()!=null){
			p2=new kPacketPlayOutNamedEntitySpawn( ((PacketPlayOutNamedEntitySpawn)ev.getPacket()) );
			for(Player p : name.keySet()){
				if(p2.getUUID() == p.getUniqueId()){
					Log("SEND NAME " + (ev.getPlayer()!=null?ev.getPlayer().getName():"NULL"));
			        UtilPlayer.sendPacket(ev.getPlayer(), name.get(p).getSpawn_packet());
			        ev.setCancelled(true);
			        break;
				}
			}
		}else if(ev.getPacket() instanceof kPacketPlayOutPlayerInfo&&ev.getPlayer()!=null){
//			Log("SEND INFo");
//	        info=new kPacketPlayOutPlayerInfo( ((PacketPlayOutPlayerInfo)ev.getPacket()) );
//	        
//	        for(Player p : name.keySet()){
//				if(info.getList() == p.getUniqueId()){
//					
//			        ev.setPacket(name.get(p).getSpawn_packet().getPacket());
//			        break;
//				}
//			}
//	        
//	        data = info.new kPlayerInfoData(info, new kGameProfile(ev.getPlayer().getUniqueId(), name.get(ev.getPlayer()).getName()),name.get(ev.getPlayer()).getName());
//			 players = info.getList();
//	         players.clear();
//	         players.add(data);
//	         
//	        ev.setPacket(info.getPacket());
		}
	}
	
	public String setNick(Player player){
		if(name.containsKey(player))name.remove(player);
		String n=RandomNick();
		return setNick(player, n);
	}
	
	public String RandomNick(){
		String n = nick[UtilMath.r(nick.length)];
		int len = UtilMath.RandomInt(3, 1);
		for(int i=0; i<len; i++){
			n=n+UtilMath.r(9);
		}
		return n;
	}
	
	public String setNick(Player player,String nick){
		if(name.containsKey(player))name.remove(player);
		
		try{
	         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
	         
	         PlayerInfoData data1 = packet.new kPlayerInfoData(packet, new kGameProfile(player.getUniqueId(), player.getName()),player.getName());
	         List<PlayerInfoData> players = packet.getList();
	         players.add(data1);

	         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
	         packet.setList(players);
      
		 for(Player o : Bukkit.getOnlinePlayers()){
           if(!o.getName().equals(player.getName())){
            UtilPlayer.sendPacket(o, packet);
           }
		 }
		 
		}catch(Exception e) {
	        e.printStackTrace();
	     }
		
		NPC npc = npcManager.createNPC("§7"+nick, player.getLocation());
		npc.setEntityID(player.getEntityId());
		npc.setNotsend(new ArrayList<Player>());
		npc.getNotsend().add(player);
		npc.despawn();
		npc.spawn();
		name.put(player, npc);
 		
		return nick;
	}
	
	public void delNick(Player player){
		if(name.containsKey(player))name.remove(player);
		
	}
	
//	@EventHandler(priority=EventPriority.LOWEST)
//	public void PlayerListName(PlayerListNameChangeEvent ev){
//		if(name.containsKey(ev.getPlayer()))ev.setNick(ev.getNick().replaceAll(ev.getPlayer().getName(), name.get(ev.getPlayer())));
//	}
//	
//	@EventHandler(priority=EventPriority.LOWEST)
//	public void AsyncChat(AsyncPlayerChatEvent ev){
//		if(name.containsKey(ev.getPlayer()))ev.setMessage(ev.getMessage().replaceAll(ev.getPlayer().getName(), name.get(ev.getPlayer())));
//	}
//	
//	@EventHandler(priority=EventPriority.LOWEST)
//	public void SendMessage(PlayerSendMessageEvent ev){
//		for(Player player : name.keySet()){
//			if(ev.getMessage().equalsIgnoreCase(player.getName())){
//				ev.setMessage(ev.getMessage().replaceAll(player.getName(), name.get(player)));
//			}
//		}
//	}
//	
//	@EventHandler(priority=EventPriority.LOWEST)
//	public void BroadcastMessage(BroadcastMessageEvent ev){
//		for(Player player : name.keySet()){
//			if(ev.getMessage().equalsIgnoreCase(player.getName())){
//				ev.setMessage(ev.getMessage().replaceAll(player.getName(), name.get(player)));
//			}
//		}
//	}
	
}
