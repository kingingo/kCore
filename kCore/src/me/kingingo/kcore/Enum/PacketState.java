package me.kingingo.kcore.Enum;

public enum PacketState {
TRUE,
FALSE,
NOT_FOUND;

public static PacketState is(boolean b){
	if(b==true)return PacketState.TRUE;
	if(b==false)return PacketState.FALSE;
	return PacketState.NOT_FOUND;
}

}