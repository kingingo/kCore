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
	
}
