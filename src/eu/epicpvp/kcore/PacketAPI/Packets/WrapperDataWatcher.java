package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import eu.epicpvp.kcore.Disguise.disguises.DummyEntity;
import net.minecraft.server.v1_8_R3.DataWatcher;

public class WrapperDataWatcher extends DataWatcher{

	private int VISIBLE = 0;
	private int AIR_TICKS = 1;
	private int NAME = 2;
	private int CUSTOM_NAME_VISIBLE = 3;
	private int KP = 4;
	
	public WrapperDataWatcher(Entity entity) {
		super( ((CraftEntity)entity).getHandle() );
	}
	
	public WrapperDataWatcher(net.minecraft.server.v1_8_R3.Entity entity) {
		super( entity );
	}
	
	public WrapperDataWatcher(World world){
		super( new DummyEntity(((CraftWorld)world).getHandle()) );
	}
	
	public WrapperDataWatcher() {
		super(null);
	}
	
	public void setVisible(boolean flag){
		this.a(VISIBLE, Byte.valueOf( ((byte) (flag ? 0 : Byte.valueOf((byte) 32)) ) )  );
	}
	
	public boolean isVisible() {
		return (this.getByte(VISIBLE)==0);
	}
	
	public void setAirTicks(int i) {
		this.a(AIR_TICKS, Short.valueOf((short)i));
	}

	public int getAirTicks() {
		return this.getShort(AIR_TICKS);
	}
	
	public void setCustomName(String s) {
		this.a(NAME, s);
	}

	public String getCustomName() {
		return this.getString(NAME);
	}

	public boolean hasCustomName() {
	    return this.getString(NAME).length() > 0;
	}

	public void setCustomNameVisible(boolean flag) {
	    this.a(CUSTOM_NAME_VISIBLE, Byte.valueOf((byte)(flag ? 1 : 0)));
	}

	public boolean getCustomNameVisible() {
	    return this.getByte(CUSTOM_NAME_VISIBLE) == 1;
	}
}
