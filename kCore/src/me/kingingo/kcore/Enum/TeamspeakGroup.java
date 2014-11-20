package me.kingingo.kcore.Enum;

import lombok.Getter;

public enum TeamspeakGroup {

	OWNER(32,"Owner"),
	ADMIN(34,"Admin"),
	MODERATOR(35,"Moderator"),
	ARCHITEKT_LEITUNG(79,"Architekt-Leitung"),
	ARCHITEKT(37,"Architekt"),
	SUPPORTER(36,"Supporter"),
	PROBE_SUPPORTER(38,"Probe-Supporter"),
	PROBE_ARCHITEKT (39,"Probe-Architekt"),
	RUHE (45,"Ruhe"),
	NICHT_ANSTUPSBAR (56,"Nicht-Anstupsbar"),
	MITGLIED (53,"Mitglied"),
	VERTIFIZIERT(75,"Vertifiziert"),
	PREMIUM(41,"Premium"),
	YOUTUBER(43,"Youtuber"),
	TEAM_FREUND(42,"Teamfreund"),
	GEBURTSTAG(55,"Geburtstag"),
	BOT(47,"Bot"),
	TECHNIK(30,"Technik"),
	PTT (46,"Pit"),
	NICHT_ANSTUBSBART(69,"Nicht-Anstupsbar[T]"),
	DEVELOPER(33,"Developer");
	
	@Getter
	int id;
	@Getter
	String name;
	
	private TeamspeakGroup(int id,String name){
		this.id=id;
		this.name=name;
	}
	
	public static TeamspeakGroup get(int id){
		for(TeamspeakGroup g : TeamspeakGroup.values()){
			if(g.getId()==id)return g;
		}
		return null;
	}
	
}
