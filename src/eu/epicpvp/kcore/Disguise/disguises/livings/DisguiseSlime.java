package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseInsentient;
import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;

public class DisguiseSlime extends DisguiseInsentient
{
  public DisguiseSlime(org.bukkit.entity.Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, new Byte((byte)1));
  }

  public void SetSize(int i)
  {
    this.DataWatcher.watch(16, new Byte((byte)i));
  }

  public int GetSize()
  {
    return this.DataWatcher.getByte(16);
  }

  public EntityType GetEntityTypeId() {
	return EntityType.SLIME;
  }

  public kPacket GetSpawnPacket() {
	  kPacketPlayOutSpawnEntityLiving packet = new kPacketPlayOutSpawnEntityLiving();
	  packet.setEntityID(this.Entity.getId());
	  packet.setX(this.Entity.locX);
	  packet.setY(this.Entity.locY);
	  packet.setZ(this.Entity.locZ);
	  packet.setYaw(this.Entity.yaw);
	  packet.setPitch(this.Entity.pitch);
	  packet.setDataWatcher(this.DataWatcher);
	  packet.setEntityType(EntityType.SLIME);
	  packet.setEntityID(this.Entity.getId());
	  return packet;
  }

  protected String getHurtSound()
  {
    return "mob.slime." + (GetSize() > 1 ? "big" : "small");
  }

  protected float getVolume()
  {
    return 0.4F * GetSize();
  }

}