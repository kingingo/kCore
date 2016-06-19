package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class kPacketPlayOutChat implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutChat packet;
	private String ICHAT = "a";
	private String TYPE = "b";
	
	public kPacketPlayOutChat(){
		packet=new PacketPlayOutChat();
	}
	

	public kPacketPlayOutChat(String message){
		this(message,ChatMode.CHAT);
	}
	
	public kPacketPlayOutChat(String message,ChatMode mode){
		this();
		setIChatBaseComponent(new ChatComponentText(message));
		setType(mode.getMode());
	}
	
	public IChatBaseComponent getIChatBaseComponent(){
		return (IChatBaseComponent)UtilReflection.getValue(ICHAT, packet);
	}
	
	public void setIChatBaseComponent(IChatBaseComponent base){
		UtilReflection.setValue(ICHAT, packet, base);
	}
	
	public void setType(byte b){
		UtilReflection.setValue(TYPE, packet, b);
	}
	
	public void setType(ChatMode m){
		setType(m.getMode());
	}
	
	//0: chat (chat box), 1: system message (chat box), 2: above hotbar
	public static enum ChatMode{
		SYSTEM_MSG((byte)1),
		CHAT((byte)0),
		HOVBAR((byte)2);
		
		@Getter
		private byte mode;
		
		private ChatMode(byte b){
			this.mode=b;
		}
    }
}
