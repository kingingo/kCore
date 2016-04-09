package eu.epicpvp.kcore.Translation;

import java.util.HashMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.player.LanguageType;
import dev.wolveringer.dataserver.player.Setting;
import dev.wolveringer.dataserver.protocoll.packets.PacketInChangePlayerSettings;
import dev.wolveringer.dataserver.protocoll.packets.PacketOutPlayerSettings;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class TranslationManager extends kListener {
	@Getter
	private static TranslationManager instance;

	public static void init(JavaPlugin instance) {
		TranslationManager.instance = new TranslationManager(instance);
		TranslationManager.instance.loadTranslations();
	}
	public static void changeLanguage(Player player,LanguageType language){
		getInstance().changeLanguage0(player, language);
	}
	
	public static void sendText(Player player, String name, Object... input){
		getInstance().sendText0(player, name, input);
	}
	
	public static LanguageType getLanguage(Player player){
		return getInstance().getLanguage0(player);
	}
	
	public static String getText(String name,Object... input){
		return getInstance().getText0(name, input);
	}
	
	public static String getText(String name){
		return getInstance().getText0(name);
	}
	
	public static String getText(Player player,String name,Object...input){
		return getInstance().getText0(player, name, input);
	}
	
	public static String getText(Player player,String name){
		return getInstance().getText0(player, name);
	}
	
	public static String getText(LanguageType language,String name,Object... input){
		return getInstance().getText0(language, name, input);
	}

	@Getter
	private HashMap<LanguageType, Translation> translations;
	@Getter
	private HashMap<UUID, LanguageType> players;
	@Getter
	private JavaPlugin pluginInstance;
	@Getter
	private DocumentBuilderFactory factory;
	@Getter
	private DocumentBuilder builder;

	public TranslationManager(JavaPlugin plugin) {
		super(plugin, "TranslationManager");
		this.pluginInstance = plugin;
		this.factory = DocumentBuilderFactory.newInstance();
		this.translations=new HashMap<>();
		this.players=new HashMap<>();
		
		try{
			this.builder=this.factory.newDocumentBuilder();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadTranslations(){
		for (LanguageType language : LanguageType.values()) {
			Translation trans = new Translation(language);
			if (trans.load()) {
				translations.put(language, trans);
			}
		}
		
		Translation source = translations.get(LanguageType.ENGLISH);
		if(source != null){
			for(Translation trans : translations.values()){
				if(trans.getLanguage() != source.getLanguage()){
					trans.calculateDone(source);
				}
			}
		}
	}

	public void changeLanguage0(Player player, LanguageType language) {
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		UtilServer.getClient().writePacket(new PacketInChangePlayerSettings(loadedplayer.getUUID(), Setting.LANGUAGE, language.getShortName()));
		players.put(loadedplayer.getUUID(), language);
	}

	public void sendText0(Player player, String name, Object... input) {
		if (player == null) {
			new NullPointerException("player == NULL").printStackTrace();
			return;
		}
		player.sendMessage(getText(player, name, input));
	}

	public LanguageType getLanguage0(Player player) {
		if (players.containsKey(player.getUniqueId())) {
			return players.get(player.getUniqueId());
		} else {
			new NullPointerException("language == NULL").printStackTrace();
			return LanguageType.ENGLISH;
		}
	}

	public String getText0(String name, Object... input) {
		return getText0(LanguageType.ENGLISH, name, input);
	}

	public String getText0(String name) {
		return getText0(LanguageType.ENGLISH, name);
	}

	public String getText0(Player player, String name, Object... input) {
		return getText0(getLanguage(player), name, input);
	}

	public String getText0(Player player, String name) {
		return getText0(getLanguage(player), name);
	}

	public String getText0(LanguageType language, String name, Object... input) {
		if(translations.containsKey(language)){
			String message = translations.get(language).get(name, input);
			if (message == null) {
				new NullPointerException("Message '" + name + "' for language " + language.getShortName() + " not found!").printStackTrace();
				return getText0(language, "MSG_NOT_FOUND", name); //TODO try english translation!
			}

			return message;
		}else{
			new NullPointerException("Language "+language.getShortName()+"/"+name+" not found?!").printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void a(AsyncPlayerPreLoginEvent e) {
		final LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(e.getName());
		PacketOutPlayerSettings.SettingValue[] obj = loadedplayer.getSettings(Setting.LANGUAGE).getSync();
				
		LanguageType type;
		if (obj == null || obj.length == 0 || obj[0].getSetting() != Setting.LANGUAGE) {
			System.out.println("Getlanguage for player " + e.getName() + " is null!");
			type = LanguageType.ENGLISH;
		} else
			type = LanguageType.getLanguageFromName(obj[0].getValue());
				
				
		System.out.println("LA: "+type.getShortName() +" "+loadedplayer.getUUID());
		players.put(loadedplayer.getUUID(), type);
	}

	@EventHandler
	public void a(PlayerQuitEvent e) { //Remove after leave. Its possiable that the player change his translation
		players.remove(e.getPlayer().getUniqueId());
	}
}
