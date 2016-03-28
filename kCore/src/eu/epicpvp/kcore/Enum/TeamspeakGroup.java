package eu.epicpvp.kcore.Enum;

import eu.epicpvp.kcore.Permission.PermissionType;
import lombok.Getter;

public enum TeamspeakGroup {

	QUERY(2,"Query",PermissionType.TEAMSPEAK_QUERY),
	OWNER(31,"Owner",PermissionType.TEAMSPEAK_OWNER),
	DEV_OWNER(30,"Dev/Owner",PermissionType.TEAMSPEAK_DEV_OWNER),
	ADMIN(33,"Admin",PermissionType.TEAMSPEAK_ADMIN),
	PVP_ADMIN(29,"PvP-Admin",PermissionType.TEAMSPEAK_PVP_ADMIN),
	TEAM_LEITER(28,"Team-Leiter",PermissionType.TEAMSPEAK_TEAM_LEITER),
	SMOD(27,"sMod",PermissionType.TEAMSPEAK_SMOD),
	MOD(26,"Mod",PermissionType.TEAMSPEAK_MOD),
	FORUM_MOD(25,"Forum-Mod",PermissionType.TEAMSPEAK_FOURM_MOD),
	ARCHITEKT(24,"Architekt",PermissionType.TEAMSPEAK_ARCHITEKT),
	HELFER(23,"Helfer",PermissionType.TEAMSPEAK_HELFER),
	PROBE_ARCHITEKT (22,"Probe-Architekt",PermissionType.TEAMSPEAK_PROBE_ARCHITEKT),
	MITGLIED (8,"Mitglied",PermissionType.TEAMSPEAK_MITGLIED),
	PREMIUM(13,"Premium",PermissionType.TEAMSPEAK_PREMIUM),
	YOUTUBER(12,"Youtuber",PermissionType.TEAMSPEAK_YOUTUBER),
	TEAM_FREUND(14,"Teamfreund",PermissionType.TEAMSPEAK_TEAM_FREUND),
	GEBURTSTAG(19,"Geburtstag",PermissionType.TEAMSPEAK_GEBURTSTAG),
	BOT(20,"Bot",PermissionType.TEAMSPEAK_BOT),
	VERIFIZIERT(46,"Verifiziert",PermissionType.TEAMSPEAK_VERIFIZIERT),
	AUFNAHME(37,"Aufnahme",PermissionType.TEAMSPEAK_AUFNAHME),
	NICHT_ANSTUPSBAR(35,"Nicht-Anstupsbar",PermissionType.TEAMSPEAK_NICHT_ANSTUPSBAR),
	NICHT_ANSCHREIBBAR(36,"Nicht-Anschreibar",PermissionType.TEAMSPEAK_NICHT_ANSCHREIBBAR);
	
	@Getter
	int id;
	@Getter
	String name;
	@Getter
	PermissionType perm;
	
	private TeamspeakGroup(int id,String name,PermissionType perm){
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
