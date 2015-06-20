package me.kingingo.kcore.Disguise.disguises.livings;
import java.util.Random;

import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutRelEntityMoveLook;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.Packet;

public class DisguiseBlock extends DisguiseBase
{
  private static Random _random = new Random();
  private int _blockId;
  private int _blockData;

  public DisguiseBlock(org.bukkit.entity.Entity entity, int blockId, int blockData){
    super(entity);

    this._blockId = blockId;
    this._blockData = blockData;
  }

  public int GetBlockId(){
    return this._blockId;
  }

  public byte GetBlockData(){
    return (byte)this._blockData;
  }

  public Packet GetSpawnPacket(){
//	WrapperPlayServerSpawnEntity pa = new WrapperPlayServerSpawnEntity();
//	pa.setEntityID(this.Entity.getId());
//	pa.setZ(this.Entity.locZ);
//	pa.setX(this.Entity.locX);
//	pa.setY(this.Entity.locY);
//	pa.setYaw(((byte)(int)(this.Entity.yaw * 256.0F / 360.0F)));
//	pa.setType(70);
//	pa.setObjectData((this._blockId | this._blockData << 16));
//	return pa.getHandle();
	  
	  kPacketPlayOutSpawnEntity spawn = new kPacketPlayOutSpawnEntity();
	  spawn.setX(this.Entity.locX);
	  spawn.setY(this.Entity.locY);
	  spawn.setZ(this.Entity.locZ);
	  spawn.setYaw(this.Entity.yaw);
	  spawn.setPitch(this.Entity.pitch);
	  spawn.setEntityID(this.Entity.getId());
	  spawn.setEntityType(70);
	  spawn.setObjectData((this._blockId | this._blockData << 16));
	  return spawn.getPacket();
	  
//    PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
//    UtilReflection.setValue("a", packet, this.Entity.getId());
//    UtilReflection.setValue("b", packet, MathHelper.floor(this.Entity.locX * 32.0D));
//    UtilReflection.setValue("c", packet, MathHelper.floor(this.Entity.locY * 32.0D));
//    UtilReflection.setValue("d", packet, MathHelper.floor(this.Entity.locZ * 32.0D));
//    UtilReflection.setValue("h", packet, MathHelper.d(this.Entity.pitch * 256.0F / 360.0F));
//    UtilReflection.setValue("i", packet, MathHelper.d(this.Entity.yaw * 256.0F / 360.0F));
//    UtilReflection.setValue("j", packet, 70);
//    UtilReflection.setValue("k", packet, (this._blockId | this._blockData << 16));
//
//    double d1 = this.Entity.motX;
//    double d2 = this.Entity.motY;
//    double d3 = this.Entity.motZ;
//    double d4 = 3.9D;
//
//    if (d1 < -d4) d1 = -d4;
//    if (d2 < -d4) d2 = -d4;
//    if (d3 < -d4) d3 = -d4;
//    if (d1 > d4) d1 = d4;
//    if (d2 > d4) d2 = d4;
//    if (d3 > d4) d3 = d4;
//
//    UtilReflection.setValue("e", packet, ((int)(d1 * 8000.0D)));
//    UtilReflection.setValue("f", packet, ((int)(d2 * 8000.0D)));
//    UtilReflection.setValue("g", packet, ((int)(d3 * 8000.0D)));
//
//    return packet;
  }

  protected String getHurtSound(){
    return "damage.hit";
  }

  protected float getVolume(){
    return 1.0F;
  }

  protected float getPitch(){
    return (_random.nextFloat() - _random.nextFloat()) * 0.2F + 1.0F;
  }

}