package eu.epicpvp.kcore.Listener.BungeeCordFirewall;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketOutServerStatus;
import dev.wolveringer.dataserver.protocoll.packets.PacketOutServerStatus.Action;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Command.Admin.CommandFirewall;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class BungeeCordFirewallListener extends kListener{

	@Getter
	private boolean firewall = false; // TO TRUE!
	private ArrayList<String> bungeecord_ips;
	
	public BungeeCordFirewallListener(CommandHandler cmd) {
		super(cmd.getPlugin(), "BungeeCordFirewallListener");
		this.bungeecord_ips=new ArrayList<>();
		cmd.register(CommandFirewall.class, new CommandFirewall(this));
		loadBG();
	}
	
	public void setFirewall(boolean firewall){
		this.firewall=firewall;
		UtilServer.getClient().sendServerMessage(ClientType.ALL, "BungeeFirewall", new DataBuffer().writeInt(1).writeBoolean(firewall));
	}
	
	public void loadBG(){
		bungeecord_ips.clear();
		
		UtilServer.getClient().getServerStatus(Action.BUNGEECORD, null).getAsync(new Callback<PacketOutServerStatus>() {
			
			@Override
			public void call(PacketOutServerStatus packet) {
				
			}
		});
	}
	
	@EventHandler
	public void message(ServerMessageEvent ev){
		if(ev.getChannel().equalsIgnoreCase("BungeeFirewall")){
			switch(ev.getBuffer().readInt()){
			case 0:
				loadBG();
				break;
			case 1:
				firewall=ev.getBuffer().readBoolean();
				break;
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void preLogin(PlayerJoinEvent ev){
		if(firewall)
			if(!this.bungeecord_ips.contains(ev.getPlayer().spigot().getRawAddress().getAddress().getHostAddress()))
				ev.getPlayer().kickPlayer("§cPlease use the EpicPvP Proxy!");
		
	}

}
