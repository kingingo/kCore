package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseGolem;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseSnowman extends DisguiseGolem
{
  public DisguiseSnowman(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.SNOWMAN;
  }
}