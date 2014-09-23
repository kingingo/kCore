package me.kingingo.kcore.Addons;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class AddonRealTime extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private World world;
	
	public AddonRealTime(JavaPlugin instance,World world){
		super(instance,"[AddonRealTime]");
		this.instance=instance;
		this.world=world;
	}
	
	@EventHandler
	public void RealTime(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		getWorld().setFullTime(getTime());
		getWorld().setStorm(false);
	}
	
	SimpleDateFormat ft;
	public long getTime(){
		ft=new SimpleDateFormat ("hh");
		switch(Integer.valueOf(ft.format(new Date()))){
		case 0:return 18000;
		case 1: return 19000;
		case 2: return 20000;
		case 3: return 21000;
		case 4: return 22000;
		case 5: return 23000;
		case 6: return 24000;
		case 7: return 1000;
		case 8: return 2000;
		case 9: return 3000;
		case 10: return 4000;
		case 11: return 5000;
		case 12: return 6000;
		case 13: return 7000;
		case 14: return 8000;
		case 15: return 9000;
		case 16: return 10000;
		case 17: return 11000;
		case 18: return 12000;
		case 19: return 13000;
		case 20: return 14000;
		case 21: return 15000;
		case 22: return 16000;
		case 23: return 17000;
		default:return 1000;
		}
	}
	
}
