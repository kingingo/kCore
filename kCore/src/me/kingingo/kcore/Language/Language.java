package me.kingingo.kcore.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Events.ServerEnableEvent;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Language {
	
	@Getter
	private static HashMap<LanguageType,HashMap<String,String>> list;
	
	public static void load(MySQL mysql){
		list=new HashMap<>();
		mysql.Update("CREATE TABLE IF NOT EXISTS language(type varchar(30),name varchar(30),msg varchar(30))");
		try{				
			ResultSet rs = mysql.Query("SELECT type,name,msg FROM language");
			 while (rs.next()){
				 if(LanguageType.get(rs.getString(1))==null)continue;
				 if(!list.containsKey(LanguageType.get(rs.getString(1))))list.put(LanguageType.get(rs.getString(1)), new HashMap<String,String>());
				 list.get(LanguageType.get(rs.getString(1))).put(rs.getString(2), rs.getString(3));
			 }
			 rs.close();
		}catch (SQLException e){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
		}
		addALL(mysql);
	}

	public static void sendText(Player player,String name,Object[] input){
		player.sendMessage( toText(list.get(LanguageType.GERMANY).get(name), input) );
	}
	
	public static void sendText(Player player,String name,Object input){
		player.sendMessage( toText(list.get(LanguageType.GERMANY).get(name), input) );
	}
	
	public static String getText(String name,Object input){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Messgae nicht gefunden "+name);
			return "";
		}
		return toText(list.get(LanguageType.GERMANY).get(name), input);
	}
	
	public static String getText(String name,Object[] input){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Messgae nicht gefunden "+name);
			return "";
		}
		return toText(list.get(LanguageType.GERMANY).get(name), input);
	}
	
	public static String getText(String name){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Messgae nicht gefunden "+name);
			return "";
		}
		return list.get(LanguageType.GERMANY).get(name);
	}
	
	public static String getText(Player player,String name){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Messgae nicht gefunden "+name);
			return "";
		}
		return list.get(LanguageType.GERMANY).get(name);
	}
	
	public static String getText(Player player,String name,Object[] input){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Messgae nicht gefunden "+name);
			return "";
		}
		return toText(list.get(LanguageType.GERMANY).get(name), input);
	}
	
	public static String getText(Player player,String name,Object input){
		if(!list.get(LanguageType.GERMANY).containsKey(name)){
			System.out.println("[Language] Messgae nicht gefunden "+name);
			return "";
		}
		return toText(list.get(LanguageType.GERMANY).get(name), input);
	}
	
	public static String toText(String msg,Object input){
		return (msg.contains("{INPUT0}") ? msg.replaceAll("\\{INPUT0\\}", String.valueOf(input)) : msg);
	}
	
	public static String toText(String msg,Object[] input){
		for(int i = 0 ; i < input.length ; i++){
			msg=msg.replaceFirst("\\{INPUT"+i+"\\}", String.valueOf(input[i]));
		}
		return msg;
	}
	
	public static void addALL(MySQL mysql){
		if(!list.containsKey(LanguageType.GERMANY))list.put(LanguageType.GERMANY, new HashMap<String,String>());
		add(mysql, "TREASURE_CHEST_TIME_AWAY", "§cDie Zeit ist abgelaufen.");
		add(mysql, "VERSUS_ADDED", "Du wurdest auf die Warteliste hinzugefuegt!");
		add(mysql, "VERSUS_PLACE", "Du befindest dich auf PLatz §b{INPUT0}");
		add(mysql, "TRACKING_RANGE", "§7Die Player Tracking Range ist nun §a{INPUT0}§7.");
		add(mysql, "NO_CHARAKTER", "Es sind ungueltige Zeichen im Namen!");
		add(mysql, "STATS_PREFIX", "§b■■■■■■■■§6 §lSTATS §b■■■■■■■■");
		add(mysql, "STATS_PREFIXBY", "§b■■■■■■■■ §6§lStats von {INPUT0} §b■■■■■■■■");
		add(mysql, "STATS_KILLS", "§6Kills : §7");
		add(mysql, "STATS_DEATHS", "§6Tode : §7");
		add(mysql, "STATS_MONEY", "§6Geld : §7");
		add(mysql, "STATS_KDR", "§6KDR : §7");
		add(mysql, "STATS_GILDE", "§6Gilde : §7");
		add(mysql, "STATS_RANKING", "§6Ranking : §7");
		add(mysql, "PREMIUM_PET", "§cKaufe dir §aPremium§c um diese Funktion nutzen zu koennen.");
		add(mysql, "TREASURE_CHEST_TOO_NEAR", "§bDu bist zu nah an einer anderen TreasureChest!");
		add(mysql, "CHAT_MESSAGE_BLOCK", "§cBitte unterlasse solche Woerter im Chat!");
		add(mysql, "SPIELER_ENTFERNT_COMPASS", "§7Der Spieler §b{INPUT0}§7 ist §b{INPUT1} §7Bloecke von dir enfternt!");
		add(mysql, "AUßERHALB_DER_MAP", "Du bist außerhalb der Map!");
		add(mysql, "COINS_DEL_PLAYER", "§aDen Spieler §b{INPUT0}§a wurden §b{INPUT1}§a entfernt");
		add(mysql, "COINS_ADD_PLAYER", "§aDen Spieler §b{INPUT0}§a wurden §b{INPUT1}§a hinzugefuegt.");
		add(mysql, "CMD_MUTE", "§aDie Commands wurden gesperrt!");
		add(mysql, "CMD_UNMUTE", "§cDie Commands wurde entsperrt!");
		add(mysql, "PVP_MUTE", "§cPvP wurde deaktiviert!");
		add(mysql, "PVP_UNMUTE", "§aPvP wurde aktiviert!");
		add(mysql, "CHAT_MUTE", "§aDer Chat wurde gesperrt!");
		add(mysql, "CHAT_UNMUTE", "§cDer Chat wurde entsperrt!");
		add(mysql, "MORE_HAND", "§aDu hast nun 64x.");
		add(mysql, "MORE_INV", "§aDeine Items im Inventory wurden auf 64x aufgerundet.");
		add(mysql, "ENDERGAMES_TELEPORT", "§7Du hast deine Position mit §b{INPUT0} §7gewechselt!");
		add(mysql, "KIT_SHOP_ADD", "§eDu hast das Kit §3{INPUT0} §egewaehlt.");
		add(mysql, "KIT_SHOP_NO_MONEY", "Du hast nicht genug §b{INPUT0}§e.");
		add(mysql, "KIT_SHOP_BUYED_KIT", "Du hast erfolgreich das Kit §3{INPUT0}§e gekauft.");
		add(mysql, "PLAYER_IS_OFFLINE", "Der Spieler {INPUT0} ist Offline.");
		add(mysql, "FIGHT_START_IN", "§eIhr koennt in §b{INPUT0}§e Sekunden Kaempfen!");
		add(mysql, "FIGHT_START", "§cIhr koennt nun Kaempfen!");
		add(mysql, "GAME_END_IN", "§eDas Spiel endet in §b{INPUT0}§e ");
		add(mysql, "GAME_END", "§cDas Spiel wurde beendet.");
		add(mysql, "RESTART_IN", "§4Der Server Restartet in §b{INPUT0}§4 Sekunden");
		add(mysql, "RESTART", "§4§lDer Server Restartet jetzt");
		add(mysql, "SERVER_FULL", "§cDer Server ist voll!");
		add(mysql, "SERVER_NOT_LOBBYPHASE", "§cDieser Server ist aktuell nicht in der Lobbyphase!");
		add(mysql, "SERVER_FULL_WITH_PREMIUM", "§cDer Server ist voll! Kaufe dir Premium auf §bshop.EpicPvP.de§c um auf volle Servern zu joinen");
		add(mysql, "KILL_BY", "§b{INPUT0} §ewurde von §a{INPUT1}§e getoetet");
		add(mysql, "DEATH", "§b{INPUT0}§e ist gestorben.");
		add(mysql, "GAME_EXCLUSION", "§c{INPUT0} wurde vom Spiel ausgeschlossen");
		add(mysql, "PERK_NOT_BOUGHT", "§cDu hast noch keine Perks gekauft! §bShop.EpicPvP.de");
		add(mysql, "VOTE_TEAM_ADD", "§cDu bist nun in §a{INPUT0}§c");
		add(mysql, "VOTE_TEAM_MIN_PLAYER", "Es muessen min. {INPUT0} Spieler online sein!");
		add(mysql, "VOTE_TEAM_REMOVE", "§cDu hast §a{INPUT0}§c verlassen");
		add(mysql, "VOTE_TEAM_FULL", "§a{INPUT0}§c ist voll");
		add(mysql, "VOTE_MIN", "§eEs muessen mindestens §b{INPUT0}§e Spieler online sein damit das Spiel starten kann");
		add(mysql, "KICKED_BY_PREMIUM", "§cDu wurdest vom Server gekickt damit ein Premium User spielen kann. Du moechtest auch? §bshop.EpicPvP.de");
		add(mysql, "PREFIX_GAME", "§7[§6{INPUT0}§7]: §7");
		add(mysql, "PREFIX", "§7[§6EpicPvP§7]: §7");
		add(mysql, "TEAM_WIN", "§eDas Team {INPUT0}§e hat das Spiel Gewonnen.");
		add(mysql, "TEAM_OUT", "§b{INPUT0}§c ist gefallen");
		add(mysql, "RESTART_FROM_ADMIN", "§cDer Server wurde von einem Administrator restartet!");
		add(mysql, "SCHUTZZEIT_END_IN", "§eDie Schutzzeit endet in §b{INPUT0}§e Sekunden");
		add(mysql, "SCHUTZZEIT_END", "§eDie Schutzzeit ist nun zuende, ihr koennt euch bekriegen");
		add(mysql, "GAME_START", "§eDas Spiel ist gestartet");
		add(mysql, "GAME_START_IN", "§eDas Spiel startet in §b{INPUT0}§e Sekunden");
		add(mysql, "DEATHMATCH_START", "§cDas Deathmatch startet");
		add(mysql, "DEATHMATCH_START_IN", "§cDas Deathmatch startet in §b{INPUT0}§c Sekunden");
		add(mysql, "DEATHMATCH_END", "§cDas Deathmatch ist zu ende.");
		add(mysql, "GAME_WIN", "§eDer Spieler §b{INPUT0}§e hat das Spiel gewonnen!");
		add(mysql, "SURVIVAL_GAMES_DISTRICT_WIN", "§eDas District §a{INPUT0}§e mit den Spielern §b{INPUT1}§e und §b{INPUT2}§e hat Gewonnen");
		add(mysql, "DEATHMATCH_END_IN", "§cDas Deathmatch endet in §b{INPUT0}§c Sekunden");
		add(mysql, "LOGIN_MESSAGE", "§cBitte logge dich ein: §b/Login <Password>");
		add(mysql, "KICK_BY_FALSE_PASSWORD", "§cDu hast §b{INPUT0}§c mal dein Password falsch eingegeben");
		add(mysql, "LOGIN_ACCEPT", "§aDu hast dich Erfolgreich eingeloggt");
		add(mysql, "LOGIN_DENY", "§cDas Password ist falsch");
		add(mysql, "REGISTER_ACCEPT", "§aRegister Erfolgreich!");
		add(mysql, "REGISTER_MESSAGE", "§cRegistriere dich bitte: §b/Register <Password>");
		add(mysql, "SPECTATOR_CHAT_CANCEL", "§cDu kannst als Spectator nicht im Chat schreiben.");
		add(mysql, "TTT_WIN", "§eDie §c§l{INPUT0}§e haben diese Runde gewonnen!");
		add(mysql, "TTT_DEATH", "§eDer Spieler §b{INPUT0}§e ist gestorben und war §c{INPUT1}.");
		add(mysql, "TTT_TESTER_WAS_USED", "§eDer Tester ist im moment nicht Freigegeben.");
		add(mysql, "TTT_TRAITOR_CHAT", "§eUm den §cTraitor-Chat§e zu benutzten §c/tc [Nachricht]§e.");
		add(mysql, "TTT_TESTER_USED", "§eDer Tester ist immoment nicht Frei.");
		add(mysql, "TTT_TESTER_TIME", "§eDu warst schon in Tester kommer spaeter wieder.");
		add(mysql, "TTT_TRAITOR_SHOP", "Du hast nicht genug Traitor-Punkte!");
		add(mysql, "TTT_PASSE_USE", "§eDu hast einen §b{INPUT0}§e Pass benutzt! Du hast jetzt noch §b{INPUT1}!");
		add(mysql, "TTT_PASSE_KEINE", "§eDu besitzt keine {INPUT0} Paesse!");
		add(mysql, "TTT_PASSE_LOBBYPHASE", "§eDie LobbyPhase ist vorbei...");
		add(mysql, "TTT_PASSE_MAX_USED", "§eEs wurden schon zu viele §b{INPUT0}§e Paesse benutzt");
		add(mysql, "TTT_SHOP", "§eDu hast nicht genug Punkte!");
		add(mysql, "TTT_SHOP_BUYED", "§eDu hast erfolgreich ein Item erworben!");
		add(mysql, "TTT_NPC_CLICKED", "§eDas ist §b{INPUT0}§e er war ein §c{INPUT1}.");
		add(mysql, "TTT_DNA_TEST", "§eDer DNA-TEST hat ergeben, dass der Spieler {INPUT0}§e von {INPUT1}§e getoetet wurde.");
		add(mysql, "TTT_TRAITOR_SHOP_RADAR_CHANGE", "§eDu hast den Spieler §b{INPUT0}§e ausgewaehlt!");
		add(mysql, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH", "Bei diesem Spieler ist es Zuspaet");
		add(mysql, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT", "Du hast erfolgreich {INPUT0} wiederbelebt");
		add(mysql, "TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER", "Du wurdest von {INPUT0} wiederbelebt");
		add(mysql, "TTT_TESTER_JOIN", "{INPUT0} ist den Tester beigetreten.");
		add(mysql, "TTT_LEICHE_IDENTIFIZIERT", "§eDie Leiche von {INPUT0}§e wurde gefunden er war ein {INPUT1}.");
		add(mysql, "TTT_IS_NOW", "§eDu bist ein §c{INPUT0}.");
		add(mysql, "COINS_ADD", "§eDu hast §b{INPUT0}§e Coins erhalten.");
		add(mysql, "COINS_DEL", "§eDir wurden §c{INPUT0} §eCoins abgezogen.");
		add(mysql, "GAME_AUSGESCHIEDEN", "{INPUT0} wurde aus dem Spiel ausgeschlossen!");
		add(mysql, "CAVEWARS_SPIDER_DEATH", "§eDas Schaf vom Team {INPUT0}§e wurde von§b {INPUT1} §egetoetet!");
		add(mysql, "SHEEPWARS_SHEEP_DEATH", "§eTeam {INPUT0}§e hat ihr Schaf verloren!");
		add(mysql, "BEDWARS_BED_BROKE", "§eTeam {INPUT0}§e hat ihr Bett verloren!");
		add(mysql, "GILDE_MONEY_DEPOSIT", "§b{INPUT0}§a hat §b{INPUT1} Epics§a eingezahlt.");
		add(mysql, "GILDE_MONEY_LIFTED", "§b{INPUT0}§a hat§b {INPUT1} Epics§a abgehoben.");
		add(mysql, "GILDE_NOT_ENOUGH_MONEY", "Es ist nicht genug auf dem Gilden-Konto.");
		add(mysql, "GILDE_CREATE", "§aDu hast erfolgreich die Gilde §6{INPUT0}§a gegruendet! Dein Gilden Home wurde auf deine aktuelle Position gesetzt.");
		add(mysql, "GILDE_PLAYER_OFFLINE", "§cDer Spieler §b{INPUT0}§c ist offline.");
		add(mysql, "GILDE_PREFIX", "§7[§6Gilde§7]: ");
		add(mysql, "GILDE_PLAYER_ENTRE", "§b{INPUT0} hat die Gilde betreten.");
		add(mysql, "GILDE_NAME_LENGTH_MIN", "§cDer Gildenname muss laenger als §b{INPUT0}§c Zeichen sein.");
		add(mysql, "GILDE_NAME_LENGTH_MAX", "§cDer Gildenname muss kleiner als §b{INPUT0}§c Zeichen sein.");
		add(mysql, "GILDE_EXIST", "§cDer Gildenname wurde bereits benutzt.");
		add(mysql, "GILDE_EXIST_NOT", "§cDiese Gilde exestiert nicht!");
		add(mysql, "GILDE_DELETE", "§cDu hast die Gilde geloescht.");
		add(mysql, "GILDE_PLAYER_LEAVE", "§b%§c ist §4offline§c gegangen.");
		add(mysql, "GILDE_EINLADEN", "§aDu hast §b{INPUT0}§a in die Gilde eingeladen");
		add(mysql, "GILDE_STATS_PREFIX", "§b■■■■■■■■ §6§lGilden §b■■■■■■■■");
		add(mysql, "GILDE_STATS_PREFIXBY", "§b■■■■■■■■ §6§lGilden Info von {INPUT0}§b ■■■■■■■■");
		add(mysql, "GILDE_EILADUNG", "§aDu wurdest in die Gilde §b{INPUT0}§a eingeladen");
		add(mysql, "GILDE_CLOSED", "§cDie Gilde wurde geschlossen.");
		add(mysql, "GILDE_KICK_PLAYER", "§cDer Spieler §b{INPUT0}§c wurde aus der Gilde gekickt.");
		add(mysql, "GILDE_SETHOME", "§aDu hast das Gildenhome gesetzt.");
		add(mysql, "GILDE_SETISLAND", "§aDu hast eine Gildenisland erstellt.");
		add(mysql, "GILDEN_NAME", "§cDu hast ein Ungueltiges Zeichen in deinen Gildennamen!");
		add(mysql, "GILDE_TELEPORT_CANCELLED", "§cDie Teleportation wurde abgebrochen.");
		add(mysql, "GILDE_TELEPORTET", "§cDie Teleportation wurde durchgefuehrt.");
		add(mysql, "GILDE_HOME", "§aDu wirst in §c{INPUT0}§a Teleportiert...");
		add(mysql, "GILDE_PLAYER_NICHT_EINGELADEN", "§cDu wurdest in nicht in eine Gilde eingeladen.");
		add(mysql, "GILDE_PLAYER_JOIN", "§a{INPUT0}§b hat den Server betreten.");
		add(mysql, "FRIEND_HIT", "§cWillst du deinen Kameraden toeten?");
		add(mysql, "FRIEND_PREFIX", "§7[§6Freund§7]: ");
		add(mysql, "FRIEND_EXIST", "§aDu bist bereits mit dem Spieler §b{INPUT0}§a befreundet.");
		add(mysql, "FRIEND_SEND", "§aDu hast dem Spieler §b{INPUT0}§a eine freundschaftsanfrage gesendet.");
		add(mysql, "FRIEND_NOT", "§cDu bist nicht mit §b{INPUT0}§c befreundet.");
		add(mysql, "FRIEND_DEL", "§cDeine Freundschaft zu §b{INPUT0}§c wurde beendet!");
		add(mysql, "FRIEND_DEL_IN", "§aIn §6{INPUT0}§a Sekunden wird die Freundschaft mit §b{INPUT1}§a aufgeloest.");
		add(mysql, "FRIEND_GET", "§aDu hast von §b{INPUT0}§a eine Freundschaftsanfrage erhalten.");
		add(mysql, "FRIEND_NOW", "§aDu bist nun mit §b{INPUT0}§a befreundet!");
		add(mysql, "FRIEND_ASK_NOT", "§aDu hast momentan keine Freundschaftsanfragen.");
		add(mysql, "FRIEND_IS_FRIEND", "§aDu bist mit §b{INPUT0}§a befreundet!");
		add(mysql, "FRIEND_YOURE_SELF", "§cDu kannst nicht mit dir selber befreundet sein!");
		add(mysql, "NEULING_SCHUTZ", "§cDer Spieler §b{INPUT0}§c ist noch im Neulingsschutz!");
		add(mysql, "NEULING_SCHUTZ_YOU", "§cDu bist momentan noch im Neulingsschutz benutzte §7/neuling§c um den Schutz zu deaktivieren.");
		add(mysql, "NEULING_END", "§cDu besitzt nun kein Neulingsschutz mehr!");
		add(mysql, "NEULING_CMD", "§cDu bist kein Neuling.");
		add(mysql, "ANTI_LOGOUT_FIGHT", "§cDu bist nun im Kampf, logge dich nicht aus!");
		add(mysql, "ANTI_LOGOUT_FIGHT_END", "§cDu bist nun nicht mehr im Kampf!");
		add(mysql, "TELEPORT_VERZÖGERUNG", "§aDu wirst in §b{INPUT0}§a teleportiert.");
		add(mysql, "TELEPORT", "§aDu wurdest erfolgreich teleportiert!");
		add(mysql, "TELEPORT_HERE", "§a{INPUT0} wurdest erfolgreich teleportiert!");
		add(mysql, "ACCEPT", "§aDu hast die Anfrage angenommen.");
		add(mysql, "DENY", "§cDu hast die Anfrage abgelehnt.");
		add(mysql, "ACCEPT_FROM", "§a{INPUT0} hat die Anfrage angenommen.");
		add(mysql, "DENY_FROM", "§c{INPUT0} has die Anfrage abgelehnt.");
		add(mysql, "NO_ANFRAGE", "§cDu hast keine Anfrage erhalten.");
		add(mysql, "ME", "mir");
		add(mysql, "TELEPORT_ANFRAGE_HERE_EMPFÄNGER", "§aDer Spieler §b{INPUT0}§a fragt, ob du dich zu ihn telepotieren moechtest. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny");
		add(mysql, "TELEPORT_ANFRAGE_EMPFÄNGER", "§aDer Spieler §b{INPUT0}§a fragt, ob er sich zu dir telepotieren darf. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny");
		add(mysql, "TELEPORT_ANFRAGE_SENDER", "§aDu hast §b{INPUT0}§a eine Teleport Anfrage gesendet.");
		add(mysql, "WARP_EXIST", "§cDer Warp existiert nicht.");
		add(mysql, "WARP_SET", "§aDer Warp §b{INPUT0}§a wurde gespeichert!");
		add(mysql, "KIT_USE", "§§cDu hast das Kit §b{INPUT0}§c benutzt.");
		add(mysql, "KIT_SET", "§7Das Kit §b{INPUT0}§7 wurde gespeichert");
		add(mysql, "KIT_EXIST", "§cDas Kit existiert nicht.");
		add(mysql, "KIT_DEL", "§cDas Kit §b{INPUT0}§c wurde entfernt");
		add(mysql, "KIT_DELAY", "§cDu kannst das Kit erst in §b{INPUT0}§c wieder benutzen.");
		add(mysql, "CLEARINVENTORY", "§cDein Inventar wurde geleert");
		add(mysql, "CLEARINVENTORY_OTHER", "§cDein Inventar wurde von §b{INPUT0}§c geleert.");
		add(mysql, "CLEARINVENTORY_FROM_OTHER", "§cDu hast das Inventar von§b {INPUT0}§c geleert");
		add(mysql, "CLEARINVENTORY_ALL", "§c§4Du hast das Inventar von allen Spielern geleert.");
		add(mysql, "NO_ITEM_IN_HAND", "§cDu hast kein Item in der Hand.");
		add(mysql, "ITEM_RENAME", "§aDas Item wurde umbenannt");
		add(mysql, "NO_ANSWER_PARTNER", "§cDu hast niemanden zum antworten.");
		add(mysql, "SOCIALSPY_ON", "§aDu hast Socialspy aktiviert.");
		add(mysql, "SOCIALSPY_OFF", "§cDu hast Socialspy deaktiviert.");
		add(mysql, "MONEY_NO_DOUBLE", "§cEntweder ist das keine Zahl oder 0.");
		add(mysql, "MONEY_ENOUGH_MONEY", "§cDu hast nicht genug Epics.");
		add(mysql, "MONEY_SEND_TO", "§aDu hast dem Spieler §b{INPUT0}§7 {INPUT1}§a Epics gesendet.");
		add(mysql, "MONEY_RECEIVE_FROM", "§aDu hast von §b{INPUT0}§b {INPUT1}§a Epics erhalten.");
		add(mysql, "FALLDOWN_BREWITEM_BLOCKED", "§cDu kannst erst in §b{INPUT0}§c dein Item benutzten.");
		add(mysql, "VANISH_AN", "§aVanish wurde aktiviert.");
		add(mysql, "VANISH_AUS", "§cVanish wurde deaktiviert.");
		add(mysql, "UNKNOWN_CMD", "§7Viele Befehle. Solche nicht. Falsche geschreib?! Wow.");
		add(mysql, "NO_PERMISSION", "§cViele Berechtigungen, diese nicht, Wow.");
		add(mysql, "WHEREIS_TEXT", "§6Du befindest dich momentan auf dem §b{INPUT0}§6 Server.");
		add(mysql, "FALLDOWN_NICHT_GENUG_POWER", "§cDu hast nicht genug Kraft!");
		add(mysql, "SKYBLOCK_HAVE_ISLAND", "§cDu hast bereits eine Insel!");
		add(mysql, "SKYBLOCK_NO_ISLAND", "§cDu besitzt keine Insel.");
		add(mysql, "SKYBLOCK_PARTY_NO", "§aAktuell hast du keine Party am laufen.");
		add(mysql, "SKYBLOCK_REMOVE_ISLAND", "§cDeine Insel wurde geloescht!");
		add(mysql, "SKYBLOCK_CHANGE_BIOME", "§aDas Biom deiner Inesel wurde zu §b{INPUT0}§a geaendert.");
		add(mysql, "SKYBLOCK_CREATE_ISLAND", "§aDeine Insel wurde erstellt.");
		add(mysql, "SKYBLOCK_PLAYER_KICK", "§aDu hast den Spieler §c{INPUT0}§a von deiner Insel gekickt.");
		add(mysql, "SKYBLOCK_PLAYER_KICKED", "§cDu wurdest von §4{INPUT0}s§c Insel gekickt.");
		add(mysql, "SKYBLOCK_PLAYER_NOT_ON_YOUR_ISLAND", "§aDer Spieler §c{INPUT0}§a ist nicht auf deiner Insel.");
		add(mysql, "NO_BEFEHL", "§cDieser Befehl exestiert nicht!");
		add(mysql, "WARPS_EMPTY","Es exestieren noch keine Warps");
		add(mysql,"NOT_ENOUGH_EXP","§cDu hast nicht genug Exp!");
		add(mysql,"NOT_ENOUGH_MONEY","§cDu hast nicht genug Geld!");
		add(mysql, "SKYBLOCK_PARTY_EINLADEN_IS_IN", "Der Spieler ist bereits in einer Party.");
		add(mysql, "SKYBLOCK_PARTY_IN", "§cDu bist bereits in einer Party. Tippe §7/party verlassen§a um sie wieder zu verlassen!");
		add(mysql, "SKYBLOCK_PARTY_EINLADEN_NO", "§cDu wurdest in keine Party eingeladen.");
		add(mysql, "SKYBLOCK_PARTY_ENTER", "§aDu hast die Party betreten!");
		add(mysql, "SKYBLOCK_PARTY_ENTER_BY", "§aDer Spieler §b{INPUT0}§a hat die Party betreten.");
		add(mysql, "SKYBLOCK_PARTY_SIZE", "§cDu kannst maximal §b{INPUT0}§c Spieler in deine Party einladen.");
		add(mysql, "SKYBLOCK_PARTY_VOLL", "§cDie Party ist voll!");
		add(mysql, "SKYBLOCK_PARTY_EINLADEN_INVITE", "§aDu wurdest von §b{INPUT0}§a in seine Skyblock-Part eingeladen. Wenn du annehmen moechtest§7 /party annehmen");
		add(mysql, "SKYBLOCK_PARTY_EINLADEN_IS", "§cDer Spieler §b{INPUT0}§c ist bereits zur Party eingeladen.");
		add(mysql, "SKYBLOCK_PARTY_ERSTELLT", "§aDeine Skyblock-Party wurde erfolgreich erstellt!");
		add(mysql, "SKYBLOCK_PARTY_NO_OWNER", "§cDu bist nicht der Owner dieser Party.");
		add(mysql, "SKYBLOCK_PARTY_EINLADEN", "§aDu hast §b{INPUT0}§a zur Party eingeladen.");
		add(mysql, "SKYBLOCK_PARTY_VERLASSEN", "§cDu hast die Party verlassen.");
		add(mysql, "SKYBLOCK_PARTY_KICKEN", "§cDu hast §b{INPUT0}§c aus der Party gekickt.");
		add(mysql, "SKYBLOCK_PARTY_SCHLIEßEN", "§cDie Party wurde geschlossen.");
		add(mysql, "SKYBLOCK_PARTY_PLAYER_NOT", "§cDieser Spieler ist nicht in der Party.");
		add(mysql, "SKYBLOCK_TELEPORT_HOME", "§aDu wurdest zu deiner Insel teleportiert!");
		add(mysql, "SKYBLOCK_PREFIX", "§b■■■■■■■■ §6§lSkyBlock §b■■■■■■■■");
		add(mysql, "SKYBLOCK_PARTY_PREFIX", "§b■■■■■■■■ §6§lSkyBlock Party §b■■■■■■■■");
		add(mysql, "SKYBLOCK_REMOVE_ISLAND_ONE", "§cDu kannst nur einmal am Tag deine Insel loeschen oder als Ultra eine Gilden Insel erstellen!");
		add(mysql, "SIGN_SHOP_NO_ITEM_ON_INV", "§cDu hast das Item nicht im Inventar oder du hast zu wenig von den Items");
		add(mysql, "SIGN_SHOP_VERKAUFT_", "§6Du hast {INPUT0} mal {INPUT1}:{INPUT2} Verkauft und hast {INPUT3} Epics erhalten.");
		add(mysql, "SIGN_SHOP_VERKAUFT", "§6Du hast {INPUT0} mal {INPUT1} Verkauft und hast {INPUT2} Epics erhalten.");
		add(mysql, "SIGN_SHOP_DELAY", "§cDu kannst nur alle {INPUT0} sekunden was verkaufen!");
		add(mysql, "SIGN_SHOP_GET", "§6Du hast {INPUT0} mal {INPUT1} bekommen dir wurden {INPUT2} §bEpics §6abgezogen.");
		add(mysql, "SIGN_SHOP_GET_", "§6Du hast {INPUT0} mal {INPUT1}:{INPUT2} bekommen dir wurden {INPUT3} §bEpics §6abgezogen.");
		add(mysql, "kFLY_NOT_ON", "§cFly ist nicht aktiviert!");
		add(mysql, "NO_INTEGER", "§cDas ist keine Zahl.");
		add(mysql, "kFLY_SPEED", "§aFly speed wurde auf §b{INPUT0}§a geaendert");
		add(mysql, "kFLY_ON", "§aFly wurde aktiviert.");
		add(mysql, "kFLY_OFF", "§cFly wurde deaktiviert.");
		add(mysql, "kFLY_PVP_FLAG", "§cDu kannst nicht im PvP-Bereich fliegen.");
		add(mysql, "SPAWN_SET", "§aDer Spawn wurde gespeichert!");
		add(mysql, "SPAWN_TELEPORT", "§aDu wurdest zum Spawn Teleportiert!");
		add(mysql, "GIVEALL", "§c{INPUT0}§b hat jedem Spieler {INPUT1}x §a{INPUT2} §bgegeben.");
		add(mysql, "REPAIR_HAND", "§cDas Item in deiner Hand wurde repariert!");
		add(mysql, "REPAIR_ALL", "§cDein Inventory wurde repariert!");
		add(mysql, "FEED", "§cDu hast nun kein Hunger mehr!");
		add(mysql, "FEED_ALL", "§cDer Spieler {INPUT0} hat dein Hunger gestillt!");
		add(mysql, "FEED_OTHER", "§aDu hast den Hunger von §b{INPUT0}§c gestillt!");
		add(mysql, "HEAL", "§cDu hast nun wieder volles Leben.");
		add(mysql, "HEAL_ALL", "§aDer Spieler §b{INPUT0}§c hat nun wieder volles Leben.");
		add(mysql, "HEAL_OTHER", "§aDu hast §b{INPUT0}s§c geheilt.");
		add(mysql, "DAY", "§aEs ist nun 4:55 Uhr.");
		add(mysql, "NIGHT", "§aEs ist nun 18:34 Uhr.");
		add(mysql, "SUN", "§aJetzt scheint die Sonne.");
		add(mysql, "USE_ENDERPEARL_TIME", "§cDu kannst die Enderperle in §b{INPUT0}§c wieder benutzten");
		add(mysql, "USE_BEFEHL_TIME", "§cDu kannst den Befehl in §b{INPUT0}§c wieder benutzten.");
		add(mysql, "NO_RANG", "§cDu hast keine Rechte dazu");
		add(mysql, "HOME_SET", "§7Das Home §b{INPUT0}§7 wurde gespeichert!");
		add(mysql, "HOME_EXIST", "§cDas Home exestiert nicht.");
		add(mysql, "HOME_DEL", "§cDas Home §b{INPUT0}§c wurde geloescht");
		add(mysql, "HOME_SKYBLOCK_DELETE", "§7Die Homes von §b{INPUT0}§7 wurden entfernt.");
		add(mysql, "HOME_MAX", "§cDu darfst nur §b{INPUT0}§c Homes setzten!");
		add(mysql, "HOME_QUESTION", "§aDer Spieler §b{INPUT0}§a fragt, ob er ein Home setzten darf.§b /homeaccept or. /homedeny");
		add(mysql, "HOME_ISLAND", "§aEs wurde eine anfrage zum setzten des Homes abgesetzt.");
		add(mysql, "WARP_DEL", "§aDer Warp §b{INPUT0}§a wurde geloescht");
		add(mysql,"NOT_ENTITY_ID","§cDas ist keine ID.");
		add(mysql, "WARP_EXIST", "§cDer Warp exestiert nicht.");
		add(mysql, "VOTE_THX", "§aDanke fürs §lVoten§a du hast deine Belohnung bekommen.");
		add(mysql, "ANIT_LOGOUT_FIGHT_CMD", "§cDu kannst den Befehl §b/gilden home§c nicht in Kampf ausführen!");
		add(mysql,"XMAS_DOOR","§aDu hast das Türchen geöffnet und §e{INPUT0}§a Coins erhalten!");
		add(mysql,"XMAS_DAY","§cIst heute der {INPUT0}te ?");
		add(mysql,"LOGIN_FAIL","§cUngültiges Zeichen!");
		add(mysql,"LOAD_PLAYER_DATA","Deine Spieler Informationen werden geladen...");
		add(mysql,"PET_MUST_BUYED_IN_SHOP","§cDu musst das Pet im Online-Store kaufen §bShop.EpicPvP.de");
		add(mysql,"GAME_TIME_CHANGE","Die Zeit wurde zu {INPUT0} geändert!");
	}
	
	public static void add(MySQL mysql,String name,String msg){
		if(!list.get(LanguageType.GERMANY).containsKey(name))mysql.Update("INSERT INTO language (type,name,msg) VALUES ('"+LanguageType.GERMANY.getDef()+"','"+name+"','"+msg+"');");
	}
	
}
