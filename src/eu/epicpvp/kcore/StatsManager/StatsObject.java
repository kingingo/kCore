package eu.epicpvp.kcore.StatsManager;

import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.nbt.NBTTagCompound;
import lombok.Getter;

public class StatsObject {

	@Getter
	private Object value;
	@Getter
	private Object change;

	public StatsObject(Object value) {
		this.value = value;
	}
	
	public void reset(){
		this.change=null;
	}

	public void add(Object obj) {
		if (value instanceof Integer) {
			if (change == null)
				change = 0;
			change =  UtilNumber.toInt(change) + UtilNumber.toInt(obj);
			value = UtilNumber.toInt(value) + UtilNumber.toInt(obj);
		} else if (value instanceof Double) {
			if (change == null) change = 0;
			change = UtilNumber.toDouble(change) + UtilNumber.toDouble(obj);
			value = UtilNumber.toDouble(value) + UtilNumber.toDouble(obj);
		} else if (value instanceof Long) {
			if (change == null)
				change = 0;
			change = UtilNumber.toLong(change) + UtilNumber.toLong(obj);
			value = UtilNumber.toLong(value) + UtilNumber.toLong(obj);
		} else if (value instanceof String) {
			change = obj;
			value = obj;
		}else if (value instanceof NBTTagCompound) {
			change = obj;
			value = obj;
		}
	}
}
