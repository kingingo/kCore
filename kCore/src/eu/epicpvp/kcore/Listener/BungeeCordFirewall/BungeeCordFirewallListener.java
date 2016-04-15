package eu.epicpvp.kcore.Listener.BungeeCordFirewall;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Command.Admin.CommandFirewall;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import lombok.Getter;
import lombok.Setter;

public class BungeeCordFirewallListener extends kListener{

	@Getter
	@Setter
	private boolean firewall = false; // TO TRUE!
	@Getter
	private JavaPlugin instance; // TO TRUE!
	private ArrayList<String> bungeecord_ips;
	
	public BungeeCordFirewallListener(JavaPlugin instance,CommandHandler cmd) {
		super(instance, "BungeeCordFirewallListener");
		this.bungeecord_ips=new ArrayList<>();
		this.instance=instance;
		cmd.register(CommandFirewall.class, new CommandFirewall(this));
		
		loadBG();
	}
	
	public void loadBG(){
		bungeecord_ips.clear();
		//TODO
	}
	
	@EventHandler
	public void message(ServerMessageEvent ev){
		if(ev.getChannel().equalsIgnoreCase("BungeeFirewall")){
			loadBG();
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void preLogin(PlayerJoinEvent ev){
		if(!firewall)return;
		if(!this.bungeecord_ips.contains(ev.getPlayer().spigot().getRawAddress().getAddress().getHostAddress())){
			ev.getPlayer().kickPlayer("§cPlease use the EpicPvP Proxy!");
		}
	}

}
