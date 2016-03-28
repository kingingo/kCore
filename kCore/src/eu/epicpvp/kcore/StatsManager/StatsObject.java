package eu.epicpvp.kcore.StatsManager;

import lombok.Getter;

public class StatsObject {

	@Getter
	private Object value;
	@Getter
	private Object change;
	
	public StatsObject(Object value){
		this.value=value;
	}
	
	public void add(Object obj){
		if(value instanceof Integer){
			if(change==null)change=0;
			change = ((int)change)+(int)obj;
			value = ((int)value)+(int)obj;
		}else if(value instanceof Double){
			if(change==null)change=0;
			change = ((double)change)+(double)obj;
			value = ((double)value)+(double)obj;
		}else if(value instanceof Long){
			if(change==null)change=0;
			change = ((long)change)+(long)obj;
			value = ((long)value)+(long)obj;
		}else if(value instanceof String){
			change = obj;
			value = obj;
		}
	}
}
