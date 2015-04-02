package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.Permission.kPermission;

public enum TeamspeakGroup {

	QUERY(2,"Query",kPermission.TEAMSPEAK_QUERY),
	OWNER(31,"Owner",kPermission.TEAMSPEAK_OWNER),
	DEV_OWNER(30,"Dev/Owner",kPermission.TEAMSPEAK_DEV_OWNER),
	ADMIN(33,"Admin",kPermission.TEAMSPEAK_ADMIN),
	PVP_ADMIN(29,"PvP-Admin",kPermission.TEAMSPEAK_PVP_ADMIN),
	TEAM_LEITER(28,"Team-Leiter",kPermission.TEAMSPEAK_TEAM_LEITER),
	SMOD(27,"sMod",kPermission.TEAMSPEAK_SMOD),
	MOD(26,"Mod",kPermission.TEAMSPEAK_MOD),
	FORUM_MOD(25,"Forum-Mod",kPermission.TEAMSPEAK_FOURM_MOD),
	ARCHITEKT(24,"Architekt",kPermission.TEAMSPEAK_ARCHITEKT),
	HELFER(23,"Helfer",kPermission.TEAMSPEAK_HELFER),
	PROBE_ARCHITEKT (22,"Probe-Architekt",kPermission.TEAMSPEAK_PROBE_ARCHITEKT),
	MITGLIED (8,"Mitglied",kPermission.TEAMSPEAK_MITGLIED),
	PREMIUM(13,"Premium",kPermission.TEAMSPEAK_PREMIUM),
	YOUTUBER(12,"Youtuber",kPermission.TEAMSPEAK_YOUTUBER),
	TEAM_FREUND(14,"Teamfreund",kPermission.TEAMSPEAK_TEAM_FREUND),
	GEBURTSTAG(19,"Geburtstag",kPermission.TEAMSPEAK_GEBURTSTAG),
	BOT(20,"Bot",kPermission.TEAMSPEAK_BOT),
	VERIFIZIERT(46,"Verifiziert",kPermission.TEAMSPEAK_VERIFIZIERT),
	AUFNAHME(37,"Aufnahme",kPermission.TEAMSPEAK_AUFNAHME),
	NICHT_ANSTUPSBAR(35,"Nicht-Anstupsbar",kPermission.TEAMSPEAK_NICHT_ANSTUPSBAR),
	NICHT_ANSCHREIBBAR(36,"Nicht-Anschreibar",kPermission.TEAMSPEAK_NICHT_ANSCHREIBBAR);
	
	@Getter
	int id;
	@Getter
	String name;
	@Getter
	kPermission perm;
	
	private TeamspeakGroup(int id,String name,kPermission perm){
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
