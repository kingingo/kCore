package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.Permission.Permission;

public enum TeamspeakGroup {

	OWNER(25,"Owner",Permission.TEAMSPEAK_OWNER),
	DEV_OWNER(26,"Dev/Owner",Permission.TEAMSPEAK_DEV_OWNER),
	ADMIN(28,"Admin",Permission.TEAMSPEAK_ADMIN),
	TEAM_LEITER(29,"Team-Leiter",Permission.TEAMSPEAK_TEAM_LEITER),
	SMOD(31,"sMod",Permission.TEAMSPEAK_SMOD),
	MOD(32,"Mod",Permission.TEAMSPEAK_MOD),
	ARCHITEKT_LEITER(30,"Architekt-Leiter",Permission.TEAMSPEAK_ARCHITEKT_LEITER),
	ARCHITEKT(33,"Architekt",Permission.TEAMSPEAK_ARCHITEKT),
	HELFER(34,"Helfer",Permission.TEAMSPEAK_HELFER),
	PROBE_ARCHITEKT (35,"Probe-Architekt",Permission.TEAMSPEAK_PROBE_ARCHITEKT),
	MITGLIED (9,"Mitglied",Permission.TEAMSPEAK_MITGLIED),
	PREMIUM(10,"Premium",Permission.TEAMSPEAK_PREMIUM),
	YOUTUBER(12,"Youtuber",Permission.TEAMSPEAK_YOUTUBER),
	TEAM_FREUND(13,"Teamfreund",Permission.TEAMSPEAK_TEAM_FREUND),
	GEBURTSTAG(17,"Geburtstag",Permission.TEAMSPEAK_GEBURTSTAG),
	BOT(18,"Bot",Permission.TEAMSPEAK_BOT),
	NICHT_ANSTUPSBAR(20,"Nicht-Anstupsbar",Permission.TEAMSPEAK_NICHT_ANSTUPSBAR),
	GARNICHT_ANSTUPSBAR(39,"Garnicht-Anstupsbar",Permission.TEAMSPEAK_GARNICHT_ANSTUPSBAR),
	NICHT_REDEN(15,"Nicht-Reden",Permission.TEAMSPEAK_NICHT_REDEN),
	NICHT_BEWEGEN(16,"Nicht-Bewegen",Permission.TEAMSPEAK_NICHT_BEWEGEN),
	NICHT_ANSCHREIBBAR(21,"Nicht-Anschreibar",Permission.TEAMSPEAK_NICHT_ANSCHREIBBAR),
	NICHT_ANSTUPSBART(23,"Nicht-Anstupsbar[T]",Permission.TEAMSPEAK_NICHT_ANSTUPSBART);
	
	@Getter
	int id;
	@Getter
	String name;
	@Getter
	Permission perm;
	
	private TeamspeakGroup(int id,String name,Permission perm){
		this.id=id;
		this.name=name;
		this.perm=perm;
	}
	
	public static TeamspeakGroup get(int id){
		for(TeamspeakGroup g : TeamspeakGroup.values()){
			if(g.getId()==id)return g;
		}
		return null;
	}
	
}
