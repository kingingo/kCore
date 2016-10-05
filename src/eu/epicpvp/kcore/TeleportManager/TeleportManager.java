package eu.epicpvp.kcore.TeleportManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Command.Commands.CommandReTpa;
import eu.epicpvp.kcore.Command.Commands.CommandTpa;
import eu.epicpvp.kcore.Command.Commands.CommandTpaHere;
import eu.epicpvp.kcore.Command.Commands.CommandTpaccept;
import eu.epicpvp.kcore.Command.Commands.CommandTpdeny;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class TeleportManager extends kListener{

	@Getter
	private PermissionManager permManager;
	@Getter
	private CommandHandler cmd;
	@Getter
	private ArrayList<Teleporter> teleport = new ArrayList<>();
	@Getter
	private HashMap<Player,Teleporter> teleport_anfrage = new HashMap<>();
	@Getter
	private TeleportCheck check;
	
	public TeleportManager(CommandHandler cmd,PermissionManager permManager){
		this(cmd,permManager,TeleportCheck.TIME);
	}
	
	public TeleportManager(CommandHandler cmd,PermissionManager permManager,TeleportCheck check){
		super(permManager.getInstance(),"TeleportManager");
		this.check=check;
		this.permManager=permManager;
		this.cmd=cmd;
		
		cmd.register(CommandTpa.class, new CommandTpa(this));
		cmd.register(CommandTpaHere.class, new CommandTpaHere(this));
		cmd.register(CommandTpaccept.class, new CommandTpaccept(this));
		cmd.register(CommandReTpa.class, new CommandReTpa(this));
		cmd.register(CommandTpdeny.class, new CommandTpdeny(this));
		UtilServer.setTeleportManager(this);
	}
	
	public boolean near(Player plr){
		Collection<Entity> nearbyEntities = plr.getWorld().getNearbyEntities( plr.getLocation(), 25, 25, 25 );
		for ( Entity e : nearbyEntities ) {
			if ( e instanceof Player && e.getUniqueId() != plr.getUniqueId() ) {
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		Iterator<Teleporter> iterator = teleport.iterator();
		while (iterator.hasNext()) {
			Teleporter tp = iterator.next();
			if(tp.TeleportDo()){
				iterator.remove();
			}
		}
	}
	
	
	
}
