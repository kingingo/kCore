package eu.epicpvp.kcore.Permission.Group;

public enum GroupTyp {
ALL("all"),
GUNGAME("gungame"),
GAME("game"),
PVP("pvp"),
SKY("sky"),
WARZ("warz");

public static GroupTyp get(String perm){
	GroupTyp per=null;
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
