package me.kingingo.kcore.Disguise.disguises.livings;
import java.util.UUID;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import me.kingingo.kcore.Disguise.disguises.DisguiseHuman;
import me.kingingo.kcore.Hologram.wrapper.WrapperPlayServerNamedEntitySpawn;
import me.kingingo.kcore.Hologram.wrapper.WrapperPlayServerSpawnEntityLiving;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.util.com.mojang.authlib.GameProfile;

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

  public PacketContainer GetSpawnPacket(){
	  WrapperPlayServerNamedEntitySpawn packet = new WrapperPlayServerNamedEntitySpawn();
	  packet.setEntityID(this.Entity.getId());
	  packet.setProfile(new WrappedGameProfile(UUID.randomUUID(),this._name));
	  packet.setX(MathHelper.floor(this.Entity.locX * 32.0D));
	  packet.setY(MathHelper.floor(this.Entity.locY * 32.0D));
	  packet.setZ(MathHelper.floor(this.Entity.locZ * 32.0D));
	  packet.setYaw(((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
	  packet.setPitch(((byte)(int)(this.Entity.pitch * 256.0F / 360.0F)));
	  packet.setMetadata(new WrappedDataWatcher(this.DataWatcher));
	  return packet.getHandle();
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