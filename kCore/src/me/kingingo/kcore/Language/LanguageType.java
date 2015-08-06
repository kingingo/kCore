package me.kingingo.kcore.Language;

import lombok.Getter;

public enum LanguageType {
ITALIA("it"),
SPANISH("es"),
GERMANY("de"),
ENGLISH("en"),
NETHERLANDS("nl"),
FRANCE("fr");

@Getter
private String def;
private LanguageType(String def){
	this.def=def;
}

public static LanguageType get(String def){
	for(LanguageType type : LanguageType.values())if(type.getDef().equalsIgnoreCase(def))return type;
	return LanguageType.GERMANY;
}

}
