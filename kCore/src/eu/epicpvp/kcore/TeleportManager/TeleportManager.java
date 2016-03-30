package eu.epicpvp.kcore.TeleportManager;

import java.util.ArrayList;
import java.util.HashMap;

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
	private int sec;
	
	public TeleportManager(CommandHandler cmd,PermissionManager permManager,int sec){
		super(permManager.getInstance(),"TeleportManager");
		this.permManager=permManager;
		this.cmd=cmd;
		this.sec=sec;
		
		cmd.register(CommandTpa.class, new CommandTpa(this));
		cmd.register(CommandTpaHere.class, new CommandTpaHere(this));
		cmd.register(CommandTpaccept.class, new CommandTpaccept(this));
		cmd.register(CommandReTpa.class, new CommandReTpa(this));
		cmd.register(CommandTpdeny.class, new CommandTpdeny(this));
	}
	
	Teleporter tp;
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		for(int i = 0; i < teleport.size(); i++){
			tp=((Teleporter)teleport.get(i));
			if(tp.TeleportDo()){
				teleport.remove(tp);
			}
		}
	}
	
	
	
}
