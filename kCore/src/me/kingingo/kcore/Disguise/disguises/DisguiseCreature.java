package me.kingingo.kcore.Disguise.disguises;
import org.bukkit.entity.EntityType;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import me.kingingo.kcore.PacketWrapper.WrapperPlayServerSpawnEntityLiving;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EnumEntitySize;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

public abstract class DisguiseCreature extends DisguiseInsentient
{
  public DisguiseCreature(org.bukkit.entity.Entity entity)
  {
    super(entity);
  }

  protected abstract EntityType GetEntityTypeId();

  public PacketContainer GetSpawnPacket()
  {
	  WrapperPlayServerSpawnEntityLiving pa = new WrapperPlayServerSpawnEntityLiving();
		pa.setEntityID(this.Entity.getId());
		pa.setZ(this.Entity.locZ);
		pa.setX(this.Entity.locX);
		pa.setY(this.Entity.locY);
		pa.setYaw(this.Entity.yaw);
		pa.setMetadata(new WrappedDataWatcher(this.DataWatcher));
		pa.setType(this.GetEntityTypeId());
		return pa.getHandle();
//	WrapperPlayServerSpawnEntityLiving pa = new WrapperPlayServerSpawnEntityLiving();
//	pa.setEntityID(this.Entity.getId());
//	pa.setZ(EnumEntitySize.SIZE_2.a(this.Entity.locZ));
//	pa.setX(EnumEntitySize.SIZE_2.a(this.Entity.locX));
//	pa.setY(MathHelper.floor(this.Entity.locY * 32.0D));
//	pa.setYaw(((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//	pa.setMetadata(new WrappedDataWatcher(this.DataWatcher));
//	pa.setType(this.GetEntityTypeId());
//	return pa.getHandle();
		
//    PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
//    UtilReflection.setValue("a", packet, this.Entity.getId());
//    UtilReflection.setValue("b", packet, ((byte)GetEntityTypeId()));
//    UtilReflection.setValue("c", packet, EnumEntitySize.SIZE_2.a(this.Entity.locX));
//    UtilReflection.setValue("d", packet, MathHelper.floor(this.Entity.locY * 32.0D));
//    UtilReflection.setValue("e", packet, EnumEntitySize.SIZE_2.a(this.Entity.locZ));
//    UtilReflection.setValue("i", packet, ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//    UtilReflection.setValue("j", packet, ((byte)(int)(this.Entity.pitch * 256.0F / 360.0F)));
//    UtilReflection.setValue("k",packet, ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
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
//
//    UtilReflection.setValue("f", packet, ((int)(var4 * 8000.0D)));
//    UtilReflection.setValue("g", packet, ((int)(var6 * 8000.0D)));
//    UtilReflection.setValue("h", packet, ((int)(var8 * 8000.0D)));
//
//    UtilReflection.setValue("l", packet, this.DataWatcher);
//    UtilReflection.setValue("m", packet, this.DataWatcher.b());
//
//    return packet;
  }
}