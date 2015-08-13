package me.kingingo.kcore.Enum;

import lombok.Getter;

public enum Zeichen {
HÄKCHEN_FETT("✔","[(=A=)]"),
HÄKCHEN("✓","[(=B=)]"),
HÄKCHEN_Angehaktes_Kästchen("☑","[(=C=)]"),
KÄTCHEN("☐","[(=D=)]"),
KÄTCHEN_BIG("■","[(=E=)]"),
MAHLZEICHEN("✕","[(=F=)]"),
MAHLZEICHEN_FETT("✖","[(=G=)]"),
Andreaskreuz("☓","[(=H=)]"),
WAHL_X("✗","[(=I=)]"),
WAHL_X_FETT("✘","[(=J=)]"),
HERZ("♥","[(=K=)]"),
BIG_HERZ("❤","[(=L=)]"),
ROTATE_HERZ("❥","[(=M=)]"),
DOPPEL_PFEIL("»","[(=N=)]");

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
