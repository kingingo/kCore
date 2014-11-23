package me.kingingo.kcore.Pet.kEntity;

import net.minecraft.server.v1_7_R4.EntityEnderDragon;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.Location;

public class kEnderdragon extends EntityEnderDragon{
public Location targetLoc = null;
	public kEnderdragon(World world) {
		super(world);
	}

	@Override
	public void e(){
		if(targetLoc!=null){
		    this.lastYaw = this.yaw = this.targetLoc.getYaw();
		    this.pitch = this.targetLoc.getPitch() * 0.5F;
		 
		    this.b(this.yaw+180, this.pitch+180);
		    this.aO = this.aM = this.yaw;
			this.locX=targetLoc.getX();
			this.locY=targetLoc.getY();
			this.locZ=targetLoc.getZ();
		}
	}
	 
}
