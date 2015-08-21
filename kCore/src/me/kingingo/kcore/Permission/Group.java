package me.kingingo.kcore.Permission;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Group {

	@Getter
	@Setter
	private String prefix="";
	@Getter
	private GroupTyp typ;
	@Getter
	private String group;
	@Getter
	private ArrayList<String> perms;
	
	public Group(String group,GroupTyp typ){
		this.group=group;
		this.typ=typ;
		this.perms=new ArrayList<>();
	}
	
	public void add(kPermission perm){
		add(perm.getPermissionToString());
	}
	
	public void add(String perm){
		if(!perms.contains(perm.toLowerCase()))perms.add(perm.toLowerCase());
	}
	
	public void del(String perm){
		perms.remove(perm.toLowerCase());
	}
	
	public void del(kPermission perm){
		del(perm.getPermissionToString());
	}
	
	public boolean is(kPermission perm){
		return perms.contains(perm);
	}
	
}
