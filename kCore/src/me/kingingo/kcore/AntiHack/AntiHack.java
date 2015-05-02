package me.kingingo.kcore.AntiHack;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.kingingo.kcore.AntiHack.Events.AntiHackPlayerDetectedEvent;
import me.kingingo.kcore.AntiHack.Hack.Forcefield;
import me.kingingo.kcore.Listener.kListener;

public class AntiHack extends kListener{

	@Getter
	private JavaPlugin instance;
	private ArrayList<IHack> list = new ArrayList<>();
	
	public AntiHack(JavaPlugin instance) {
		super(instance, "AntiHack");
		this.instance=instance;
		
		list.add(new Forcefield(this, 5));
	}
	
	@EventHandler
	public void Detected(AntiHackPlayerDetectedEvent ev){
		
	}

}
