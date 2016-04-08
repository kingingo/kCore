package eu.epicpvp.kcore.Translation;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;

public class TranslationManager {
	
	@Getter
	public static DocumentBuilderFactory factory;
	@Getter
	public static DocumentBuilder builder;
	@Getter
	@Setter
	public static JavaPlugin instance;
	@Getter
	@Setter
	public static HashMap<Language,Translation> translations;
	@Getter
	@Setter
	public static HashMap<Player,Language> language;
	
	static{
		factory = DocumentBuilderFactory.newInstance();
		language = new HashMap<>();
		translations = new HashMap<>();
		
		try {
			builder = getFactory().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void init(JavaPlugin instance){
		setInstance(instance);
		
		for(Language language : Language.values()){
			Translation trans = new Translation(language);
			
			if(trans.load()){
				translations.put(language, trans);
			}
		}
	}
	
//	public static Language getLanguageType(Player player){
//		
//	}
	
}
