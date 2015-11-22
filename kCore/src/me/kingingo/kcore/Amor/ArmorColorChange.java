package me.kingingo.kcore.Amor;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Color;

public class ArmorColorChange {

	@Setter
	@Getter
	private ArmorColorChangeType type;
	@Setter
	@Getter
	private Color[] color;
	@Setter
	@Getter
	private long time;
	
	public ArmorColorChange(ArmorColorChangeType type){
		this.type=type;
	}
	
}
