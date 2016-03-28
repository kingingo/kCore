package eu.epicpvp.kcore.Enum;

import lombok.Getter;

public enum Zeichen {
HÄKCHEN_FETT("âœ”","[(=A=)]"),
HÄKCHEN("âœ“","[(=B=)]"),
HÄKCHEN_Angehaktes_KÃ¤stchen("â˜‘","[(=C=)]"),
KÄTCHEN("â˜§","[(=D=)]"),
KÄTCHEN_BIG("â– ","[(=E=)]"),
MAHLZEICHEN("✖","[(=F=)]"),
MAHLZEICHEN_FETT("✖","[(=G=)]"),
Andreaskreuz("â˜“","[(=H=)]"),
WAHL_X("âœ—","[(=I=)]"),
WAHL_X_FETT("âœ˜","[(=J=)]"),
HERZ("❤","[(=K=)]"),
BIG_HERZ("❤","[(=L=)]"),
ROTATE_HERZ("â§¥","[(=M=)]"),
DOPPEL_PFEIL("Â»","[(=N=)]");

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
