package me.kingingo.kcore.PacketAPI.v1_8_R2;

import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.Entity;

public class kDataWatcher extends DataWatcher{

	private int VISIBLE = 0;
	private int NAME = 2;
	private int CUSTOM_NAME_VISIBLE = 3;
	
	public kDataWatcher(Entity entity) {
		super(entity);
	}
	
	public kDataWatcher(){
		super(null);
//		setVisible(false);
//	    this.a(1, Short.valueOf((short)300));
//	    setCustomNameVisible(false);
//	    setCustomName("");
//	    this.a(4, Byte.valueOf((byte)0));
	}
	
	public void setVisible(boolean flag){
		this.a(VISIBLE, Byte.valueOf( ((byte) (flag ? 0 : Byte.valueOf((byte) 32)) ) )  );
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
