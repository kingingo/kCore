package eu.epicpvp.kcore.Disguise.disguises;
import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutSpawnEntityLiving;

public abstract class DisguiseCreature extends DisguiseInsentient
{
  public DisguiseCreature(org.bukkit.entity.Entity entity)
  {
    super(entity);
  }

  public PacketWrapper GetSpawnPacket(){
	  WrapperPacketPlayOutSpawnEntityLiving living = new WrapperPacketPlayOutSpawnEntityLiving();
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