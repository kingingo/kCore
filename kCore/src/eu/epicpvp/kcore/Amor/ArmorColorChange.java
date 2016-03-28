package eu.epicpvp.kcore.Amor;

import org.bukkit.Color;

import lombok.Getter;
import lombok.Setter;

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
