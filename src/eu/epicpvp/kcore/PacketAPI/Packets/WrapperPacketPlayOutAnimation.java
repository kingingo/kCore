package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.entity.Entity;

import eu.epicpvp.kcore.NPC.NPCAnimation;
import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;

public class WrapperPacketPlayOutAnimation implements PacketWrapper{
	@Getter
	private PacketPlayOutAnimation packet;
	private String ID = "a";
	private String ANIMATION = "b";
	
	public WrapperPacketPlayOutAnimation(){
		packet=new PacketPlayOutAnimation();
	}
	
	public WrapperPacketPlayOutAnimation(Entity entity,NPCAnimation animation){
		packet=new PacketPlayOutAnimation();
		
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
	public NPCAnimation getNPCAnimation(){
		return NPCAnimation.getId(((int)UtilReflection.getValue(ANIMATION, packet)));
	}
	
	public void setNPCAnimation(NPCAnimation animation){
		UtilReflection.setValue(ANIMATION, packet, animation.getId());
	}
	
}
