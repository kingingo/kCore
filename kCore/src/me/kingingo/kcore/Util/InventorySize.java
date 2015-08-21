package me.kingingo.kcore.Util;

public enum InventorySize {
_9(9),
_18(18),
_27(27),
_36(36),
_45(45);
	
int i;
private InventorySize(int i){
this.i=i;
}

public int getSize(){
	return this.i;
}

public static InventorySize itemSize(int size){
	if(size<=9){
		return InventorySize._9;
	}else if(size<=18){
		return InventorySize._18;
	}else if(size<=27){
		return InventorySize._27;
	}else if(size<=36){
		return InventorySize._36;
	}
	return InventorySize._45;
}
	
}
