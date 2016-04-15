package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseMonster;

public class DisguiseSkeleton extends DisguiseMonster
{
  public DisguiseSkeleton(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(13, Byte.valueOf((byte)0));
  }

  public EntityType GetEntityTypeId()
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