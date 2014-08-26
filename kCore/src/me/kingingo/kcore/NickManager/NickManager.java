package me.kingingo.kcore.NickManager;

import java.util.HashMap;

import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.NickManager.Command.CommandNick;
import me.kingingo.kcore.NickManager.Events.BroadcastMessageEvent;
import me.kingingo.kcore.NickManager.Events.PlayerSendMessageEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class NickManager {

	public HashMap<Player,String> name = new HashMap<>();
	public String[] nick = new String[]{"king","Exteme","Steve","Buddy","Flex","Apex","Captain","Tim"
			,"Gigga","AdamHD","Jesus","xgen","BTW","Robin","checker","dc","ingo","Style"
			,"Jonny","leon","Manii","Ginkis","eco","ungadunga","John","Samir","Pika","fredwa",
			"Dox","Dove","Ole","Crypt","Bro"};
	
	public NickManager(CommandHandler cmd,PermissionManager pManager){
		cmd.register(CommandNick.class, new CommandNick(this,pManager));
	}
	
	public String setNick(Player player){
		if(name.containsKey(player))name.remove(player);
		String n=RandomNick();
		name.put(player, n);
		return n;
	}
	
	public String RandomNick(){
		String n = nick[UtilMath.r(nick.length)];
		int len = UtilMath.RandomInt(3, 1);
		for(int i=1; i<len; i++){
			n=n+UtilMath.r(9);
		}
		return n;
	}
	
	public void setNick(Player player,String nick){
		if(name.containsKey(player))name.remove(player);
		name.put(player, nick);
	}
	
	public void delNick(Player player){
		if(name.containsKey(player))name.remove(player);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void AsyncChat(AsyncPlayerChatEvent ev){
		if(name.containsKey(ev.getPlayer()))ev.setMessage(ev.getMessage().replaceAll(ev.getPlayer().getName(), name.get(ev.getPlayer())));
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void SendMessage(PlayerSendMessageEvent ev){
		for(Player player : name.keySet()){
			if(ev.getMessage().equalsIgnoreCase(player.getName())){
				ev.setMessage(ev.getMessage().replaceAll(player.getName(), name.get(player)));
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void BroadcastMessage(BroadcastMessageEvent ev){
		for(Player player : name.keySet()){
			if(ev.getMessage().equalsIgnoreCase(player.getName())){
				ev.setMessage(ev.getMessage().replaceAll(player.getName(), name.get(player)));
			}
		}
	}
	
}
