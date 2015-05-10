package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseInsentient;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutEntityLiving;
import net.minecraft.server.v1_8_R2.Packet;

import org.bukkit.entity.EntityType;

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

  public Packet GetSpawnPacket()
  {
//	WrapperPlayServerSpawnEntityLiving pa = new WrapperPlayServerSpawnEntityLiving();
//	pa.setEntityID(this.Entity.getId());
//	pa.setZ(this.Entity.locZ);
//	pa.setX(this.Entity.locX);
//	pa.setY(this.Entity.locY);
//	pa.setYaw(((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//	pa.setMetadata(new WrappedDataWatcher(this.DataWatcher));
//	pa.setType(EntityType.SLIME);
//	return pa.getHandle();
	  
	  kPacketPlayOutEntityLiving packet = new kPacketPlayOutEntityLiving();
	  packet.setEntityID(this.Entity.getId());
	  packet.setX(this.Entity.locX);
	  packet.setY(this.Entity.locY);
	  packet.setZ(this.Entity.locZ);
	  packet.setYaw(this.Entity.yaw);
	  packet.setPitch(this.Entity.pitch);
	  packet.setDataWatcher(this.DataWatcher);
	  packet.setEntityType(EntityType.SLIME);
	  packet.setEntityID(this.Entity.getId());
	  return packet.getPacket();
	  
//    PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
//    UtilReflection.setValue("a", packet, this.Entity.getId());
//    UtilReflection.setValue("b", packet, 55);
//    UtilReflection.setValue("c", packet, EnumEntitySize.SIZE_2.a(this.Entity.locX));
//    UtilReflection.setValue("d", packet, MathHelper.floor(this.Entity.locY * 32.0D));
//    UtilReflection.setValue("e", packet, EnumEntitySize.SIZE_2.a(this.Entity.locZ));
//    UtilReflection.setValue("i", packet, ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//    UtilReflection.setValue("j", packet, ((byte)(int)(this.Entity.pitch * 256.0F / 360.0F)));
//    UtilReflection.setValue("k", packet, ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//
//    double var2 = 3.9D;
//    double var4 = 0.0D;
//    double var6 = 0.0D;
//    double var8 = 0.0D;
//
//    if (var4 < -var2)
//    {
//      var4 = -var2;
//    }
//
//    if (var6 < -var2)
//    {
//      var6 = -var2;
//    }
//
//    if (var8 < -var2)
//    {
//      var8 = -var2;
//    }
//
//    if (var4 > var2)
//    {
//      var4 = var2;
//    }
//
//    if (var6 > var2)
//    {
//      var6 = var2;
//    }
//
//    if (var8 > var2)
//    {
//      var8 = var2;
//    }
//    UtilReflection.setValue("f", packet, ((int)(var4 * 8000.0D)));
//    UtilReflection.setValue("g", packet, ((int)(var6 * 8000.0D)));
//    UtilReflection.setValue("h", packet, ((int)(var8 * 8000.0D)));
//    UtilReflection.setValue("l", packet, this.DataWatcher);
//    UtilReflection.setValue("m", packet, this.DataWatcher.b());
//
//    return packet;
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