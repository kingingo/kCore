package me.kingingo.kcore.Enum;

import lombok.Setter;
import me.kingingo.kcore.Util.C;

public enum Text {
STATS_PREFIX("§b■■■■■■■■§6 §lSTATS §b■■■■■■■■"),
STATS_PREFIXBY("§b■■■■■■■■ §6§lStats von % §b■■■■■■■■"),
STATS_KILLS("§6Kills : §7"),
STATS_DEATHS("§6Tode : §7"),
STATS_MONEY("§6Geld : §7"),
STATS_KDR("§6KDR : §7"),
PREMIUM_PET("§cKaufe dir §aPremium§c um diese funktion nutzen zu können."),
TREASURE_CHEST_TOO_NEAR("§bDu bist zu naher an einer anderen TreasureChest!"),
STATS_GILDE("§6Gilde : §7"),
STATS_RANKING("§6Ranking : §7"),
SPIELER_ENTFERNT_COMPASS("§7Der Spieler §b%§7 ist §b% §7Bloecke von dir enfternt!"),
AUßERHALB_DER_MAP("Du bist außerhalb der Map!"),
CMD_MUTE("§aDie Commands wurden gesperrt!"),
CMD_UNMUTE("§cDie Commands wurde entsperrt!"),
PVP_MUTE("§cPvP wurde deaktiviert!"),
PVP_UNMUTE("§aPvP wurde Aktiviert!"),
CHAT_MUTE("§aDer Chat wurde gesperrt!"),
CHAT_UNMUTE("§cDer Chat wurde entsperrt!"),
ENDERGAMES_TELEPORT("§7Du hast deine Position mit §b% §7gewechselt!"),
KIT_SHOP_ADD("§eDu hast das Kit §3% §egewählt."),
KIT_SHOP_NO_MONEY("Du hast nicht genug §b%§e."),
KIT_SHOP_BUYED_KIT("Du hast erfolgreich das Kit§3 % 3Egekauft."),
PLAYER_IS_OFFLINE("Der Spieler % ist Offline."),
FIGHT_START_IN(C.cGray+"§eIhr könnt in§b % §eSekunden Kämpfen!"),
FIGHT_START(C.cGray+"§cIhr könnt nun Kämpfen!"),
GAME_END_IN(C.cGray+"§eDas Spiel endet in§b % §e"),
GAME_END(C.cGray+"§cDas Spiel wurde beendet."),
RESTART_IN(C.cGray+"§4Der Server Restartet in§b % §4Sekunden"),
RESTART(C.cGray+"§4§lDer Server Restartet Jetzt"),
SERVER_FULL(C.cRed+"§cDer Server ist voll!"),
SERVER_NOT_LOBBYPHASE(C.cRed+"§cDieser Server ist aktuell nicht in der Lobbyphase!"),
SERVER_FULL_WITH_PREMIUM("§cDer Server ist voll! Kaufe dir Premium auf §bshop.EpicPvP.de§c um auf vollen Servern zu joinen"),
KILL_BY("§b% §ewurde von§a % §egetötet"),
DEATH("§b% §eist gestorben."),
GAME_EXCLUSION(C.cRed+"% wurde vom Spiel ausgeschlossen"),
PERK_NOT_BOUGHT("§cDu hast noch keine Perks gekauft! §bShop.EpicPvP.de"),
VOTE_TEAM_ADD("§cDu bist nun in§a %§c"),
VOTE_TEAM_MIN_PLAYER("Es muessen min. % Spieler online sein!"),
VOTE_TEAM_REMOVE("§cDu hast §a %§c verlassen"),
VOTE_TEAM_FULL("§a%§c ist voll"),
VOTE_MIN("§eEs müssen mindestens §b% §eSpieler online sein damit das Spiel starten kann"),
KICKED_BY_PREMIUM(C.cRed+"§cDu wurdest vom Server gekickt damit ein Premium User spielen kann. Du möchtest auch? &bshop.EpicPvP.de"),
PREFIX_GAME(C.cDAqua+"§7[§6%§7]: "+C.cGray),
PREFIX(C.cDAqua+"§7[§6EpicPvP§7]: "+C.cGray),
TEAM_WIN("§eDas Team %§e hat das Spiel Gewonnen."),
TEAM_OUT("§b%§c ist gefallen"),
RESTART_FROM_ADMIN("§cDer Server wurde von einem Administrator restartet!"),
SCHUTZZEIT_END_IN("§eDie Schutzzeit endet in§b %§e Sekunden"),
SCHUTZZEIT_END("§eDie Schutzzeit ist nun zuende, ihr könnt euch bekriegen"),
GAME_START(C.cGray+"§eDas Spiel ist gestartet"),
GAME_START_IN(C.cGray+"§eDas Spiel startet in§b %§e Sekunden"),
DEATHMATCH_START(C.cGray+"§cDas Deathmatch startet"),
DEATHMATCH_START_IN(C.cGray+"§cDas Deathmatch startet in §b% §cSekunden"),
DEATHMATCH_END(C.cGray+"Das Deathmatch ist zu ende."),
GAME_WIN(C.cAqua+"§eDer Spieler §b% §ehat das Spiel gewonnen!"),
SURVIVAL_GAMES_DISTRICT_WIN("§eDas District§a % §emit den Spielern§b %§e und§b % §ehat Gewonnen"),
DEATHMATCH_END_IN(C.cGray+"§cDas Deathmatch endet in §b%§c Sekunden"),
LOGIN_MESSAGE("§cBitte logge dich ein: §b/Login <Password>"),
KICK_BY_FALSE_PASSWORD("§cDu hast §b%§c mal dein Password falsch eingegeben"),
LOGIN_ACCEPT("§aDu hast dich Erfolgreich eingeloggt"),
LOGIN_DENY("§cDas Password ist falsch"),
REGISTER_ACCEPT("§aRegister Erfolgreich!"),
REGISTER_MESSAGE("§cRegistriere dich bitte: §b/Register <Password>"),
SPECTATOR_CHAT_CANCEL("§cDu kannst als Spectator nicht im Chat schreiben."),
TTT_WIN("§eDie§c§l % §ehaben diese Runde gewonnen!"),
TTT_DEATH("§eDer Spieler§b %§e ist gestorben und war§c %."),
TTT_TESTER_WAS_USED("§eDer Tester ist im moment nicht Freigegeben."),
TTT_TRAITOR_CHAT("§eUm den §cTraitor-Chat§e zu benutzten §c'/tc [Nachricht]'§e. "),
TTT_TESTER_USED("§eDer Tester ist im moment nicht Frei."),
TTT_TESTER_TIME("§eDu warst schon in Tester kommer später wieder."),
TTT_TRAITOR_SHOP("Du hast nicht genug Traitor-Punkte!"),
TTT_PASSE_USE("§eDu hast einen§b %§e Pass benutzt du hast jetzt noch§b %!"),
TTT_PASSE_KEINE("§eDu besitzt keine % Pässe!"),
TTT_PASSE_LOBBYPHASE("§eDie LobbyPhase ist vorbei..."),
TTT_PASSE_MAX_USED("§eEs wurden schon zu viele §b%§e Pässe benutzt"),
TTT_SHOP("§eDu hast nicht genug Punkte!"),
TTT_SHOP_BUYED("§eDu hast erfolgreich ein Item erworben!"),
TTT_NPC_CLICKED("§eDas ist§b %§e er war ein§c %."),
TTT_DNA_TEST("§eDer DNA-TEST hat er geben das der Spieler %§e von %§e getötet wurde."),
TTT_TRAITOR_SHOP_RADAR_CHANGE("§eDu hast den Spieler§b %§e ausgewählt!"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH("Bei diesem Spieler ist es Zuspät"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT("Du hast erfolgreich % wiederbelebt"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER("Du wurdest von % wiederbelebt"),
TTT_TESTER_JOIN("% ist den Tester beigetreten."),
TTT_LEICHE_IDENTIFIZIERT("§eDie Leiche von % §ewurde gefunden er war ein %."),
TTT_IS_NOW("§eDu bist ein §c%."),
TOKENS_ADD("§eDu hast §b%§e Tokens erhalten."),
TOKENS_DEL("§eDir wurden §c% §eTokens abgezogen."),
COINS_ADD("§eDu hast §b%§e Coins erhalten."),
COINS_DEL("§eDir wurden §c% §eCoins abgezogen."),
GAME_AUSGESCHIEDEN("% wurde aussem Spiel ausgeschlossen!"),
CAVEWARS_SPIDER_DEATH("§eDas Schaf vom Team %§e wurde von§b % §egetötet!"),
SHEEPWARS_SHEEP_DEATH("§eDas Schaf vom Team %§e wurde von§b % §egetötet!"),
GILDE_CREATE("§aDu hast erfolgreich die Gilde§6 %§a gegründet! Dein Gilden Home wurde auf deine aktuelle Position gesetzt."),
GILDE_PLAYER_OFFLINE("§cDer Spieler §b% §cist Offline."),
GILDE_PREFIX("§7[§6Gilde§7]: "),
GILDE_PLAYER_GO_OUT("§b% §chat die Gilde verlassen."),
GILDE_COUNT("§cDie Maximalanzahl der Gilde ist erreicht!"),
GILDE_PLAYER_LEAVE("§b%§c ist §4offline§c gegangen."),
GILDE_PLAYER_IS_IN_GILDE1("§c% ist bereits in einer Gilde."),
GILDE_PLAYER_IS_IN_GILDE("§cDu bist bereits in einer Gilde"),
GILDE_PLAYER_IS_NOT_IN_GILDE("§cDu bist in keiner Gilde"),
GILDE_IS_NOT_IN_THE_GILD("§cDer Spieler §b%§c ist nicht in deiner Gilde!"),
GILDE_OWNER_NOT("§cDu bist nicht der Owner der Gilde."),
GILDE_PLAYER_ENTRE("§aDie Gilde wurde von§b %§a betreten."),
GILDE_NAME_LENGTH_MIN("§cDer Gilden Name muss länger als §b%§c Zeichen sein."),
GILDE_NAME_LENGTH_MAX("§cDer Gilden Name muss kleiner als §b%§c Zeichen sein."),
GILDE_EXIST("§cDer Gilden Name wurde bereits benutzt."),
GILDE_EXIST_NOT("§cDiese Gilde exestiert nicht!"),
GILDE_DELETE("§cDu hast die Gilde gelöscht."),
GILDE_EINLADEN("§aDu hast§b %§a in die Gilde eingeladen"),
GILDE_STATS_PREFIX("§b■■■■■■■■ §6§lGilden §b■■■■■■■■"),
GILDE_STATS_PREFIXBY("§b■■■■■■■■ §6§lGilden Info von % §b■■■■■■■■"),
GILDE_EILADUNG("§aDu wurdest in die Gilde §b%§a eingeladen"),
GILDE_CLOSED("§cDie Gilde wurde geschlossen."),
GILDE_KICK_PLAYER("§cDer Spieler§b %§c wurde aus der Gilde gekickt."),
GILDE_SETHOME("§aDu hast das Gilden Home gesetzt."),
GILDE_SETISLAND("§aDu hast eine Gilden Island erstellt."),
GILDEN_NAME("§cDu hast ein Ungültiges Zeichen in deinen Gildennamen!"),
GILDE_TELEPORT_CANCELLED("§cDie Teleportation wurde abgebrochen."),
GILDE_TELEPORTET("§cDie Teleportation wurde durchgeführt."),
GILDE_HOME("§aDu wirst in§c %§a Teleportiert..."),
GILDE_PLAYER_NICHT_EINGELADEN("§cDu wurdest in nicht in eine Gilde eingeladen."),
GILDE_PLAYER_JOIN("§a%§b hat den Server betreten."),
FRIEND_HIT("§cDu willst deine Kameraden töten?"),
FRIEND_PREFIX("§7[§6Friend§7]: "),
FRIEND_EXIST("§aDu bist bereits mit den Spieler §b%§a befreundet."),
FRIEND_SEND("§aDu hast den Spieler §b%§a eine freundschaftsanfrage gesendet."),
FRIEND_NOT("§cDu bist nicht mit §b%§c befreundet."),
FRIEND_DEL("§cDeine Freundschaft zu §b%§c wurde beendet!"),
FRIEND_DEL_IN("§aIn §6%§a Sekunden wird die Freundschaft mit§b %§a aufgelöst."),
FRIEND_GET("§aDu hast von §b%§a eine Freundschaftsanfrage erhalten."),
FRIEND_NOW("§aDu bist nun mit §b%§a befreundet!"),
FRIEND_ASK_NOT("§aDu hast momentan keine Freundschaftsanfragen."),
FRIEND_IS_FRIEND("§aDu bist mit §b%§a befreundet!"),
FRIEND_YOURE_SELF("§cDu kannst nicht mit dir selber befreundet sein!"),
NICK("§aDu hast dein Nick zu §b§l%§a geändert!"),
NEULING_SCHUTZ("§cDer Spieler§b % §cist noch im Neuling's Schutz!"),
NEULING_SCHUTZ_YOU("§cDu bist momentan noch im Neuling Schutz benutzte §7'/neuling'§c um den Schutz zu Deaktivieren."),
NEULING_END("§cDu besitzt nun kein Neuling Schutz mehr!"),
NEULING_CMD("§cDu bist kein Neuling."),
ANTI_LOGOUT_FIGHT("§cDu bist nun im Kampf, logge dich nicht aus!"),
ANTI_LOGOUT_FIGHT_END("§cDu bist nun nicht mehr im Kampf!"),
TELEPORT_VERZÖGERUNG("§aDu wirst in §b%§a teleportiert."),
TELEPORT("§aDu wurdest erfolgreich teleportiert!"),
TELEPORT_HERE("§a% wurdest erfolgreich teleportiert!"),
ACCEPT("§aDu hast die Anfrage angenommen."),
DENY("§cDu hast die Anfrage abgelehnt."),
ACCEPT_FROM("§a% hat die Anfrage angenommen."),
DENY_FROM("§c% has die Anfrage abgelehnt."),
NO_ANFRAGE("§cDu hast keine Anfrage erhalten."),
TELEPORT_ANFRAGE_HERE_EMPFÄNGER("§aDer Spieler§b % §afragt ob du dich zu ihn telepotieren möchtest. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny"),
TELEPORT_ANFRAGE_EMPFÄNGER("§aDer Spieler§b % §afragt ob er sich zu dir telepotieren darf. Um zu akzeptieren §7/tpaccept§a um abzulehnen §7/tpdeny"),
TELEPORT_ANFRAGE_SENDER("§aDu hast§b % §aeine Teleport Anfrage gesendet."),
WHEREIS_TEXT("§6Du befindest dich momentan auf dem §b%§6 Server."),
FALLDOWN_NICHT_GENUG_POWER("§bDu hast nicht genug Power!"),
SKYBLOCK_HAVE_ISLAND("§cDu hast bereits eine Insel!"),
SKYBLOCK_NO_ISLAND("§cDu besitzt keine Insel."),
SKYBLOCK_PARTY_NO("§aAktuell hast du keine Party am laufen."),
SKYBLOCK_REMOVE_ISLAND("§cDeine Insel wurde gelöscht!"),
SKYBLOCK_CHANGE_BIOME("§aDas Biom deiner Inesel wurde zu §b%§a geändert."),
SKYBLOCK_CREATE_ISLAND("§aDeine Insel wurde erstellt."),
NO_BEFEHL("§cDieser Befehl exestiert nicht!"),
SKYBLOCK_PARTY_EINLADEN_IS_IN("Der Spieler ist bereits in einer Party."),
SKYBLOCK_PARTY_IN("§cDu bist bereits in einer Party. Tippe §7/party verlassen§a um sie wieder zu verlassen!"),
SKYBLOCK_PARTY_EINLADEN_NO("§cDu wurdest in keine Party eingeladen."),
SKYBLOCK_PARTY_ENTER("§aDu hast die Party betreten!"),
SKYBLOCK_PARTY_ENTER_BY("§aDer Spieler§b %§a hat die Party betreten."),
SKYBLOCK_PARTY_SIZE("§cDu kannst maximal§b %§c Spieler in deine Party einladen."),
SKYBLOCK_PARTY_VOLL("§cDie Party ist voll!"),
SKYBLOCK_PARTY_EINLADEN_INVITE("§aDu wurdest von §b%§a in seine Skyblock-Part eingeladen. Wenn du annehmen möchtest§7 /party annehmen"),
SKYBLOCK_PARTY_EINLADEN_IS("§cDer Spieler§b % §cist bereits zur Party eingeladen."),
SKYBLOCK_PARTY_ERSTELLT("§aDeine Skyblock-Party wurde erfolgreich erstellt!"),
SKYBLOCK_PARTY_NO_OWNER("§cDu bist nicht der Owner einer Party."),
SKYBLOCK_PARTY_EINLADEN("§aDu hast §b%§a zur Pary eingeladen."),
SKYBLOCK_PARTY_VERLASSEN("§cDu hast die Party verlassen."),
SKYBLOCK_PARTY_KICKEN("§cDu hast §b%§c aus der Party gekickt."),
SKYBLOCK_PARTY_SCHLIEßEN("§cDie Party wurde geschlossen."),
SKYBLOCK_PARTY_PLAYER_NOT("§cDieser Spieler ist nicht in der Party."),
SKYBLOCK_TELEPORT_HOME("§aDu wurdest zu deiner Insel teleportiert!"),
SKYBLOCK_PREFIX("§b■■■■■■■■ §6§lSkyBlock §b■■■■■■■■"),
SKYBLOCK_PARTY_PREFIX("§b■■■■■■■■ §6§lSkyBlock Party §b■■■■■■■■"),
SKYBLOCK_REMOVE_ISLAND_ONE("§cDu kannst nur einmal am Tag deine Insel löschen!"),
SIGN_SHOP_NO_ITEM_ON_INV("§cDu hast das Item nicht im Inventar oder du hast zu wenig von den Item's"),
SIGN_SHOP_VERKAUFT_("§6Du hast % mal %:% Verkauft und hast % Epic's erhalten."),
SIGN_SHOP_VERKAUFT("§6Du hast % mal % Verkauft und hast % Epic's erhalten."),
SIGN_SHOP_DELAY("§cDu kannst nur alle % sekunden was verkaufen!"),
SIGN_SHOP_GET("§6Du hast % mal % bekommen dir wurden % §bEpics §6abgezogen."),
SIGN_SHOP_GET_("§6Du hast % mal %:% bekommen dir wurden % §bEpics §6abgezogen."),
kFLY_ON("§aFly wurde aktiviert."),
kFLY_OFF("§cFly wurde deaktiviert."),
kFLY_PVP_FLAG("§cDu kannst nicht im PvP Bereich fliegen."),
SPAWN_SET("§aDer Spawn wurde gespeichert!"),
SPAWN_TELEPORT("§aDu wurdest zum Spawn Teleportiert!"),
GIVEALL("§c%§b hat jeden Spieler %x §a% §bgegeben."),
REPAIR_HAND("§cDas Item in deiner Hand wurde repariert!"),
REPAIR_ALL("§cDein Inventory wurde repariert!"),
FEED("§cDu hast nun kein Hunger mehr!"),
FEED_ALL("§cDer Spieler % hat dein Hunger gestillt!"),
FEED_OTHER("§aDu hast den Hunger von §b%§c gestillt!"),
HEAL("§cDu hast nun wieder volles Leben."),
HEAL_ALL("§aDer Spieler §b%§c hat nun wieder volles Leben."),
HEAL_OTHER("§aDu hast§b %'s§c geheilt."),
DAY("§aEs ist nun 4:55 Uhr."),
NIGHT("§aEs ist nun 18:34 Uhr."),
SUN("§aJetzt scheint die Sonne."),
USE_ENDERPEARL_TIME("§cDu kannst die Enderpearl in§b %§c wieder benutzten"),
USE_BEFEHL_TIME("§cDu kannst den Befehl in§b % §c wieder benutzten."),
NO_RANG("§cDu hast keine Rechte dazu"),
HOME_SET("§7Das Home §b%§7 wurde gespeichert!"),
HOME_EXIST("§cDas Home exestiert nicht."),
HOME_DEL("§cDas Home§b %§c wurde gelöscht"),
HOME_SKYBLOCK_DELETE("§7Die Homes von §b%§7 wurden entfernt."),
HOME_MAX("§cDu darfst nur §b%§c Homes setzten!"),
HOME_QUESTION("§aDer Spieler§b %§a fragt ob er ein Home setzten darf.§b /homeaccept or. /homedeny"),
HOME_ISLAND("§aEs wurde eine anfrage zum setzten des Homes abgesetzt."),
WARP_DEL("§aDer Warp§b %§a wurde gelöscht"),
WARP_EXIST("§cDer Warp exestiert nicht."),
WARP_SET("§aDas Home §b%§a wurde gespeichert!"),
KIT_USE("§cDu hast das Kit§b %§c benutzt."),
KIT_SET("§7Das Kit§b %§7 wurde gespeichert"),
KIT_EXIST("§cDas Kit exestiert nicht."),
KIT_DEL("§cDas Kit§b %§c wurde entfernt"),
KIT_DELAY("§cDu kannst das Kit erst in§b %§c wieder benutzten."),
CLEARINVENTORY("§cDein Inventory wurde geleert"),
CLEARINVENTORY_OTHER("§cDein Inventory wurde von §b%§c geleert."),
CLEARINVENTORY_FROM_OTHER("§cDu hast das Inventory von§b %§c geleert"),
CLEARINVENTORY_ALL("§c§4Du hast das Inventory von allen Spieler geleert."),
NO_ITEM_IN_HAND("§cDu hast kein Item in der Hand."),
ITEM_RENAME("§aDas Item wurde umbenannt"),
NO_ANSWER_PARTNER("§cDu hast niemanden zum antworten."),
SOCIALSPY_ON("§aDu hast Socialspy aktiviert."),
SOCIALSPY_OFF("§cDu hast Socialspy deaktiviert."),
MONEY_NO_DOUBLE("§cEntweder ist das keine Zahl oder 0."),
MONEY_ENOUGH_MONEY("§cDu hast nicht genug Epics."),
MONEY_SEND_TO("§aDu hast den Spieler §b% §7%§a Epics gesendet."),
MONEY_RECEIVE_FROM("§aDu hast von §b%§b %§a Epics erhalten."),
NO_PERMISSION("§cDu hast dafür keine Permission!");

@Setter
private String t;
private String[] tt;
private Text(String t){
	this.t=t;
}

private Text(String[] t){
	this.tt=t;
}

public String getText(){
	return this.t;
}

public String[] getTexts(Object s){
	for(int i = 0 ; i < tt.length ; i++){
		tt[i]=tt[i].replaceFirst("%",String.valueOf(s));
	}
	return tt;
}

public String[] getTextTexs(String[] s){
	for(int i = 0 ; i < tt.length ; i++){
		for(int i1 = 0 ; i1 < s.length ; i1++){
			if(s[i1]==null)continue;
			tt[i]=tt[i].replaceFirst("%", s[i1]);
			s[i1]=null;
		}
	}
	return tt;
}

public String[] getTexts(){
	return tt;
}

public String getText(Object s){
	if(this.t.contains("%"))return t.replaceAll("%", String.valueOf(s));
	return this.t;
}

public String getText(String[] s){
	String tt=this.t;
	for(int i = 0 ; i < s.length ; i++){
		tt=tt.replaceFirst("%", s[i]);
	}
	return tt;
}

	
}
