package eu.epicpvp.kcore.Translation;

import org.bukkit.entity.Player;

import eu.epicpvp.dataserver.protocoll.packets.PacketInChangePlayerSettings;
import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.datenclient.translation.TranslationManager;
import eu.epicpvp.datenserver.definitions.dataserver.player.LanguageType;
import eu.epicpvp.datenserver.definitions.dataserver.player.Setting;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TranslationHandler {
	@Getter
	@Setter
	private static TranslationManager instance;

	public static void changeLanguage(Player player, LanguageType language) {
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		UtilServer.getClient().writePacket(new PacketInChangePlayerSettings(loadedplayer.getPlayerId(),
				Setting.LANGUAGE, language.getShortName()));
		instance.updateLanguage(loadedplayer);
	}

	public static void sendText(Player player, String name, Object... input) {
		instance.translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()), input);
	}

	public static LanguageType getLanguage(Player player) {
		return instance.getLanguage(UtilServer.getClient().getPlayerAndLoad(player.getName()));
	}

	public static void registerFallback(LanguageType type,String key,String message){
		instance.getTranslationFile(type).registerFallbackMessage(key, message);
	}

	public static String getText(String name, Object... input) {
		try {
			return instance.translate(name, input);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getText(String name) {
		try {
			return instance.translate(name);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getText(Player player, String name, Object... input) {
		try {
			return instance.translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()), input);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getText(Player player, String name) {
		try {
			return instance.translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()));
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getText(LanguageType language, String name, Object... input) {
		try {
			return instance.translate(name, language, input);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getPrefixAndText(Player player, String name, Object... input) {
		try {
			LoadedPlayer loadedPlayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
			return instance.translate("PREFIX", loadedPlayer) + instance.translate(name, loadedPlayer, input);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getPrefixAndText(Player player, String name) {
		try {
			LoadedPlayer loadedPlayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
			return instance.translate("PREFIX", loadedPlayer) + instance.translate(name, loadedPlayer);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getPrefixAndText(LanguageType language, String name, Object... input) {
		try {
			return instance.translate("PREFIX", language) + instance.translate(name, language, input);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "";
		}
	}
}
