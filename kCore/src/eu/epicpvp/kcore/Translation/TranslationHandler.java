package eu.epicpvp.kcore.Translation;

import java.io.File;

import org.bukkit.entity.Player;

import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.player.LanguageType;
import dev.wolveringer.dataserver.player.Setting;
import dev.wolveringer.dataserver.protocoll.packets.PacketInChangePlayerSettings;
import dev.wolveringer.translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;

public class TranslationHandler{
	@Getter
	@Setter
	private static TranslationManager instance;
	
	public static void changeLanguage(Player player,LanguageType language){
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		UtilServer.getClient().writePacket(new PacketInChangePlayerSettings(loadedplayer.getPlayerId(), Setting.LANGUAGE, language.getShortName()));
		getInstance().updateLanguage(loadedplayer);
	}
	
	public static void sendText(Player player, String name, Object... input){
		getInstance().translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()), input);
	}
	
	public static LanguageType getLanguage(Player player){
		return getInstance().getLanguage(UtilServer.getClient().getPlayerAndLoad(player.getName()));
	}
	
	public static String getText(String name,Object... input){
		return getInstance().translate(name, input);
	}
	
	public static String getText(String name){
		return getInstance().translate(name);
	}
	
	public static String getText(Player player,String name,Object...input){
		return getInstance().translate( name,UtilServer.getClient().getPlayerAndLoad(player.getName()), input);
	}
	
	public static String getText(Player player,String name){
		return getInstance().translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()));
	}
	
	public static String getText(LanguageType language,String name,Object... input){
		return getInstance().translate(name, language, input);
	}
}
