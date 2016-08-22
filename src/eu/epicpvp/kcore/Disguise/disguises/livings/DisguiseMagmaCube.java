package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutSpawnEntityLiving;

public class DisguiseMagmaCube extends DisguiseSlime
{
  public DisguiseMagmaCube(org.bukkit.entity.Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId() {
	return EntityType.MAGMA_CUBE;
  }

  public PacketWrapper GetSpawnPacket(){
	  WrapperPacketPlayOutSpawnEntityLiving packet = new WrapperPacketPlayOutSpawnEntityLiving();
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