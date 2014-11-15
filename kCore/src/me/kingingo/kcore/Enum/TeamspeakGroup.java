package me.kingingo.kcore.Enum;

import lombok.Getter;

public enum TeamspeakGroup {

	OWNER(32),
	ADMIN(34),
	MODERATOR(35),
	ARCHITEKT_LEITUNG(79),
	ARCHITEKT(37),
	SUPPORTER(36),
	PROBE_SUPPORTER(38),
	PROBE_ARCHITEKT (39),
	RUHE (45),
	NICHT_ANSTUPSBAR (56),
	MITGLIED (53),
	VERTIFIZIERT(75),
	PREMIUM(41),
	YOUTUBER(43),
	TEAM_FREUND(42),
	GEBURTSTAG(55),
	BOT(47),
	TECHNIK(30),
	PTT (46),
	NICHT_ANSTUBSBART(69),
	DEVELOPER(33);
	
	@Getter
	int id;
	
	private TeamspeakGroup(int id){
		this.id=id;
	}
	
}
