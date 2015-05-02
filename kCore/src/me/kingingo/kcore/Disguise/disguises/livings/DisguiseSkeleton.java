package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseMonster;
import net.minecraft.server.v1_8_R2.DataWatcher;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

public class DisguiseSkeleton extends DisguiseMonster
{
  public DisguiseSkeleton(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(13, Byte.valueOf((byte)0));
  }

  protected EntityType GetEntityTypeId()
  {
    return EntityType.SKELETON;
  }

  public void SetSkeletonType(Skeleton.SkeletonType skeletonType)
  {
    this.DataWatcher.watch(13, Byte.valueOf((byte)skeletonType.getId()));
  }

  public int GetSkeletonType()
  {
    return this.DataWatcher.getByte(13);
  }

  protected String getHurtSound()
  {
    return "mob.skeleton.hurt";
  }
}