package me.kingingo.kcore.LaunchItem;

import lombok.Getter;

public class LaunchItemEvent {
    
	@Getter
	private LaunchItem item;

    public LaunchItemEvent(LaunchItem item){
        this.item=item;
    }
}
