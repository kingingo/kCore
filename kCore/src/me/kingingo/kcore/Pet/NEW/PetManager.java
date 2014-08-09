package me.kingingo.kcore.Pet.NEW;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Pet.NEW.Pet.PetLiving;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PetManager implements Listener {

	@Getter
	JavaPlugin instance;
	@Getter
	HashMap<Player,PetLiving> pets = new HashMap<>();
	
	public PetManager(JavaPlugin plugin ){
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void Move(UpdateEvent ev){
		if(UpdateType.FAST!=ev.getType())return;
	}
	
}
