package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperArmorStandDataWatcher extends WrapperDataWatcher{
	
	public WrapperArmorStandDataWatcher(Entity entity) {
		super(entity);
		initialize();
	}
	
	public WrapperArmorStandDataWatcher(World world){
		super(world);
		initialize();
	}
	
	public WrapperArmorStandDataWatcher(){
		initialize();
	}
	
	public void initialize(){
	    this.a(10, Byte.valueOf((byte)0));
	}
	
	public boolean isSmall() {
	    return (this.getByte(10) & 0x1) != 0;
	}
	
	public void setSmall(boolean flag) {
	    byte b0 = this.getByte(10);

	    if (flag)
	      b0 = (byte)(b0 | 0x1);
	    else {
	      b0 = (byte)(b0 & 0xFFFFFFFE);
	    }

	    
	    this.watch(10, Byte.valueOf(b0));
	}
	
	public void setBasePlate(boolean flag) {
	    byte b0 = this.getByte(10);

	    if (flag)
	    	b0 = (byte)(b0 & 0xFFFFFFF7);
	    else {
	    	b0 = (byte)(b0 | 0x8);
	    }

	    this.watch(10, Byte.valueOf(b0));
	}
	
	public boolean hasBasePlate() {
		return (this.getByte(10) & 0x8) != 0;
	}
	
	public void setGravity(boolean flag) {
	    byte b0 = this.getByte(10);

	    if (flag)
	      b0 = (byte)(b0 | 0x2);
	    else {
	      b0 = (byte)(b0 & 0xFFFFFFFD);
	    }

	    this.watch(10, Byte.valueOf(b0));
	}

	public boolean hasGravity() {
		return (this.getByte(10) & 0x2) != 0;
	}
}
