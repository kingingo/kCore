package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import me.kingingo.kcore.NPC.NPCAnimation;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;

import org.bukkit.entity.Entity;

public class kPacketPlayOutAnimation implements kPacket{
	@Getter
	private PacketPlayOutAnimation packet;
	private String ID = "a";
	private String ANIMATION = "b";
	
	public kPacketPlayOutAnimation(){
		packet=new PacketPlayOutAnimation();
	}
	
	public kPacketPlayOutAnimation(Entity entity,NPCAnimation animation){
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
