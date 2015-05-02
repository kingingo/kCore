package me.kingingo.kcore.Disguise.disguises.livings;
import java.util.Arrays;

import me.kingingo.kcore.Disguise.disguises.DisguiseMonster;
import net.minecraft.server.v1_8_R2.MobEffect;
import net.minecraft.server.v1_8_R2.MobEffectList;
import net.minecraft.server.v1_8_R2.PotionBrewer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseEnderman extends DisguiseMonster
{
  public DisguiseEnderman(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, new Byte((byte)0));
    this.DataWatcher.a(17, new Byte((byte)0));
    this.DataWatcher.a(18, new Byte((byte)0));

    int i = PotionBrewer.a(Arrays.asList(new MobEffect[] { new MobEffect(MobEffectList.FIRE_RESISTANCE.id, 777) }));
    this.DataWatcher.watch(8, Byte.valueOf((byte)(PotionBrewer.b(Arrays.asList(new MobEffect[] { new MobEffect(MobEffectList.FIRE_RESISTANCE.id, 777) })) ? 1 : 0)));
    this.DataWatcher.watch(7, Integer.valueOf(i));
  }

  public void SetCarriedId(int i)
  {
    this.DataWatcher.watch(16, Byte.valueOf((byte)(i & 0xFF)));
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

  public boolean bX()
  {
    return this.DataWatcher.getByte(18) > 0;
  }
  public void a(boolean flag)
  {
    this.DataWatcher.watch(18, Byte.valueOf((byte)(flag ? 1 : 0)));
  }

  protected EntityType GetEntityTypeId()
  {
    return EntityType.ENDERMAN;
  }

  protected String getHurtSound()
  {
    return "mob.endermen.hit";
  }
}