package me.kingingo.kcore.PacketAPI;

import net.minecraft.server.v1_8_R2.MathHelper;

public class UtilPacket {

	public static int toFixedPoint(double d) {
		return MathHelper.floor(d * 32.0D);
	}
	   
	public static byte toPackedByte(float f) {
		return ((byte)(int)(f * 256.0F / 360.0F));
	}
	
}
