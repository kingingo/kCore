package eu.epicpvp.kcore.PacketAPI.Packets;

import org.apache.commons.lang.Validate;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;

public class WrapperPacketPlayInTabComplete implements PacketWrapper {

	@Getter
	@Setter
	private PacketPlayInTabComplete packet;
	private String CMD = "a";
	private String BLOCK_POSTION = "b";
	private String line;
	
	public WrapperPacketPlayInTabComplete(PacketPlayInTabComplete packet) {
		this.packet = packet;
	}

	public WrapperPacketPlayInTabComplete(Object packet) {
		this.packet = (PacketPlayInTabComplete) packet;
	}

	public WrapperPacketPlayInTabComplete() {
		packet = new PacketPlayInTabComplete();
	}

	public String getBlockPosition() {
		return (String) UtilReflection.getValue(BLOCK_POSTION, packet);
	}

	public void setBlockPosition(BlockPosition po) {
		UtilReflection.setValue(BLOCK_POSTION, packet, po);
	}

	public String getChatMessage() {
		if(line == null){
			line = (String) UtilReflection.getValue(CMD, packet);
			Validate.notNull(line,"Message cant be null!");
		}
		return line;
	}

	public void setChatMessage(@NonNull String cmd) {
		line = cmd;
		UtilReflection.setValue(CMD, packet, cmd);
	}

}
