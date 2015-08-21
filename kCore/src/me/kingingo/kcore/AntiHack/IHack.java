package me.kingingo.kcore.AntiHack;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;

public class IHack extends kListener{

	@Getter
	private AntiHack antiHack;
	@Getter
	private HackType hackType;
	
	public IHack(AntiHack antiHack,String module) {
		super(antiHack.getInstance(), module);
		this.antiHack=antiHack;
	}
	
}
