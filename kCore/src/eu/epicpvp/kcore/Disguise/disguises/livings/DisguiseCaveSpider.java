package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseCaveSpider extends DisguiseSpider
{
  public DisguiseCaveSpider(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.CAVE_SPIDER;
  }

  public String getHurtSound()
  {
    return "mob.cow.hurt";
  }
}