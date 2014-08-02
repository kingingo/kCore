package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;

public class DisguiseCow extends DisguiseAnimal
{
  public DisguiseCow(Entity entity)
  {
    super(entity);
  }

  protected int GetEntityTypeId()
  {
    return 92;
  }

  public String getHurtSound()
  {
    return "mob.cow.hurt";
  }
}