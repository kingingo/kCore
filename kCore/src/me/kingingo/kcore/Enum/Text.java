package me.kingingo.kcore.Enum;

import me.kingingo.kcore.Util.C;

public enum Text {
PERK_SNEAKDAMAGE(new String[]{"§b§oWenn du schleichst erhältst du nur noch % Schaden"}),
PERK_NOFALLDAMAGE(new String[]{"§b§oDu erhältst keinen Fallschaden"}),
PERK_NOFIREDAMAGE(new String[]{"§b§oDu erhältst keinen Feuerschaden"}),
PERK_ARROWFIRE(new String[]{"§b§oDu erhältst eine Chance um % Prozent dass dein Pfeil brennt"}),
PERK_NODROPSBYDEATH(new String[]{"§b§oDu dropst keine Items wenn du stirbst"}),
PERK_POTIONBYDEATH(new String[]{"§b§oBeim Tod kriegt der Mörder den Effect %."}),
PERK_NOKNOCKBACK(new String[]{"§b§oDu erhältst keinen Rückstoß"}),
PERK_HEAL(new String[]{"§b§oDein Leben regeneriert um % Prozent schneller"}),
PERK_POISEN(new String[]{"§b§oDu hast eine % 'tige Prozent Chance das der Spieler dem du Schaden zufügst","§b§o % Sekunden lang vergiftet ist"}),
PERK_NOHUNGER(new String[]{"§b§oDu hungerst nie."}),
PERK_HEALBYHIT(new String[]{"§b§o% Prozent Chance ein % Herz zu bekommen wenn du deinen Gegner schlägst."}),
PERK_SPAWNBYDEATH(new String[]{"§b§oBeim Tot gibt es die Chance das ein % spawn."}),
PERK_PREMIUM(new String[]{"§b§oNach dem Respawnen hasts du 1 Minute lang extra Potions."}),
PERK_MOREHEALTH(new String[]{"§b§oImmer wenn du nur noch % Herzen hast erhält dein Geger denn % Prozent Schaden "}),
PERK_STOPPERK(new String[]{"§b§oSobald du jemanden schlägst ist sein Perk für % sekunden unbrauchbar."}),
PERK_DEATHDROPONLY(new String[]{"§b§oWenn du stirbst hast du % sekunden wo niemand deine Sachen aufheben kann."}),

CHAT_MUTE("Der Chat wurde gesperrt!"),
CHAT_UNMUTE("Der Chat wurde entsperrt!"),

KIT_SHOP_ADD("§eDu hast das Kit §3% §egewählt."),
KIT_SHOP_NO_MONEY("Du hast nicht genug §b%§e."),
KIT_SHOP_BUYED_KIT("Du hast erfolgreich das Kit§3 % 3Egekauft."),

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
DEATHMATCH_END_IN(C.cGray+"§cDas Deathmatch endet in §b%§e Sekunden"),
LOGIN_MESSAGE("§cBitte logge dich ein: §b/Login <Password>"),
KICK_BY_FALSE_PASSWORD("§cDu hast §b%§c mal dein Password falsch eingegeben"),
LOGIN_ACCEPT("§aDu hast dich Erfolgreich eingeloggt"),
LOGIN_DENY("§cDas Password ist falsch"),
REGISTER_ACCEPT("§aRegister Erfolgreich!"),
REGISTER_MESSAGE("§cRegistriere dich bitte: §b/Register <Password>"),
SPECTATOR_CHAT_CANCEL("§cDu kannst als Spectator nicht im Chat schreiben."),
TTT_WIN("Die % haben diese Runde gewonnen!"),
TTT_DEATH("Der Spieler % ist gestorben und war %."),
TTT_TESTER_WAS_USED("Der Tester ist im moment nicht Freigegeben."),
TTT_TESTER_USED("Der Tester ist im moment nicht Frei."),
TTT_TESTER_TIME("Du warst schon in Tester kommer später wieder."),
TTT_TRAITOR_SHOP("Du hast nicht genug Traitor-Punkte!"),
TTT_PÄSSE_USE("Du hast einen Traitor Pass benutzt du hast jetzt noch %!"),
TTT_PÄSSE_KEINE("Du besitzt keine Traitor Pässe!"),
TTT_PÄSSE_LOBBYPHASE("Die LobbyPhase ist vorbei..."),
TTT_PÄSSE_MAX_USED("Es wurden schon zu viele Traitor Pässe benutzt"),
TTT_SHOP("Du hast nicht genug Punkte!"),
TTT_SHOP_BUYED("Du hast erfolgreich ein Item erworben!"),
TTT_NPC_CLICKED("Das ist % er war ein %."),
TTT_TRAITOR_SHOP_RADAR_CHANGE("Du hast den Spieler % ausgewählt!"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH("Bei diesem Spieler ist es Zuspät"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT("Du hast erfolgreich % wiederbelebt"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER("Du wurdest von % wiederbelebt"),
TTT_TESTER_JOIN("% ist den Tester beigetretten."),
TOKENS_ADD("§eDu hast §b%§e Tokens erhalten."),
TOKENS_DEL("§eDir wurden §c% §eTokens abgezogen."),
COINS_ADD("§eDu hast §b%§e Coins erhalten."),
COINS_DEL("§eDir wurden §c% §eCoins abgezogen."),
GAME_AUSGESCHIEDEN("% wurde aussem Spiel ausgeschlossen!"),
SHEEPWARS_SHEEP_DEATH("§eDas Schaf vom Team %§e wurde von§b % §egetötet!");

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

public String[] getTexts(String[] s){
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

//public String getText(int s){
//	if(this.t.contains("%"))return t.replaceAll("%", String.valueOf(s));
//	return this.t;
//}
//
//public String getText(String s){
//	if(this.t.contains("%"))return t.replaceAll("%", s);
//	return this.t;
//}

public String getText(String[] s){
	String tt=this.t;
	for(int i = 0 ; i < s.length ; i++){
		tt=tt.replaceFirst("%", s[i]);
	}
	return tt;
}

	
}
