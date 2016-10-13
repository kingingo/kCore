package eu.epicpvp.kcore.Permission;

import eu.epicpvp.datenserver.definitions.permissions.GroupTyp;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(exclude = "starIndex")
public class Permission {
	private String permission;
	private GroupTyp group;
	private int starIndex = -1;
	
	
	public boolean acceptPermission(String perm){
		if(starIndex != -1){
			return perm.substring(0,Math.min(starIndex, perm.length())).equalsIgnoreCase(permission.substring(0,starIndex));
		}
		return permission.equalsIgnoreCase(perm);
	}
	
	public String getPermissionToString(){
		return permission;
	}

	public String getRawPermission(){
		return permission.startsWith("-") ? permission.substring(1) : permission;
	}
	
	public boolean isNegative(){
		return permission.startsWith("-");
	}
	
	public Permission(String permission, GroupTyp group) {
		this.permission = permission;
		this.group = group;
		starIndex = permission.indexOf("*");
	}
}
