package me.kingingo.kcore.Disguise.disguises.livings;
import java.util.Random;

import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;

import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutRelEntityMoveLook;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.IBlockData;
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
  
  public DisguiseBlock(org.bukkit.entity.Entity entity, int blockId){
	    super(entity);

	    this._blockId = blockId;
	    this._blockData = 0;
	  }

  public int GetBlockId(){
	    return this._blockId;
	  }

  public byte GetBlockData(){
	  return (byte)this._blockData;
  }
	  
  public kPacket GetSpawnPacket(){
	  kPacketPlayOutSpawnEntity spawn = new kPacketPlayOutSpawnEntity();
	  spawn.setX(this.Entity.locX);
	  spawn.setY(this.Entity.locY);
	  spawn.setZ(this.Entity.locZ);
	  spawn.setYaw(this.Entity.yaw);
	  spawn.setPitch(this.Entity.pitch);
	  spawn.setEntityID(this.Entity.getId());
	  spawn.setEntityType(70);
	  spawn.setObjectData(  Block.getCombinedId( CraftMagicNumbers.getBlock(_blockId).getBlockData() ) );
	  return spawn;
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