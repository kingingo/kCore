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
	private ArrayList<Permission> perms;
	
	public Group(String group,GroupTyp typ){
		this.group=group;
		this.typ=typ;
		this.perms=new ArrayList<>();
	}
	
	public void add(Permission perm){
		perms.add(perm);
	}
	
	public void del(Permission perm){
		perms.remove(perm);
	}
	
	public boolean is(Permission perm){
		return perms.contains(perm);
	}
	
}
