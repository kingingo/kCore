package me.kingingo.kcore.Kit;

import lombok.Getter;

public enum KitType {
SPEZIAL_KIT("§4Spezial-Kit"),
ADMIN("§cAdmin-Kit"),
STARTER("§7Starter-Kit"),
VIP("§aPremium-Kit"),
ULTRA("§aPremium-Kit"),
LEGEND("§aPremium-Kit"),
MVP("§aPremium-Kit"),
MVP_PLUS("§aPremium-Kit"),
KAUFEN("§6Kaufbares-Kit"),
KAUFEN_COINS("§6Kaufbares-Kit"),
KAUFEN_GEMS("§6Kaufbares-Kit");

@Getter
String name;
private KitType(String name){
	this.name=name;
}

}
