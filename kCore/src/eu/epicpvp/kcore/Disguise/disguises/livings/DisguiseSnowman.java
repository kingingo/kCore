package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseGolem;

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