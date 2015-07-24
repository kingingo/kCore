package me.kingingo.kcore.Disguise.disguises;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;

import org.bukkit.entity.EntityType;

public abstract class DisguiseCreature extends DisguiseInsentient
{
  public DisguiseCreature(org.bukkit.entity.Entity entity)
  {
    super(entity);
  }

  protected abstract EntityType GetEntityTypeId();

  public kPacket GetSpawnPacket(){
	  kPacketPlayOutSpawnEntityLiving living = new kPacketPlayOutSpawnEntityLiving();
	  living.setX(this.Entity.locX);
	  living.setY(this.Entity.locY);
	  living.setZ(this.Entity.locZ);
	  living.setYaw(this.Entity.yaw);
	  living.setPitch(this.Entity.pitch);
	  living.setDataWatcher(this.DataWatcher);
	  living.setEntityID(this.GetEntityId());
	  living.setEntityType(this.GetEntityTypeId());
	  return living;
  }
}