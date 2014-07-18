package me.kingingo.kcore.Enum;

import me.kingingo.kcore.Util.C;

public enum Text {
FIGHT_START_IN(C.cGray+"§eIhr könnt in§b % §eSekunden Kämpfen!"),
FIGHT_START(C.cGray+"§cIhr könnt nun Kämpfen!"),
GAME_END_IN(C.cGray+"§eDas Spiel endet in§b % §e"),
GAME_END(C.cGray+"§cDas Spiel wurde beendet."),
RESTART_IN(C.cGray+"§4Der Server Restartet in§b % §4Sekunden"),
RESTART(C.cGray+"§4§lDer Server Restartet Jetzt"),
SERVER_FULL(C.cRed+"§cDer Server ist voll!"),
SERVER_NOT_LOBBYPHASE(C.cRed+"§cDieser Server ist aktuell nicht in der Lobbyphase!"),
SERVER_FULL_WITH_PREMIUM("§cDer Server ist voll! Kaufe dir Premium auf §bshop.EpicPvP.de§c um auf vollen Servern zu joinen"),
KILL_BY("§b% §ewurde von§a % §egekillt"),
DEATH("§b% §eist gestorben."),
GAME_EXCLUSION(C.cRed+"% wurde vom Spiel ausgeschlossen"),
VOTE_TEAM_ADD("§cDu bist nun in§a %§c"),
VOTE_TEAM_REMOVE("§cDu hast §a %§c verlassen"),
VOTE_TEAM_FULL("§a%§c ist voll"),
VOTE_MIN("§eEs müssen mindestens §b% §eSpieler online sein damit das Spiel starten kann"),
KICKED_BY_PREMIUM(C.cRed+"§cDu wurdest vom Server gekickt damit ein Premium User spielen kann. Du möchtest auch? &bshop.EpicPvP.de"),
PREFIX_GAME(C.cDAqua+"§7[§6%§7]: "+C.cGray),
PREFIX(C.cDAqua+"§7[§6EpicPvP§7]: "+C.cGray),
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
TTT_DETECTIVE_SHOP("Du hast nicht genug Detective-Punkte!"),
TTT_TRAITOR_SHOP_BUYED("Du hast erfolgreich ein Traitor Item erworben!"),
TTT_DETECTIVE_SHOP_BUYED("Du hast erfolgreich ein Traitor Item erworben!"),
TTT_TRAITOR_SHOP_RADAR_CHANGE("Du hast den Spieler % ausgewählt!"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_DEATH("Bei diesem Spieler ist es Zuspät"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBT("Du hast erfolgreich % wiederbelebt"),
TTT_DETECTIVE_SHOP_DEFIBRILLATOR_WIEDERBELEBTER("Du wurdest von % wiederbelebt"),
TTT_TESTER_JOIN("% ist den Tester beigetretten.");

private String t;
private Text(String t){
	this.t=t;
}

public String getText(){
	return this.t;
}

public String getText(int s){
	if(this.t.contains("%"))return t.replaceAll("%", String.valueOf(s));
	return this.t;
}

public String getText(String s){
	if(this.t.contains("%"))return t.replaceAll("%", s);
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
