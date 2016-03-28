package eu.epicpvp.kcore.Language.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Language.LanguageType;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilList;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class LanguageListener extends kListener{

	public LanguageListener(JavaPlugin instance) {
		super(instance, "LanguageListener");
	}
	
	String def;
	@EventHandler(priority=EventPriority.LOW)
	public void Join(AsyncPlayerPreLoginEvent ev){
		def=Language.getMysql().getString("SELECT language FROM language_user WHERE uuid='"+UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())+"'");
		if(!def.equalsIgnoreCase("null")){
			if(!Language.getList().containsKey(LanguageType.get(def)))Language.loadLanguage(LanguageType.get(def));
			Language.getLanguages().put(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()), LanguageType.get(def));
		}else{
			Language.getMysql().Update("INSERT INTO language_user (uuid,language) VALUES ('"+UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())+"','"+LanguageType.ENGLISH.getDef()+"');");
			Language.getLanguages().put(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()), LanguageType.ENGLISH);
		}
	}

	@EventHandler
	public void clear(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_08){
			UtilList.CleanList(Language.getLanguages());
		}
	}
}
