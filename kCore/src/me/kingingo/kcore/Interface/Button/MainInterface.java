package me.kingingo.kcore.Interface.Button;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Interface.DeathGamesInterface;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryLoad;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.SERVER_READY;
import me.kingingo.kcore.Packet.Packets.SERVER_SETTINGS;
import me.kingingo.kcore.Util.UtilBG;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MainInterface {

	@Getter
	private JavaPlugin instance;
	@Getter
	private InventoryPageBase main_page;
	@Getter
	private InventoryLoad load;
	@Getter
	private PacketManager packetManager;
	@Getter
	private ArrayList<String> wait_list = new ArrayList<>();
	@Getter
	private String server;
	
	public MainInterface(JavaPlugin instance,String server,PacketManager packetManager){
		this.instance=instance;
		this.server=server;
		this.packetManager=packetManager;
		this.main_page= new InventoryPageBase(9, "Main:");
		this.load=new InventoryLoad(getInstance(), "Server wird Eingestellt...");
		
		new DeathGamesInterface(this);
	}
	
	public void start(Player player,SERVER_SETTINGS packet){
		player.openInventory(load);
		wait_list.add(player.getName());
		getPacketManager().SendPacket("DATA", packet);
	}
	
	@EventHandler
	public void Receive(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof SERVER_READY){
			SERVER_READY packet = (SERVER_READY)ev.getPacket();
			if(wait_list.contains(packet.getPlayer())){
				if(UtilPlayer.isOnline(packet.getPlayer())){
					UtilBG.sendToServer(Bukkit.getPlayer(packet.getPlayer()), packet.getServer(), getInstance());
				}else{
					wait_list.remove(packet.getPlayer());
					getPacketManager().SendPacket(packet.getServer(), new SERVER_READY());
				}
			}else{
				getPacketManager().SendPacket(packet.getServer(), new SERVER_READY());
			}
		}
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(wait_list.contains(ev.getPlayer().getName()))wait_list.remove(ev.getPlayer().getName());
	}
	
}
