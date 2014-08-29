package me.kingingo.kcore.NickManager;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Hologram.wrapper.WrapperPlayServerNamedEntitySpawn;
import me.kingingo.kcore.NickManager.Command.CommandNick;
import me.kingingo.kcore.NickManager.Events.BroadcastMessageEvent;
import me.kingingo.kcore.NickManager.Events.PlayerListNameChangeEvent;
import me.kingingo.kcore.NickManager.Events.PlayerSendMessageEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityTracker;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class NickManager {

	@Getter
	private HashMap<Player,String> name = new HashMap<>();
	@Getter
	private PermissionManager pManager;
	@Getter
	private CommandHandler cmd;
	
	public String[] nick = new String[]{"king","Exteme","Steve","Buddy","Flex","Apex","Captain","Tim"
			,"Gigga","AdamHD","Jesus","xgen","BTW","Robin","checker","dc","ingo","Style"
			,"Jonny","leon","Manii","Ginkis","eco","ungadunga","John","Samir","Pika","fredwa",
			"Dox","Dove","Ole","Crypt","Bro","zocker","jman","coder","win"};
	
	public NickManager(CommandHandler cmd,PermissionManager pManager){
		this.cmd=cmd;
		this.pManager=pManager;
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(pManager.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_ENTITY_SPAWN){
			 @Override
			public void onPacketSending(PacketEvent event) {
	        if(event.getPacketType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN){
	            	try {
		                PacketContainer packet = event.getPacket();
		                boolean b = false;
		                WrapperPlayServerNamedEntitySpawn t = new WrapperPlayServerNamedEntitySpawn(packet);
		                for(Player p : UtilServer.getPlayers()){
		                	if(p.getName().equalsIgnoreCase(t.getPlayerName())){
		                		b=true;
		                		continue;
		                	}
		                }

		                if(!b)return;
		                
		                for(Player p : name.keySet()){
		                	if(p.getName().equalsIgnoreCase(t.getPlayerName())){
				                WrapperPlayServerNamedEntitySpawn p20 = new WrapperPlayServerNamedEntitySpawn(p);
				                p20.setPlayerName(name.get(p));
				                event.setPacket(p20.getHandle());
				                break;
		                	}
		                }
		            } catch (Exception e){
		            	System.err.print("[NickManager] Error: "+e.getMessage());
		            }
	            }
	    }
	});
		
		cmd.register(CommandNick.class, new CommandNick(this,pManager));
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
		name.put(player, nick);

		 EntityHuman eh = ((CraftPlayer)player).getHandle();
         PacketPlayOutEntityDestroy p29 = new PacketPlayOutEntityDestroy(new int[]{player.getEntityId()});
         PacketPlayOutNamedEntitySpawn p20 = new PacketPlayOutNamedEntitySpawn(eh);
         try {
             java.lang.reflect.Field profileField = p20.getClass().getDeclaredField("b");
             profileField.setAccessible(true);
             profileField.set(p20, new GameProfile( player.getUniqueId(),nick));
         } catch (Exception e) {
            
         }
         for(Player o : Bukkit.getOnlinePlayers()){
             if(!o.getName().equals(player.getName())){
                 ((CraftPlayer)o).getHandle().playerConnection.sendPacket(p29);
                 ((CraftPlayer)o).getHandle().playerConnection.sendPacket(p20);
             }
         }
		player.setDisplayName(nick);
		if(getPManager().isSetAllowTab()){
			UtilPlayer.setPlayerListName(player, "§e"+nick);
		}else{
			UtilPlayer.setPlayerListName(player, nick);
		}
		return nick;
	}
	
	public void delNick(Player player){
		if(name.containsKey(player))name.remove(player);
		player.setCustomName(player.getName());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void PlayerListName(PlayerListNameChangeEvent ev){
		if(name.containsKey(ev.getPlayer()))ev.setNick(ev.getNick().replaceAll(ev.getPlayer().getName(), name.get(ev.getPlayer())));
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void AsyncChat(AsyncPlayerChatEvent ev){
		if(name.containsKey(ev.getPlayer()))ev.setMessage(ev.getMessage().replaceAll(ev.getPlayer().getName(), name.get(ev.getPlayer())));
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void SendMessage(PlayerSendMessageEvent ev){
		for(Player player : name.keySet()){
			if(ev.getMessage().equalsIgnoreCase(player.getName())){
				ev.setMessage(ev.getMessage().replaceAll(player.getName(), name.get(player)));
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void BroadcastMessage(BroadcastMessageEvent ev){
		for(Player player : name.keySet()){
			if(ev.getMessage().equalsIgnoreCase(player.getName())){
				ev.setMessage(ev.getMessage().replaceAll(player.getName(), name.get(player)));
			}
		}
	}
	
}
