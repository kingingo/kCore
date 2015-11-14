package me.kingingo.kcore.Arena;

import lombok.Getter;

public enum RulePriority {
HIGHEST(0),
HIGH(1),
MEDIUM(2),
LOW(3),
LOWEST(4);

	@Getter
	private int i;
	private RulePriority(int i){
		this.i=i;
	}
}
