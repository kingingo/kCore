package eu.epicpvp.kcore.Listener.BungeeCordFirewall;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import eu.epicpvp.datenserver.definitions.connection.ClientType;
import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Command.Admin.CommandFirewall;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class BungeeCordFirewallListener extends kListener{

	@Getter
	private boolean firewall = false; // TO TRUE!

	public BungeeCordFirewallListener(CommandHandler cmd) {
		super(cmd.getPlugin(), "BungeeCordFirewallListener");
		cmd.register(CommandFirewall.class, new CommandFirewall(this));
	}

	public void setFirewall(boolean firewall){
		this.firewall=firewall;
		UtilServer.getClient().sendServerMessage(ClientType.ALL, "BungeeFirewall", new DataBuffer().writeInt(1).writeBoolean(firewall));
	}

	@EventHandler
	public void message(ServerMessageEvent ev){
		if(ev.getChannel().equalsIgnoreCase("BungeeFirewall")){
			this.firewall=ev.getBuffer().readBoolean();
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void preLogin(AsyncPlayerPreLoginEvent ev){
		if(isFirewall() && UtilServer.getClient().getPlayerAndLoad(ev.getName()).isOnlineSync()){
			ev.disallow(Result.KICK_OTHER, "§cPlease use the ClashMC Server Proxies!");
		}
	}

}
