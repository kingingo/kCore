package eu.epicpvp.kcore.Listener.FarmBoosterListener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class FarmWorld {

	private World world;
	private int cactusModifier;
	private int caneModifier;
	private int melonModifier;
	private int mushroomModifier;
	private int pumpkinModifier;
	private int saplingModifier;
	private int wheatModifier;
	private int wartModifier;
	
	public FarmWorld(World world){
		this.world=world;
		
		this.cactusModifier=((CraftWorld)world).getHandle().spigotConfig.cactusModifier;
		this.caneModifier=((CraftWorld)world).getHandle().spigotConfig.caneModifier;
		this.melonModifier=((CraftWorld)world).getHandle().spigotConfig.melonModifier;
		this.mushroomModifier=((CraftWorld)world).getHandle().spigotConfig.mushroomModifier;
		this.pumpkinModifier=((CraftWorld)world).getHandle().spigotConfig.pumpkinModifier;
		this.saplingModifier=((CraftWorld)world).getHandle().spigotConfig.saplingModifier;
		this.wheatModifier=((CraftWorld)world).getHandle().spigotConfig.wheatModifier;
		this.wartModifier=((CraftWorld)world).getHandle().spigotConfig.wartModifier;
	}
	
	public void setModifer(int i){
		((CraftWorld)world).getHandle().spigotConfig.cactusModifier = cactusModifier*i;
		((CraftWorld)world).getHandle().spigotConfig.caneModifier = caneModifier*i;
		((CraftWorld)world).getHandle().spigotConfig.melonModifier = melonModifier*i;
		((CraftWorld)world).getHandle().spigotConfig.mushroomModifier = mushroomModifier*i;
		((CraftWorld)world).getHandle().spigotConfig.pumpkinModifier = pumpkinModifier*i;
		((CraftWorld)world).getHandle().spigotConfig.saplingModifier = saplingModifier*i;
		((CraftWorld)world).getHandle().spigotConfig.wheatModifier = wheatModifier*i;
		((CraftWorld)world).getHandle().spigotConfig.wartModifier = wartModifier*i;
	}
	
}
