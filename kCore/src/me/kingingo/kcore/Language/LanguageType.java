package me.kingingo.kcore.Language;

import lombok.Getter;

public enum LanguageType {
GERMAN("GERMAN"),
ENGLISH("ENGLISH");

@Getter
private String def;
private LanguageType(String def){
	this.def=def;
}

public static LanguageType get(String def){
	for(LanguageType type : LanguageType.values())if(type.getDef().equalsIgnoreCase(def))return type;
	return LanguageType.GERMAN;
}

public static LanguageType getByMCLanguage(String mcversion){
	for(LanguageType type : LanguageType.values())if(mcversion.startsWith(type.getDef()))return type;
	return LanguageType.ENGLISH;
}

}
