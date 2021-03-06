package eu.epicpvp.kcore.Disguise.disguises.livings;
import java.util.Arrays;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseMonster;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.PotionBrewer;

public class DisguiseEnderman extends DisguiseMonster
{
  public DisguiseEnderman(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, new Short((short)0));
    this.DataWatcher.a(17, new Byte((byte)0));
    this.DataWatcher.a(18, new Byte((byte)0));

    int i = PotionBrewer.a(Arrays.asList(new MobEffect[] { new MobEffect(MobEffectList.FIRE_RESISTANCE.id, 777) }));
    this.DataWatcher.watch(8, Byte.valueOf((byte)(PotionBrewer.b(Arrays.asList(new MobEffect[] { new MobEffect(MobEffectList.FIRE_RESISTANCE.id, 777) })) ? 1 : 0)));
    this.DataWatcher.watch(7, Integer.valueOf(i));
  }

  public void setCarried(int id){
	  setCarried( Block.getByCombinedId(id) );
  }
  
  public void setCarried(IBlockData iblockdata)
  {
    this.DataWatcher.watch(16, Short.valueOf((short)(Block.getCombinedId(iblockdata) & 0xFFFF)));
  }
  
  public IBlockData getCarried() {
	return Block.getByCombinedId(this.DataWatcher.getShort(16) & 0xFFFF);
  }
  
  public int GetCarriedId()
  {
    return this.DataWatcher.getByte(16);
  }

  public void SetCarriedData(int i)
  {
    this.DataWatcher.watch(17, Byte.valueOf((byte)(i & 0xFF)));
  }

  public int GetCarriedData()
  {
    return this.DataWatcher.getByte(17);
  }

  public boolean co()
  {
    return this.DataWatcher.getByte(18) > 0;
  }

  public void a(boolean flag) {
    this.DataWatcher.watch(18, Byte.valueOf((byte)(flag ? 1 : 0)));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.ENDERMAN;
  }

  protected String getHurtSound()
  {
    return "mob.endermen.hit";
  }
}