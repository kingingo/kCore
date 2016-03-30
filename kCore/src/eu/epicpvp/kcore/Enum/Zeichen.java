package eu.epicpvp.kcore.Enum;

import lombok.Getter;

public enum Zeichen {
MAHLZEICHEN("âœ–","[(=F=)]"),
MAHLZEICHEN_FETT("âœ–","[(=G=)]"),
HERZ("â�¤","[(=K=)]"),
BIG_HERZ("â�¤","[(=L=)]");

@Getter
private String icon;
@Getter
private String placed;

private Zeichen(String icon,String placed){
	this.placed=placed;
	this.icon=icon;
}

public static String replacePlaceWithIcon(String s){
	for(Zeichen zeichen : Zeichen.values())s=s.replaceAll(zeichen.getPlaced(), zeichen.getIcon());
	return s;
}

public static String replaceIconWithPlace(String s){
	for(Zeichen zeichen : Zeichen.values())s=s.replaceAll(zeichen.getIcon(),zeichen.getPlaced());
	return s;
}

}
