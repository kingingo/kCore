package eu.epicpvp.kcore.Translation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Util.UtilServer;

public class TranslationManager {
	
	public static TranslationChannelHandler handler;
	
	public static void init(JavaPlugin instance){
		handler = new TranslationChannelHandler(instance);
		Bukkit.getMessenger().registerIncomingPluginChannel(instance, "language", handler);
		Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "language");
		
		for(Language language : Language.values()){
			Translation trans = new Translation(language);
			if(trans.load()){
				handler.getTranslations().put(language, trans);
				handler.Log("use "+language.name()+" ("+trans.getVersion()+") translation!");
			}
		}
	}
	
	public static void changeLanguage(Player player,Language language){
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		//TO DO !
	}
	
	public static void sendText(Player player, String name, Object... input){
		if(player==null){
			new NullPointerException("player == NULL");
			return;
		}
		player.sendMessage( getText(player, name, input) );
	}
	
	public static Language getLanguage(Player player){
		if(handler.getLanguage().containsKey(player)){
			return handler.getLanguage().get(player);
		}else{
			new NullPointerException("language == NULL");
			return Language.ENGLISH;
		}
	}
	
	public static String getText(String name,Object... input){
		return getText(Language.ENGLISH, name, input);
	}
	
	public static String getText(String name){
		return getText(Language.ENGLISH, name, null);
	}
	
	public static String getText(Player player,String name,Object...input){
		return getText(getLanguage(player), name, input);
	}
	
	public static String getText(Player player,String name){
		return getText(getLanguage(player), name, null);
	}
	
	public static String getText(Language language,String name,Object... input){
		String message = handler.getTranslations().get(language).get(name, input);
		if(message==null){
			new NullPointerException("Message not found? "+name);
			return getText(language, "MSG_NOT_FOUND", name);
		}
		
		return message;
	}
}
