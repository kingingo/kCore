package me.kingingo.kcore.TeleportManager;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Teleport.command.CommandTpa;
import me.kingingo.kcore.Teleport.command.CommandTpaccept;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class TeleportManager extends kListener{

	@Getter
	private PermissionManager permManager;
	@Getter
	private CommandHandler cmd;
	@Getter
	private ArrayList<Teleporter> teleport = new ArrayList<>();
	@Getter
	private HashMap<Player,Player> teleport_anfrage = new HashMap<>();
	@Getter
	private int sec;
	
	public TeleportManager(CommandHandler cmd,PermissionManager permManager,int sec){
		super(permManager.getInstance(),"TeleportManager");
		this.permManager=permManager;
		this.cmd=cmd;
		this.sec=sec;
		
		cmd.register(CommandTpa.class, new CommandTpa(this));
		cmd.register(CommandTpaccept.class, new CommandTpaccept(this));
	}
	
	Teleporter tp;
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FASTEST)return;
		for(int i = 0; i < teleport.size(); i++){
			tp=((Teleporter)teleport.get(i));
			if(tp.TeleportDo()){
				teleport.remove(tp);
			}
		}
	}
	
	
	
}
