package me.kingingo.kcore.Kit;

import lombok.Getter;

public enum KitType {
SPEZIAL_KIT("§4Spezial-Kit"),
ADMIN("§cAdmin-Kit"),
STARTER("§7Starter-Kit"),
PREMIUM("§aPremium-Kit"),
KAUFEN("§6Kaufbares-Kit");

@Getter
String name;
private KitType(String name){
	this.name=name;
}

}
