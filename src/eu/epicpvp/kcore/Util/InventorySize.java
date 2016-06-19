package eu.epicpvp.kcore.Util;

import lombok.Getter;

public enum InventorySize {
_9(9,InventorySplit._9, new InventorySplit[]{InventorySplit._9}),
_18(18,InventorySplit._18, new InventorySplit[]{InventorySplit._9,InventorySplit._18}),
_27(27,InventorySplit._27, new InventorySplit[]{InventorySplit._9,InventorySplit._18,InventorySplit._27}),
_36(36,InventorySplit._36, new InventorySplit[]{InventorySplit._9,InventorySplit._18,InventorySplit._27,InventorySplit._36}),
_45(45,InventorySplit._45, new InventorySplit[]{InventorySplit._9,InventorySplit._18,InventorySplit._27,InventorySplit._36,InventorySplit._45}),
_54(54,InventorySplit._54, new InventorySplit[]{InventorySplit._9,InventorySplit._18,InventorySplit._27,InventorySplit._36,InventorySplit._45,InventorySplit._54});

@Getter
private int size;
@Getter
private InventorySplit lastLine;
@Getter
private InventorySplit[] lines;

private InventorySize(int size,InventorySplit lastLine,InventorySplit[] lines){
	this.size=size;
	this.lastLine=lastLine;
	this.lines=lines;
}

public static InventorySize invSize(int size){
	if(size<=9){
		return InventorySize._9;
	}else if(size<=18){
		return InventorySize._18;
	}else if(size<=27){
		return InventorySize._27;
	}else if(size<=36){
		return InventorySize._36;
	}else if(size<=45){
		return InventorySize._45;
	}
	return InventorySize._54;
}
	
}
