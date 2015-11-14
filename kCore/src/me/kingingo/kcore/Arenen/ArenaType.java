package me.kingingo.kcore.Arenen;

import lombok.Getter;
import me.kingingo.kcore.Enum.Team;

public enum ArenaType {
_TEAMx2(new Team[]{Team.RED,Team.BLUE}),
_TEAMx3(new Team[]{Team.RED,Team.BLUE,Team.GREEN}),
_TEAMx4(new Team[]{Team.RED,Team.BLUE,Team.GREEN,Team.YELLOW}),
_TEAMx5(new Team[]{Team.RED,Team.BLUE,Team.GREEN,Team.YELLOW,Team.ORANGE}),
_TEAMx6(new Team[]{Team.RED,Team.BLUE,Team.GREEN,Team.YELLOW,Team.ORANGE,Team.GRAY});

public static ArenaType withTeamAnzahl(int team_anzahl){
	for(ArenaType type : ArenaType.values()){
		if(type.getTeam().length==team_anzahl){
			return type;
		}
	}
	return null;
}

@Getter
private Team[] team;

private ArenaType(Team[] team){
	this.team=team;
}

public static ArenaType byInt(int i){
	for(ArenaType type : values())if(type.team.length==i)return type;
	return null;
}

}