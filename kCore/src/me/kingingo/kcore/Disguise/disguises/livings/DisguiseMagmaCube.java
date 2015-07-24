package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;

import org.bukkit.entity.EntityType;

public class DisguiseMagmaCube extends DisguiseSlime
{
  public DisguiseMagmaCube(org.bukkit.entity.Entity entity)
  {
    super(entity);
  }

  public kPacket GetSpawnPacket(){
	  kPacketPlayOutSpawnEntityLiving packet = new kPacketPlayOutSpawnEntityLiving();
	  packet.setEntityID(this.Entity.getId());
	  packet.setX(this.Entity.locX);
	  packet.setY(this.Entity.locY);
	  packet.setZ(this.Entity.locZ);
	  packet.setYaw(this.Entity.yaw);
	  packet.setPitch(this.Entity.pitch);
	  packet.setDataWatcher(this.DataWatcher);
	  packet.setEntityType(EntityType.MAGMA_CUBE);
	  packet.setEntityID(this.Entity.getId());
	  return packet;
  }
}