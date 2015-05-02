package me.kingingo.kcore.AntiHack;

import lombok.Getter;

public enum Level {
HIGHEST(6),
HIGHER(5),
HIGH(4),
MEDIUM(3),
LOW(2),
LOWER(1),
LOWEST(0);

@Getter
private long level;
private Level(long level){
	this.level=level;
}
}
