package me.kingingo.kcore.Friend;

import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

public class FriendManager {

	@Getter
	JavaPlugin instance;
	
	public FriendManager(JavaPlugin instance){
		this.instance=instance;
		new FriendListener(instance);
	}
	
}
