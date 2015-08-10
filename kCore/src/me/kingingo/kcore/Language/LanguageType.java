package me.kingingo.kcore.Language;

import lombok.Getter;

public enum LanguageType {
GERMANY("de"),
ENGLISH("en");

@Getter
private String def;
private LanguageType(String def){
	this.def=def;
}

public static LanguageType get(String def){
	for(LanguageType type : LanguageType.values())if(type.getDef().equalsIgnoreCase(def))return type;
	return LanguageType.GERMANY;
}

public static LanguageType getByMCLanguage(String mcversion){
	for(LanguageType type : LanguageType.values())if(mcversion.startsWith(type.getDef()))return type;
	return LanguageType.ENGLISH;
}

}
