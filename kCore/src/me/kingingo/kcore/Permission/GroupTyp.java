package me.kingingo.kcore.Permission;

public enum GroupTyp {
ALL("all"),
GAME("game"),
PVP("pvp"),
SKY("sky"),
GUNGAME("gungame"),
BUILD("build"),
WARZ("warz");

public static GroupTyp get(String perm){
	GroupTyp per=GroupTyp.PVP;
	for(GroupTyp permission : GroupTyp.values()){
		if(permission.getName().equalsIgnoreCase(perm)){
			per=permission;
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
