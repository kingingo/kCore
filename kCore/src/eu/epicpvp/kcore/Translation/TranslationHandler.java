package eu.epicpvp.kcore.Translation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.player.LanguageType;
import dev.wolveringer.dataserver.player.Setting;
import dev.wolveringer.dataserver.protocoll.packets.PacketInChangePlayerSettings;
import dev.wolveringer.translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TranslationHandler {
	@Getter
	@Setter
	private static TranslationManager instance;

	public static void changeLanguage(Player player, LanguageType language) {
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		UtilServer.getClient().writePacket(new PacketInChangePlayerSettings(loadedplayer.getPlayerId(), Setting.LANGUAGE, language.getShortName()));
		instance.updateLanguage(loadedplayer);
	}

	public static void sendText(Player player, String name, Object... input) {
		instance.translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()), input);
	}

	public static LanguageType getLanguage(Player player) {
		return instance.getLanguage(UtilServer.getClient().getPlayerAndLoad(player.getName()));
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
		return instance.translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()), input);
	}
	
	public static String getText(Player player, String name) {
		return instance.translate(name, UtilServer.getClient().getPlayerAndLoad(player.getName()));
	}

	public static String getText(LanguageType language, String name, Object... input) {
		return instance.translate(name, language, input);
	}

	public static String getPrefixAndText(Player player, String name, Object... input) {
		LoadedPlayer loadedPlayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		return instance.translate("PREFIX", loadedPlayer) + instance.translate(name, loadedPlayer, input);
	}

	public static String getPrefixAndText(Player player, String name) {
		LoadedPlayer loadedPlayer = UtilServer.getClient().getPlayerAndLoad(player.getName());
		return instance.translate("PREFIX", loadedPlayer) + instance.translate(name, loadedPlayer);
	}

	public static String getPrefixAndText(LanguageType language, String name, Object... input) {
		return instance.translate("PREFIX", language) + instance.translate(name, language, input);
	}
}
