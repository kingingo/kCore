package me.kingingo.kcore.Disguise.disguises.livings;

import java.util.UUID;

import me.kingingo.kcore.Disguise.disguises.DisguiseHuman;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R2.Packet;

public class DisguisePlayer extends DisguiseHuman
{
  private String _name;

  public DisguisePlayer(org.bukkit.entity.Entity entity, String name)
  {
    super(entity);

    if (name.length() > 16)
    {
      name = name.substring(0, 16);
    }

    this._name = name;
  }

  public Packet GetSpawnPacket(){
//	  WrapperPlayServerNamedEntitySpawn packet = new WrapperPlayServerNamedEntitySpawn();
//	  packet.setEntityID(this.Entity.getId());
//	  packet.setProfile(new WrappedGameProfile(UUID.randomUUID(),this._name));
//	  packet.setX(MathHelper.floor(this.Entity.locX * 32.0D));
//	  packet.setY(MathHelper.floor(this.Entity.locY * 32.0D));
//	  packet.setZ(MathHelper.floor(this.Entity.locZ * 32.0D));
//	  packet.setYaw(((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//	  packet.setPitch(((byte)(int)(this.Entity.pitch * 256.0F / 360.0F)));
//	  packet.setMetadata(new WrappedDataWatcher(this.DataWatcher));
//	  return packet.getHandle();
	  
	  kPacketPlayOutNamedEntitySpawn packet = new kPacketPlayOutNamedEntitySpawn();
	  packet.setEntityID(this.Entity.getId());
	  packet.setUUID(UUID.randomUUID());
	  packet.setX(this.Entity.locX);
	  packet.setY(this.Entity.locY);
	  packet.setZ(this.Entity.locZ);
	  packet.setYaw(this.Entity.yaw);
	  packet.setPitch(this.Entity.pitch);
	  packet.setDataWatcher(this.DataWatcher);
	  
	  return packet.getPacket();
	  
//    PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
//    UtilReflection.setValue("a", packet, this.Entity.getId());
//    UtilReflection.setValue("b", packet, new GameProfile(UUID.randomUUID(), this._name));
//    UtilReflection.setValue("c", packet, MathHelper.floor(this.Entity.locX * 32.0D));
//    UtilReflection.setValue("d", packet, MathHelper.floor(this.Entity.locY * 32.0D));
//    UtilReflection.setValue("e", packet, MathHelper.floor(this.Entity.locZ * 32.0D));
//    UtilReflection.setValue("f", packet, ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//    UtilReflection.setValue("g", packet, ((byte)(int)(this.Entity.pitch * 256.0F / 360.0F)));
//    UtilReflection.setValue("i", packet, this.DataWatcher);
//
//    return packet;
  }
}