package eu.epicpvp.kcore.Translation;

import lombok.Getter;

public enum Language {
DUTCH("nl"),
ITALIAN("it"),
JAPANESE("ja"),
SPANISH("es-Es"),
GERMAN("de"),
ENGLISH("en"),
FRENCH("fr"),
TURKISH("tr");
	
	@Getter
	private String folder;
	
	private Language(String folder){
		this.folder=folder;
	}
	
}
