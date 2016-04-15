package eu.epicpvp.kcore.Enum;

import lombok.Getter;

public enum Zeichen {
	DOUBLE_ARROWS_l("«"),
	DOUBLE_ARROWS_R("»"),
	MAHLZEICHEN("✖"),
	MAHLZEICHEN_FETT("✖"),
	HERZ("❤"),
	BIG_HERZ("❤");

	@Getter
	private String icon;
	
	private Zeichen(String icon){
		this.icon=icon;
	}
}
