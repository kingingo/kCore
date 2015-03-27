package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.Permission.Permission;

public enum TeamspeakGroup {

	QUERY(2,"Query",Permission.TEAMSPEAK_QUERY),
	OWNER(31,"Owner",Permission.TEAMSPEAK_OWNER),
	DEV_OWNER(30,"Dev/Owner",Permission.TEAMSPEAK_DEV_OWNER),
	ADMIN(33,"Admin",Permission.TEAMSPEAK_ADMIN),
	PVP_ADMIN(29,"PvP-Admin",Permission.TEAMSPEAK_PVP_ADMIN),
	TEAM_LEITER(28,"Team-Leiter",Permission.TEAMSPEAK_TEAM_LEITER),
	SMOD(27,"sMod",Permission.TEAMSPEAK_SMOD),
	MOD(26,"Mod",Permission.TEAMSPEAK_MOD),
	FORUM_MOD(25,"Forum-Mod",Permission.TEAMSPEAK_FOURM_MOD),
	ARCHITEKT(24,"Architekt",Permission.TEAMSPEAK_ARCHITEKT),
	HELFER(23,"Helfer",Permission.TEAMSPEAK_HELFER),
	PROBE_ARCHITEKT (22,"Probe-Architekt",Permission.TEAMSPEAK_PROBE_ARCHITEKT),
	MITGLIED (8,"Mitglied",Permission.TEAMSPEAK_MITGLIED),
	PREMIUM(13,"Premium",Permission.TEAMSPEAK_PREMIUM),
	YOUTUBER(12,"Youtuber",Permission.TEAMSPEAK_YOUTUBER),
	TEAM_FREUND(14,"Teamfreund",Permission.TEAMSPEAK_TEAM_FREUND),
	GEBURTSTAG(19,"Geburtstag",Permission.TEAMSPEAK_GEBURTSTAG),
	BOT(20,"Bot",Permission.TEAMSPEAK_BOT),
	VERIFIZIERT(46,"Verifiziert",Permission.TEAMSPEAK_VERIFIZIERT),
	AUFNAHME(37,"Aufnahme",Permission.TEAMSPEAK_AUFNAHME),
	NICHT_ANSTUPSBAR(35,"Nicht-Anstupsbar",Permission.TEAMSPEAK_NICHT_ANSTUPSBAR),
	NICHT_ANSCHREIBBAR(36,"Nicht-Anschreibar",Permission.TEAMSPEAK_NICHT_ANSCHREIBBAR);
	
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
