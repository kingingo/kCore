package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseGolem;

import org.bukkit.entity.Entity;

public class DisguiseSnowman extends DisguiseGolem
{
  public DisguiseSnowman(Entity entity)
  {
    super(entity);
  }

  protected int GetEntityTypeId()
  {
    return 97;
  }
}