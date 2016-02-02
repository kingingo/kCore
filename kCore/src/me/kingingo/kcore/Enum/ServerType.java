package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Permission.GroupTyp;

public enum ServerType {
GUNGAME("gungame",GroupTyp.GUNGAME,GildenType.PVP,GameType.GUNGAME),
PVP("pvp",GroupTyp.PVP,GildenType.PVP,GameType.PVP),
WARZ("warz",GroupTyp.WARZ,GildenType.WARZ,GameType.WARZ),
SKYBLOCK("skyblock",GroupTyp.SKY,GildenType.SKY,GameType.SKYBLOCK),
BUNGEECORD("bungeecord",GroupTyp.ALL,null,null),
GAME("game",GroupTyp.GAME,null,null),
ALL("all",GroupTyp.ALL,null,null);

@Getter
private String name;
@Getter
private GroupTyp groupType;
@Getter
private GildenType gildenType;
@Getter
private GameType gameType;

private ServerType(String name,GroupTyp groupType,GildenType gildenType,GameType gameType){
	this.name=name;
	this.groupType=groupType;
	this.gildenType=gildenType;
	this.gameType=gameType;
}

public static ServerType get(String name){
	for(ServerType type : ServerType.values()){
		if(type.getName().equalsIgnoreCase(name))return type;
	}
	return null;
}

}
