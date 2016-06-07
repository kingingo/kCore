package eu.epicpvp.kcore.Permission.Group;

public enum GroupTyp {
ALL("all"),
GUNGAME("gungame"),
GAME("game"),
PVP("pvp"),
SKY("sky"),
WARZ("warz"),
CREATIVE("creative");

public static GroupTyp get(String typ){
	GroupTyp per=null;
	for(GroupTyp type : GroupTyp.values()){
		if(type.getName().equalsIgnoreCase(typ)){
			per=type;
			break;
		}
	}
	return per;
}

private String name;
private GroupTyp(String name){
	this.name=name;
}

public String getName(){
	return this.name;
}

}
