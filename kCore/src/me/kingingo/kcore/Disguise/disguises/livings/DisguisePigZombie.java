package me.kingingo.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguisePigZombie extends DisguiseZombie
{
  public DisguisePigZombie(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.PIG_ZOMBIE;
  }

  protected String getHurtSound()
  {
    return "mob.zombiepig.zpighurt";
  }
}